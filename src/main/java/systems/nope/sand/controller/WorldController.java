package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.sand.model.World;
import systems.nope.sand.model.request.SessionAddRequest;
import systems.nope.sand.repository.WorldRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private WorldRepository worldRepository;

    public WorldController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @GetMapping
    public List<World> all() {
        return worldRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody SessionAddRequest request
    ) {
        Optional<World> possibleExistingSession = worldRepository.findByName(request.getName());

        if (possibleExistingSession.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        World worldNew = new World(request.getName(), request.getDescription());
        worldRepository.save(worldNew);

        return ResponseEntity.ok().build();
    }
}
