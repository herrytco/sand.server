package systems.nope.worldseed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.*;
import systems.nope.worldseed.repository.stat.*;
import systems.nope.worldseed.util.ExpressionUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class StatSheetService {
    private final StatSheetRepository statSheetRepository;
    private final StatValueConstantRepository statValueConstantRepository;
    private final StatValueSynthesizedRepository statValueSynthesizedRepository;

    private final StatValueInstanceConstantRepository statValueInstanceConstantRepository;
    private final StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository;

    private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    public StatSheetService(StatSheetRepository statSheetRepository, StatValueConstantRepository statValueConstantRepository, StatValueSynthesizedRepository statValueSynthesizedRepository, StatValueInstanceConstantRepository statValueInstanceConstantRepository, StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository) {
        this.statSheetRepository = statSheetRepository;
        this.statValueConstantRepository = statValueConstantRepository;
        this.statValueSynthesizedRepository = statValueSynthesizedRepository;
        this.statValueInstanceConstantRepository = statValueInstanceConstantRepository;
        this.statValueInstanceSynthesizedRepository = statValueInstanceSynthesizedRepository;
    }

    public List<StatSheet> findByWorld(World world) { return statSheetRepository.findByWorld(world); }

    public Optional<StatSheet> findById(int id) {
        return statSheetRepository.findById(id);
    }

    public StatSheet get(int id) {
        Optional<StatSheet> optionalStatSheet = findById(id);

        if(optionalStatSheet.isEmpty())
            throw new NotFoundException(id);

        return optionalStatSheet.get();
    }

    public Optional<StatValueConstant> findStatValueConstantById(int id) {
        return statValueConstantRepository.findById(id);
    }

    public Optional<StatValueSynthesized> findStatValueSynthesizedById(int id) {
        return statValueSynthesizedRepository.findById(id);
    }

    public Optional<StatValueInstanceConstant> findStatValueInstanceConstantById(int id) {
        return statValueInstanceConstantRepository.findById(id);
    }

    public Optional<StatValueInstanceSynthesized> findStatValueInstanceSynthesizedById(int id) {
        return statValueInstanceSynthesizedRepository.findById(id);
    }

    public StatSheetRepository getStatSheetRepository() {
        return statSheetRepository;
    }

    public void updateConstantStatInstance(StatValueInstanceConstant instance, Integer valueNew) {
        instance.setValue(valueNew);
        statValueInstanceConstantRepository.save(instance);
    }

    public void enrichStatInstance(StatValueInstanceSynthesized instanceSynthesized) {
        Person person = instanceSynthesized.getPerson();
        List<SheetNode> sheetForest = constructSheetForest(person.getStatSheets());

        String formula = ((StatValueSynthesized) instanceSynthesized.getStatValue()).getFormula();

        // substitute variables in order of sheet inheritance
        for (SheetNode tree : sheetForest) {
            Optional<Stack<SheetNode>> optionalScopeStack = tree.findStackForStatValueInstance(instanceSynthesized);

            if (optionalScopeStack.isPresent()) {
                Stack<SheetNode> scopeStack = optionalScopeStack.get();

                for (SheetNode scope : scopeStack) {
                    StatSheet sheet = scope.getSheet();

                    // substitute variables in formula with Persons stats
                    for (StatValueInstance stati : person.getStatValues()) {
                        if (stati.getStatValue().getSheet() == sheet) {
                            if (stati instanceof StatValueInstanceConstant)
                                formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                        ((StatValueInstanceConstant) stati).getValue().toString());
                            else if (stati instanceof StatValueInstanceSynthesized) {
                                StatValueInstanceSynthesized s = ((StatValueInstanceSynthesized) stati);

                                if (s.getValue() != null)
                                    formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                            s.getValue().toString());
                            }

                        }
                    }
                }
                break;
            }
        }

        try {
            double result = ExpressionUtil.parseExpression(formula);
            instanceSynthesized.setValue((int) result);
        } catch (IllegalArgumentException e) {
            instanceSynthesized.setValue(-1);
            logger.error(e.getMessage());
        } catch (IllegalStateException e) {
            instanceSynthesized.setValue(-1);
            logger.error(String.format("Internal Error while parsing formula: '%s'", formula));
        }
    }

    public void updateStatValueContant(Integer id, Integer initialValueNew) {
        StatValueConstant statValueToUpdate = getStatValueConstant(id);
        statValueToUpdate.setInitalValue(initialValueNew);
        statValueConstantRepository.save(statValueToUpdate);
    }

    public StatValueConstant getStatValueConstant(int id) {
        Optional<StatValueConstant> optionalStatValueConstant = findStatValueConstant(id);

        if(optionalStatValueConstant.isEmpty())
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

        if(optionalStatValueSynthesized.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueSynthesized.get();
    }

    public Optional<StatValueSynthesized> findStatValueSynthesized(int id) {
        return statValueSynthesizedRepository.findById(id);
    }

    public void enrichPersonStats(Person person) {
        List<SheetNode> sheetForest = constructSheetForest(person.getStatSheets());

        for (StatValueInstance statValueInstance : person.getStatValues()) {

            if (statValueInstance instanceof StatValueInstanceSynthesized) {
                StatValueInstanceSynthesized stat = (StatValueInstanceSynthesized) statValueInstance;
                String formula = ((StatValueSynthesized) stat.getStatValue()).getFormula();

                // substitute variables in order of sheet inheritance
                for (SheetNode tree : sheetForest) {
                    Optional<Stack<SheetNode>> optionalScopeStack = tree.findStackForStatValueInstance(statValueInstance);

                    if (optionalScopeStack.isPresent()) {
                        Stack<SheetNode> scopeStack = optionalScopeStack.get();

                        for (SheetNode scope : scopeStack) {
                            StatSheet sheet = scope.getSheet();


                            // substitute variables in formula with Persons stats
                            for (StatValueInstance stati : person.getStatValues()) {
                                if (stati.getStatValue().getSheet() == sheet) {
                                    if (stati instanceof StatValueInstanceConstant)
                                        formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                                ((StatValueInstanceConstant) stati).getValue().toString());
                                    else if (stati instanceof StatValueInstanceSynthesized) {
                                        StatValueInstanceSynthesized s = ((StatValueInstanceSynthesized) stati);

                                        if (s.getValue() != null)
                                            formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                                    s.getValue().toString());
                                    }

                                }
                            }
                        }
                        break;
                    }
                }

                try {
                    double result = ExpressionUtil.parseExpression(formula);
                    stat.setValue((int) result);
                } catch (IllegalArgumentException e) {
                    stat.setValue(-1);
                    logger.error(e.getMessage());
                } catch (IllegalStateException e) {
                    stat.setValue(-1);
                    logger.error(String.format("Internal Error while parsing formula: '%s'", formula));
                }
            }

        }
    }

    private List<SheetNode> constructSheetForest(List<StatSheet> statSheets) {
        List<SheetNode> forest = new LinkedList<>();
        List<StatSheet> listCopy = new LinkedList<>(statSheets);
        List<StatSheet> used = new LinkedList<>();

        boolean changed;

        do {
            changed = false;

            for (StatSheet sheet : listCopy) {
                // root node, add new tree to forest
                if (sheet.getParent() == null) {
                    forest.add(new SheetNode(sheet));
                    used.add(sheet);
                    changed = true;
                }

                // child node, search for tree, the sheet belongs to
                for (SheetNode tree : forest) {
                    Optional<SheetNode> parentNode = tree.findSheetNodeContainingSheet(sheet.getParent());

                    if (parentNode.isPresent()) {
                        parentNode.get().addChild(sheet);
                        used.add(sheet);
                        changed = true;
                    }
                }
            }

            listCopy.removeAll(used);
        } while (changed);

        return forest;
    }

    public StatValueSynthesized addSynthesizedStatValueToSheet(World world, StatSheet sheet, String name, String nameShort, String unit, String formula) {
        Optional<StatValueSynthesized> referenceValue = statValueSynthesizedRepository.findByWorldAndNameAndSheet(world, name, sheet);

        if (referenceValue.isPresent())
            throw new AlreadyExistingException(name);

        StatValueSynthesized valueNew = new StatValueSynthesized(
                sheet, name, nameShort, unit, world, formula
        );

        statValueSynthesizedRepository.save(valueNew);

        for(Person assignedPerson : sheet.getAssignedPersons()) {
            StatValueInstanceSynthesized instanceNew = StatValueInstanceSynthesized.fromStatValueAndPerson(valueNew, assignedPerson);
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

        for(Person assignedPerson : sheet.getAssignedPersons()) {
            StatValueInstanceConstant instanceNew = StatValueInstanceConstant.fromStatValueAndPerson(valueNew, assignedPerson);
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
