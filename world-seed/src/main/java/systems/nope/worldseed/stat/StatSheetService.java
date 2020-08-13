package systems.nope.worldseed.stat;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.person.PersonService;
import systems.nope.worldseed.stat.model.*;
import systems.nope.worldseed.world.World;

import java.util.Optional;

@Service
public class StatSheetService {
    private final PersonService personService;
    private final StatSheetRepository statSheetRepository;
    private final StatValueConstantRepository statValueConstantRepository;
    private final StatValueSynthesizedRepository statValueSynthesizedRepository;

    private final StatValueInstanceConstantRepository statValueInstanceConstantRepository;
    private final StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository;

    public StatSheetService(PersonService personService, StatSheetRepository statSheetRepository, StatValueConstantRepository statValueConstantRepository, StatValueSynthesizedRepository statValueSynthesizedRepository, StatValueInstanceConstantRepository statValueInstanceConstantRepository, StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository) {
        this.personService = personService;
        this.statSheetRepository = statSheetRepository;
        this.statValueConstantRepository = statValueConstantRepository;
        this.statValueSynthesizedRepository = statValueSynthesizedRepository;
        this.statValueInstanceConstantRepository = statValueInstanceConstantRepository;
        this.statValueInstanceSynthesizedRepository = statValueInstanceSynthesizedRepository;
    }

    public StatSheetRepository getStatSheetRepository() {
        return statSheetRepository;
    }

    public void updateConstantStatInstance(StatValueInstanceConstant instance, Integer valueNew) {
        instance.setValue(valueNew);
        statValueInstanceConstantRepository.save(instance);
    }

    public void addStatSheetToPerson(Person person, StatSheet sheet) {
        for (StatValue value : sheet.getStatValues()) {
            StatValueInstance instance = null;

            boolean duplicate = false;

            for (StatValueInstance i : person.getStatValues())
                if (i.getStatValue().getId() == value.getId()) {
                    duplicate = true;
                    break;
                }

            if (duplicate)
                continue;

            if (value instanceof StatValueConstant) {
                instance = new StatValueInstanceConstant(person.getWorld(), value, person, ((StatValueConstant) value).getInitalValue());
                statValueInstanceConstantRepository.save((StatValueInstanceConstant) instance);
            } else if (value instanceof StatValueSynthesized) {
                instance = new StatValueInstanceSynthesized(person.getWorld(), value, person);
                statValueInstanceSynthesizedRepository.save((StatValueInstanceSynthesized) instance);
            }

            if (instance != null)
                person.getStatValues().add(instance);
        }

        personService.getPersonRepository().save(person);
    }

    public StatValue addSynthesizedStatValueToSheet(World world, StatSheet sheet, String name, String nameShort, String unit, String formula) {
        Optional<StatValueSynthesized> referenceValue = statValueSynthesizedRepository.findByWorldAndName(world, name);

        if (referenceValue.isPresent())
            throw new IllegalStateException(String.format("Duplicate Stat with name %s", name));

        StatValueSynthesized valueNew = new StatValueSynthesized(
                sheet, name, nameShort, unit, world, formula
        );

        statValueSynthesizedRepository.save(valueNew);

        return valueNew;
    }

    public StatValue addConstantStatValueToSheet(World world, StatSheet sheet, String name, String nameShort, String unit, Integer initialValue) {
        Optional<StatValueConstant> referenceValue = statValueConstantRepository.findByWorldAndName(world, name);

        if (referenceValue.isPresent())
            throw new IllegalStateException(String.format("Duplicate Stat with name %s", name));

        StatValueConstant valueNew = new StatValueConstant(
                sheet, name, nameShort, unit, world, initialValue
        );

        statValueConstantRepository.save(valueNew);

        return valueNew;
    }

    public StatSheet add(World world, String name) {
        Optional<StatSheet> referenceStatSheet = statSheetRepository.findByWorldAndName(world, name);

        if (referenceStatSheet.isPresent())
            throw new IllegalStateException(String.format("Duplicate Stat Sheet with name %s", name));

        StatSheet sheetNew = new StatSheet(name, world);

        statSheetRepository.save(sheetNew);

        return sheetNew;
    }

    public StatValueConstantRepository getStatValueConstantRepository() {
        return statValueConstantRepository;
    }

    public StatValueSynthesizedRepository getStatValueSynthesizedRepository() {
        return statValueSynthesizedRepository;
    }

    public StatValueInstanceConstantRepository getStatValueInstanceConstantRepository() {
        return statValueInstanceConstantRepository;
    }

    public StatValueInstanceSynthesizedRepository getStatValueInstanceSynthesizedRepository() {
        return statValueInstanceSynthesizedRepository;
    }
}
