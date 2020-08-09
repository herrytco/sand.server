package systems.nope.worldseed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldConstants;
import systems.nope.worldseed.world.WorldService;

import java.util.Optional;

@Service
public class Worldinator {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private WorldService worldService;

    @Autowired
    private Authenticator authenticator;

    public World ensureTestWorldExists() {
        return ensureWorldExists(WorldConstants.nonExistingWorldName, WorldConstants.worldDescription, WorldConstants.nonExistingWorldSeed);
    }

    public World ensureWorldExists(String name, String description, String seed) {
        Optional<World> optionalWorld = worldService.getWorldRepository().findByName(name);

        User testUser = authenticator.ensureTestuserExists();

        if (optionalWorld.isEmpty()) {
            World world = new World(
                    name,
                    description,
                    seed
            );

            worldService.add(testUser, world.getName(), world.getDescription(), world.getSeed());

            return world;
        }

        return optionalWorld.get();
    }

    public WorldService getWorldService() {
        return worldService;
    }
}
