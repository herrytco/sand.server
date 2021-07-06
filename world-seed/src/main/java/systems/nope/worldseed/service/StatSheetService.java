package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.person.PersonRepository;
import systems.nope.worldseed.repository.stat.*;
import systems.nope.worldseed.service.person.PersonService;

import java.util.*;

@Service
public class StatSheetService {
    private final StatSheetRepository statSheetRepository;
    private final StatValueConstantRepository statValueConstantRepository;
    private final StatValueSynthesizedRepository statValueSynthesizedRepository;

    private final PersonRepository personRepository;
    private final PersonService personService;

    private final StatValueInstanceService statValueInstanceService;

    public StatSheetService(StatSheetRepository statSheetRepository,
                            StatValueConstantRepository statValueConstantRepository,
                            StatValueSynthesizedRepository statValueSynthesizedRepository,
                            PersonRepository personRepository, PersonService personService, StatValueInstanceService statValueInstanceService) {
        this.statSheetRepository = statSheetRepository;
        this.statValueConstantRepository = statValueConstantRepository;
        this.statValueSynthesizedRepository = statValueSynthesizedRepository;
        this.personRepository = personRepository;
        this.personService = personService;
        this.statValueInstanceService = statValueInstanceService;
    }

    public List<StatSheet> findByWorld(World world) {
        return statSheetRepository.findByWorld(world);
    }

    public List<StatSheet> findByItem(Item item) {
        return statSheetRepository.findByAssignedItems(item);
    }

    public Optional<StatSheet> findById(int id) {
        return statSheetRepository.findById(id);
    }

    public StatSheet get(int id) {
        Optional<StatSheet> optionalStatSheet = findById(id);

        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(id);

        return optionalStatSheet.get();
    }

    public StatSheet add(World world, String name) {
        return add(world, name, null);
    }

    public StatSheet add(World world, String name, StatSheet parent) {
        Optional<StatSheet> referenceStatSheet = statSheetRepository.findByWorldAndName(world, name);

        if (referenceStatSheet.isPresent())
            throw new AlreadyExistingException(name);

        StatSheet sheetNew = new StatSheet(name, world);

        if (parent != null)
            sheetNew.setParent(parent);

        statSheetRepository.save(sheetNew);

        return sheetNew;
    }

    public StatValueConstant getConstant(int id) {
        Optional<StatValueConstant> optionalStatValueConstant = findConstant(id);

        if (optionalStatValueConstant.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueConstant.get();
    }

    public Optional<StatValueConstant> findConstant(int id) {
        return statValueConstantRepository.findById(id);
    }

    public StatValueSynthesized getSynthesized(int id) {
        Optional<StatValueSynthesized> optionalStatValueSynthesized = findSynthesized(id);

        if (optionalStatValueSynthesized.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueSynthesized.get();
    }

    public Optional<StatValueSynthesized> findSynthesized(int id) {
        return statValueSynthesizedRepository.findById(id);
    }

    public StatSheetRepository getStatSheetRepository() {
        return statSheetRepository;
    }

    public void deleteStatValue(Integer id) {
        Optional<StatValueConstant> optionalStatValueConstant = findConstant(id);

        if (optionalStatValueConstant.isPresent()) {
            statValueConstantRepository.delete(optionalStatValueConstant.get());
            return;
        }

        Optional<StatValueSynthesized> optionalStatValueSynthesized = findSynthesized(id);

        if (optionalStatValueSynthesized.isPresent()) {
            statValueSynthesizedRepository.delete(optionalStatValueSynthesized.get());
            return;
        }

        throw new NotFoundException(id);
    }

    public void updateStatValueContant(Integer id, Integer initialValueNew) {
        StatValueConstant statValueToUpdate = getConstant(id);
        statValueToUpdate.setInitalValue(initialValueNew);
        statValueConstantRepository.save(statValueToUpdate);
    }

    public void updateStatValueSynthesized(Integer id, String formulaNew) {
        StatValueSynthesized statValueSynthesized = getSynthesized(id);
        statValueSynthesized.setFormula(formulaNew);
        statValueSynthesizedRepository.save(statValueSynthesized);
    }

    public StatValueSynthesized addSynthesizedStatValueToSheet(World world, StatSheet sheet, String name,
                                                               String nameShort, String unit, String formula,
                                                               Boolean isResource) {
        Optional<StatValueSynthesized> referenceValue = statValueSynthesizedRepository.findByWorldAndNameAndSheet(world, name, sheet);

        if (referenceValue.isPresent())
            throw new AlreadyExistingException(name);

        StatValueSynthesized valueNew = new StatValueSynthesized(sheet, name, nameShort, unit, world, formula,
                isResource);

        statValueSynthesizedRepository.save(valueNew);

        for (Person assignedPerson : sheet.getAssignedPersons()) {
            assignedPerson.getStatValues().add(
                    statValueInstanceService.add(
                            assignedPerson.getWorld(),
                            valueNew
                    )
            );

            personRepository.save(assignedPerson);
            personService.realizePersonResources(assignedPerson);
        }

        return valueNew;
    }

    public StatValueConstant addConstantStatValueToSheet(World world, StatSheet sheet, String name, String nameShort,
                                                         String unit, Integer initialValue, Boolean isResource) {
        Optional<StatValueConstant> referenceValue = statValueConstantRepository.findByWorldAndNameAndSheet(world, name, sheet);

        if (referenceValue.isPresent())
            throw new AlreadyExistingException();

        StatValueConstant valueNew = new StatValueConstant(sheet, name, nameShort, unit, world, initialValue,
                isResource);

        statValueConstantRepository.save(valueNew);

        for (Person assignedPerson : sheet.getAssignedPersons()) {
            assignedPerson.getStatValues().add(
                    statValueInstanceService.add(
                            assignedPerson.getWorld(),
                            valueNew,
                            valueNew.getInitalValue()
                    )
            );

            personRepository.save(assignedPerson);
        }

        return valueNew;
    }

}
