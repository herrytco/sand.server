package systems.nope.worldseed.world;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorldService {

    private final WorldRepository worldRepository;

    public WorldRepository getWorldRepository() {
        return worldRepository;
    }

    public WorldService(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    public boolean add(String name, String description) {
        Optional<World> reference = worldRepository.findByName(name);

        if (reference.isPresent())
            return false;

        World worldNew = new World(name, description);
        worldRepository.save(worldNew);

        System.out.println(String.format("WORLD-ID: %d", worldNew.getId()));
        return true;
    }
}
