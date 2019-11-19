package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.sand.model.User;
import systems.nope.sand.model.World;
import systems.nope.sand.model.WorldAssignment;
import systems.nope.sand.model.request.SessionAddRequest;
import systems.nope.sand.repository.UserRepository;
import systems.nope.sand.repository.WorldAssignmentRepository;
import systems.nope.sand.repository.WorldRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    public WorldController(WorldRepository worldRepository, UserRepository userRepository, WorldAssignmentRepository worldAssignmentRepository) {
        this.worldRepository = worldRepository;
        this.userRepository = userRepository;
        this.worldAssignmentRepository = worldAssignmentRepository;
    }

    private final WorldRepository worldRepository;
    private final UserRepository userRepository;
    private final WorldAssignmentRepository worldAssignmentRepository;

    @GetMapping
    public List<World> all() {
        return worldRepository.findAll();
    }

    @GetMapping("/seed/{seed}")
    public ResponseEntity<World> getBySeed(
            @PathVariable String seed
    ) {
        Optional<World> optionalTargetWorld = worldRepository.findBySeed(seed);

        if (optionalTargetWorld.isPresent())
            return ResponseEntity.ok().body(optionalTargetWorld.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * assigns a user to a world s.t. he is able to see its contents in the client
     *
     * @param worldId - unique ID of the world
     * @param userId  - unique ID for the user
     * @return 200 if ok, 403 else
     */
    @PostMapping("/world/{worldId}/user/{userId}")
    public ResponseEntity<?> assignUserToWorld(
            @PathVariable("worldId") Integer worldId,
            @PathVariable("userId") Integer userId
    ) {
        try {
            World targetWorld = worldRepository.getOne(worldId);
            User user = userRepository.getOne(userId);

            WorldAssignment assignment = new WorldAssignment(targetWorld, user);
            worldAssignmentRepository.save(assignment);

            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or world does not exist");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> allForUser(
            @PathVariable("userId") Integer userId
    ) {
        try {
            User user = userRepository.getOne(userId);

            List<World> worlds = user.getWorlds().stream().map(WorldAssignment::getWorld).collect(Collectors.toList());

            return ResponseEntity.ok(worlds);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("User with the id '%d' does not exist", userId));
        }
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
