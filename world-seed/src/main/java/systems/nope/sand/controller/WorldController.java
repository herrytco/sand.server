package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import systems.nope.sand.config.SpringUser;
import systems.nope.sand.constants.WorldConstants;
import systems.nope.sand.model.User;
import systems.nope.sand.model.World;
import systems.nope.sand.model.WorldAssignment;
import systems.nope.sand.model.request.WorldAddRequest;
import systems.nope.sand.repository.UserRepository;
import systems.nope.sand.repository.WorldAssignmentRepository;
import systems.nope.sand.repository.WorldRepository;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
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
    private char[] alphabet = "1234567890abcdef".toCharArray();

    @GetMapping
    public List<World> all() {
        return worldRepository.findAll();
    }

    /**
     * searches for a world
     *
     * @param seed - unique 6-digit String assigned to a world
     * @return 200 with the World in the body, 404 else
     */
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

    /**
     * gets the list of worlds where the user is assigned to
     *
     * @param userId - specifies the requesting user
     * @return List of World's or 403
     */
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

    /**
     * adds a new world to the system
     *
     * @param request - holding the data from the client
     * @return new created world
     */
    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody WorldAddRequest request
    ) {
        Optional<World> possibleExistingSession = worldRepository.findByName(request.getName());

        if (possibleExistingSession.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        Optional<User> authenticatedUser = userRepository.findByEmail(name);

        if(authenticatedUser.isPresent()) {
            World worldNew = new World(authenticatedUser.get(), request.getName(), request.getDescription(), request.getWorldAnvilLink());

            // calculate the seed for the new world
            String seed = "";
            Optional<World> worldWithSeed;

            do {
                SecureRandom srng = new SecureRandom();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < WorldConstants.seedLength; i++)
                    sb.append(alphabet[srng.nextInt(alphabet.length)]);
                seed = sb.toString();
                worldWithSeed = worldRepository.findBySeed(seed);
            } while(worldWithSeed.isPresent());

            worldNew.setSeed(seed);
            worldRepository.save(worldNew);

            return ResponseEntity.ok().body(worldNew);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
