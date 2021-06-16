package systems.nope.worldseed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceConstant;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.stat.*;

import java.util.*;

@Service
public class StatSheetService {
    private final StatSheetRepository statSheetRepository;
    private final StatValueConstantRepository statValueConstantRepository;
    private final StatValueSynthesizedRepository statValueSynthesizedRepository;

    private final StatValueInstanceConstantRepository statValueInstanceConstantRepository;
    private final StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository;

    private final Logger logger = LoggerFactory.getLogger(StatSheetService.class);

    public StatSheetService(StatSheetRepository statSheetRepository, StatValueConstantRepository statValueConstantRepository, StatValueSynthesizedRepository statValueSynthesizedRepository, StatValueInstanceConstantRepository statValueInstanceConstantRepository, StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository) {
        this.statSheetRepository = statSheetRepository;
        this.statValueConstantRepository = statValueConstantRepository;
        this.statValueSynthesizedRepository = statValueSynthesizedRepository;
        this.statValueInstanceConstantRepository = statValueInstanceConstantRepository;
        this.statValueInstanceSynthesizedRepository = statValueInstanceSynthesizedRepository;
    }

    public List<StatSheet> findByWorld(World world) {
        return statSheetRepository.findByWorld(world);
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

    public Optional<StatValueConstant> findStatValueConstantById(int id) {
        return statValueConstantRepository.findById(id);
    }

    public Optional<StatValueSynthesized> findStatValueSynthesizedById(int id) {
        return statValueSynthesizedRepository.findById(id);
    }

    public Optional<StatValuePersonInstanceConstant> findStatValueInstanceConstantById(int id) {
        return statValueInstanceConstantRepository.findById(id);
    }

    public Optional<StatValuePersonInstanceSynthesized> findStatValueInstanceSynthesizedById(int id) {
        return statValueInstanceSynthesizedRepository.findById(id);
    }

    public StatSheetRepository getStatSheetRepository() {
        return statSheetRepository;
    }

    public void updateConstantStatInstance(StatValuePersonInstanceConstant instance, Integer valueNew) {
        instance.setValue(valueNew);
        statValueInstanceConstantRepository.save(instance);
    }

    public void deleteStatValue(Integer id) {
        Optional<StatValueConstant> optionalStatValueConstant = findStatValueConstant(id);

        if (optionalStatValueConstant.isPresent()) {
            statValueConstantRepository.delete(optionalStatValueConstant.get());
            return;
        }

        Optional<StatValueSynthesized> optionalStatValueSynthesized = findStatValueSynthesized(id);

        if (optionalStatValueSynthesized.isPresent()) {
            statValueSynthesizedRepository.delete(optionalStatValueSynthesized.get());
            return;
        }

        throw new NotFoundException(id);
    }

    public void updateStatValueContant(Integer id, Integer initialValueNew) {
        StatValueConstant statValueToUpdate = getStatValueConstant(id);
        statValueToUpdate.setInitalValue(initialValueNew);
        statValueConstantRepository.save(statValueToUpdate);
    }

    public StatValueConstant getStatValueConstant(int id) {
        Optional<StatValueConstant> optionalStatValueConstant = findStatValueConstant(id);

        if (optionalStatValueConstant.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueConstant.get();
    }

    public Optional<StatValueConstant> findStatValueConstant(int id) {
        return statValueConstantRepository.findById(id);
    }

    public void updateStatValueSynthesized(Integer id, String formulaNew) {
        StatValueSynthesized statValueSynthesized = getStatValueSynthesized(id);
        statValueSynthesized.setFormula(formulaNew);
        statValueSynthesizedRepository.save(statValueSynthesized);
    }

    public StatValueSynthesized getStatValueSynthesized(int id) {
        Optional<StatValueSynthesized> optionalStatValueSynthesized = findStatValueSynthesized(id);

        if (optionalStatValueSynthesized.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueSynthesized.get();
    }

    public Optional<StatValueSynthesized> findStatValueSynthesized(int id) {
        return statValueSynthesizedRepository.findById(id);
    }

    public Set<StatSheet> groundStatSheets(Collection<StatSheet> sheets) {
        Set<StatSheet> result = new HashSet<>(sheets);

        for (StatSheet sheet : sheets) {
            StatSheet parent = sheet.getParent();

            while (parent != null) {
                result.add(parent);
                parent = parent.getParent();
            }
        }

        return result;
    }

    public StatValueSynthesized addSynthesizedStatValueToSheet(World world, StatSheet sheet, String name, String nameShort, String unit, String formula) {
        Optional<StatValueSynthesized> referenceValue = statValueSynthesizedRepository.findByWorldAndNameAndSheet(world, name, sheet);

        if (referenceValue.isPresent())
            throw new AlreadyExistingException(name);

        StatValueSynthesized valueNew = new StatValueSynthesized(
                sheet, name, nameShort, unit, world, formula
        );

        statValueSynthesizedRepository.save(valueNew);

        for (Person assignedPerson : sheet.getAssignedPersons()) {
            StatValuePersonInstanceSynthesized instanceNew = StatValuePersonInstanceSynthesized.fromStatValueAndPerson(valueNew, assignedPerson);
            statValueInstanceSynthesizedRepository.save(instanceNew);
        }

        return valueNew;
    }

    public StatValueConstant addConstantStatValueToSheet(World world, StatSheet sheet, String name, String nameShort, String unit, Integer initialValue) {
        Optional<StatValueConstant> referenceValue = statValueConstantRepository.findByWorldAndNameAndSheet(world, name, sheet);

        if (referenceValue.isPresent())
            throw new AlreadyExistingException();

        StatValueConstant valueNew = new StatValueConstant(
                sheet, name, nameShort, unit, world, initialValue
        );

        statValueConstantRepository.save(valueNew);

        for (Person assignedPerson : sheet.getAssignedPersons()) {
            StatValuePersonInstanceConstant instanceNew = StatValuePersonInstanceConstant.fromStatValueAndPerson(valueNew, assignedPerson);
            statValueInstanceConstantRepository.save(instanceNew);
        }

        return valueNew;
    }

    public StatSheet add(World world, String name) {
        Optional<StatSheet> referenceStatSheet = statSheetRepository.findByWorldAndName(world, name);

        if (referenceStatSheet.isPresent())
            throw new AlreadyExistingException(name);

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
