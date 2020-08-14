package systems.nope.worldseed.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.stat.model.*;
import systems.nope.worldseed.util.ExpressionUtil;
import systems.nope.worldseed.world.World;

import java.util.Optional;
import java.util.Random;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public String createRandomString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private String findApiKey() {
        String key = createRandomString(256);

        Optional<Person> optionalPerson = personRepository.findByApiKey(key);

        while (optionalPerson.isPresent()) {
            key = createRandomString(256);
            optionalPerson = personRepository.findByApiKey(key);
        }

        return key;
    }

    public Optional<Person> findByApiKey(String apiKey) {
        Optional<Person> optionalPerson = personRepository.findByApiKey(apiKey);

        if (optionalPerson.isEmpty())
            return optionalPerson;

        Person person = optionalPerson.get();

        enrichPersonStats(person);

        return Optional.of(person);
    }

    public void enrichPersonStats(Person person) {
        for (StatValueInstance stat : person.getStatValues()) {
            if (stat instanceof StatValueInstanceSynthesized) {
                StatValueInstanceSynthesized synthesized = (StatValueInstanceSynthesized) stat;

                String formula = ((StatValueSynthesized) synthesized.getStatValue()).getFormula();

                // substitute variables in formual with Persons stats
                for (StatValueInstance stati : person.getStatValues()) {
                    if (stati instanceof StatValueInstanceConstant)
                        formula = formula.replaceAll(stati.getStatValue().getNameShort(), ((StatValueInstanceConstant) stati).getValue().toString());
                }

                try {
                    double result = ExpressionUtil.parseExpression(formula);
                    synthesized.setValue((int) result);
                } catch (IllegalArgumentException e) {
                    synthesized.setValue(-1);
                    logger.error(e.getMessage());
                } catch (IllegalStateException e) {
                    synthesized.setValue(-1);
                    logger.error(String.format("Internal Error while parsing formula: '%s'", formula));
                }
            }
        }
    }

    public Person add(World world, String name) {
        Optional<Person> referencePerson = personRepository.findByName(name);

        if (referencePerson.isPresent())
            throw new IllegalStateException(String.format("Duplicate Person with name %s", name));

        Person person = new Person(world, name, findApiKey());
        personRepository.save(person);

        return person;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}
