package systems.nope.worldseed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.model.CharacterOwnership;
import systems.nope.worldseed.model.PlayerCharacter;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.request.PlayerCharacterCreationRequest;
import systems.nope.worldseed.repository.CharacterOwnershipRepository;
import systems.nope.worldseed.repository.PlayerCharacterRepository;
import systems.nope.worldseed.repository.UserRepository;
import systems.nope.worldseed.repository.WorldRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/characters")
public class PlayerCharacterController {

    public PlayerCharacterController(WorldRepository worldRepository, PlayerCharacterRepository playerCharacterRepository, UserRepository userRepository, CharacterOwnershipRepository characterOwnershipRepository) {
        this.worldRepository = worldRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.userRepository = userRepository;
        this.characterOwnershipRepository = characterOwnershipRepository;
    }

    private final WorldRepository worldRepository;
    private final PlayerCharacterRepository playerCharacterRepository;
    private final UserRepository userRepository;
    private final CharacterOwnershipRepository characterOwnershipRepository;

    /**
     * assigns a character to a player (as in he can control the character from this point forward)
     *
     * @param worldIdString - world ID
     * @param charIdString  - ID of the character in the world
     * @param ownerIdString - ID of the player who gets the control
     * @return 202, if everything went good, 403 else
     */
    @PostMapping("/world/{worldId}/character/{charId}/owner/{ownerId}")
    public ResponseEntity<?> addOwnership(
            @PathVariable("worldId") String worldIdString,
            @PathVariable("charId") String charIdString,
            @PathVariable("ownerId") String ownerIdString
    ) {
        Integer worldId, charId, ownerId;

        try {
            worldId = Integer.parseInt(worldIdString);
            charId = Integer.parseInt(charIdString);
            ownerId = Integer.parseInt(ownerIdString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IDs must be a numberic");
        }

        World targetWorld;
        User owner;
        PlayerCharacter playerCharacter;

        try {
            targetWorld = worldRepository.getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("World with ID '%s' not found.", worldIdString));
        }

        try {
            playerCharacter = playerCharacterRepository.getOne(charId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Character with ID '%s' not found.", charIdString));
        }

        try {
            owner = userRepository.getOne(ownerId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("User with ID '%s' not found.", ownerIdString));
        }

        CharacterOwnership ownership = new CharacterOwnership(owner, playerCharacter, targetWorld);
        characterOwnershipRepository.save(ownership);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * adds a new character to the world. this does NOT mean that the character can be controlled by the
     * creator
     *
     * @param worldIdString - world ID
     * @param request       - contains all information about the new character
     * @return 202 if everything went ok, 403 else
     */
    @PostMapping("/world/{worldId}")
    public ResponseEntity<?> add(
            @PathVariable("worldId") String worldIdString,
            @RequestBody PlayerCharacterCreationRequest request
    ) {
        try {
            Integer worldId = Integer.parseInt(worldIdString);
            World targetWorld = worldRepository.getOne(worldId);

            Optional<PlayerCharacter> characterWithSameName = playerCharacterRepository.findByWorldAndName(targetWorld, request.getName());

            if (characterWithSameName.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Character with the name '%s' already exists.", request.getName()));

            PlayerCharacter characterNew = new PlayerCharacter(
                    targetWorld,
                    request.getName(),
                    request.getBodyInfo(),
                    request.getBio()
            );

            playerCharacterRepository.save(characterNew);

            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("World ID must be a number");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("World with ID '%s' not found.", worldIdString));
        }

    }
}
