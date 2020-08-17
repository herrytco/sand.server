package systems.nope.worldseed.world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.user.User;

import java.util.Optional;

@Service
public class WorldTestUtil {

    @Autowired
    private WorldService worldService;

    @Autowired
    private UserTestUtil userTestUtil;

    private World ensuredInstance;

    public World ensureTestWorldExists() {
        return ensureWorldExists(WorldConstants.nonExistingWorldName, WorldConstants.worldDescription, WorldConstants.nonExistingWorldSeed);
    }

    public World ensureWorldExists(String name, String description, String seed) {
        Optional<World> optionalWorld = worldService.getWorldRepository().findByName(name);

        User testUser = userTestUtil.ensureTestuserExists();

        if (optionalWorld.isEmpty()) {
            World world = new World(
                    name,
                    description,
                    seed
            );

            worldService.add(testUser, world.getName(), world.getDescription(), world.getSeed());

            ensuredInstance = world;
            return world;
        }

        ensuredInstance = optionalWorld.get();
        return optionalWorld.get();
    }

    public WorldService getWorldService() {
        return worldService;
    }

    public World getEnsuredInstance() {
        return ensuredInstance;
    }
}
