package systems.nope.worldseed.world;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldRepository worldRepository;

    public WorldController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @GetMapping
    public List<World> all() {
        return worldRepository.findAll();
    }
}
