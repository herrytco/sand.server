package systems.nope.worldseed.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

@Service
public class StatSheetTestUtil {

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private StatSheetService statSheetService;

    private StatSheet ensuredStatShet;

    public StatSheet getEnsuredStatShet() {
        return ensuredStatShet;
    }

    public StatSheet ensureTestStatSheet() {
        return ensureStatSheet(StatSheetConstants.testSheetName);
    }

    public StatSheet ensureStatSheet(String name) {
        World testWorld = worldTestUtil.ensureTestWorldExists();

        Optional<StatSheet> optionalSheet = statSheetService.getStatSheetRepository().findByWorldAndName(testWorld, name);

        ensuredStatShet = optionalSheet.orElseGet(() -> statSheetService.add(testWorld, name));
        return ensuredStatShet;
    }
}
