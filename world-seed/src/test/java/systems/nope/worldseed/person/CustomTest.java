package systems.nope.worldseed.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.service.person.PersonService;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomTest {

    @Autowired
    private PersonService personService;

    @Test
    public void customTest1() {
        Optional<Person> personOptional = personService.findByApiKey("wqegdwqepocttgquzojdgfqmzgibuaglnfqqtmthwdmwezkcuuolatmwlbtfmufxzwgqxzpzcekjjwajvtrjzthzqxumtsfepxunqjhdnxgwvldljquogiacqpywswfspwkcxytyonesncaxckafqghjkxbvneyovjqokcirxvcolzjlwvhaffnxlxjpclrhjeqwifnwknfzaassdqyehbwkgglcoxjrfnqycjfwjoaibdbcwjjraynnhwjejewl");

        if (personOptional.isEmpty()) {
            System.out.println("No person found, aborting...");
            return;
        }

        Person person = personOptional.get();

        for(StatValueInstance instance : person.getStatValues())
            System.out.println(instance.getStatValue().getNameShort()+": "+instance.getValue());
    }
}
