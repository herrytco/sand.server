package systems.nope.worldseed.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.stat.StatValueConstantRepository;
import systems.nope.worldseed.repository.stat.StatValueSynthesizedRepository;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

@Service
public class StatTestUtil {

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private StatSheetTestUtil statSheetTestUtil;

    @Autowired
    private StatSheetService statSheetService;

    @Autowired
    private StatValueConstantRepository statValueConstantRepository;

    @Autowired
    private StatValueSynthesizedRepository statValueSynthesizedRepository;

    private StatValueSynthesized statValueSynthesized;
    private StatValueConstant statValueConstant;

    public void ensureTestStatsExist() {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        StatSheet testSheet = statSheetTestUtil.ensureTestStatSheet();

        Optional<StatValueConstant> statValueConstantOptional = statValueConstantRepository.findByWorldAndNameAndSheet(
                testWorld,
                StatSheetConstants.testConstantStatName,
                testSheet
        );

        statValueConstant = statValueConstantOptional.orElseGet(() -> (StatValueConstant) statSheetService.addConstantStatValueToSheet(testWorld, testSheet,
                StatSheetConstants.testConstantStatName, StatSheetConstants.testConstantStatName,
                StatSheetConstants.testConstantStatUnit, StatSheetConstants.testConstantStatDefault, false));

        Optional<StatValueSynthesized> statValueSynthesizedOptional = statValueSynthesizedRepository.findByWorldAndNameAndSheet(
                testWorld,
                StatSheetConstants.testConstantStatName,
                testSheet
        );

        statValueSynthesized = statValueSynthesizedOptional
                .orElseGet(() -> (StatValueSynthesized) statSheetService.addSynthesizedStatValueToSheet(testWorld,
                        testSheet, StatSheetConstants.testSyntheticStatName, StatSheetConstants.testSyntheticStatName,
                        StatSheetConstants.testConstantStatUnit, StatSheetConstants.testSyntheticStatFormula, false));
    }

    public StatValueSynthesized getStatValueSynthesized() {
        return statValueSynthesized;
    }

    public StatValueConstant getStatValueConstant() {
        return statValueConstant;
    }
}
