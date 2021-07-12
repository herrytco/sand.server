package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.dto.UserWorldRoleDto;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.repository.UserWorldRoleRepository;
import systems.nope.worldseed.repository.WorldRepository;
import systems.nope.worldseed.service.person.PersonService;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class WorldService {

    private final WorldRepository worldRepository;
    private final RoleService roleService;
    private final UserWorldRoleRepository userWorldRoleRepository;
    private final CategoryService categoryService;
    private final PersonService personService;

    public WorldRepository getWorldRepository() {
        return worldRepository;
    }

    public WorldService(WorldRepository worldRepository, RoleService roleService,
                        UserWorldRoleRepository userWorldRoleRepository, CategoryService categoryService,
                        PersonService personService) {
        this.worldRepository = worldRepository;
        this.roleService = roleService;
        this.userWorldRoleRepository = userWorldRoleRepository;
        this.categoryService = categoryService;
        this.personService = personService;
    }

    public void join(User user, World world) {
        Role visitor = roleService.getRoleForType(RoleType.Visitor);

        UserWorldRole userWorldRole = new UserWorldRole(user, world, visitor);
        userWorldRoleRepository.save(userWorldRole);
    }

    public UserWorldRoleDto add(User creator, String name, String description) {
        return add(creator, name, description, findSeed());
    }

    public UserWorldRoleDto add(User creator, String name, String description, String seed) {
        Optional<World> reference = worldRepository.findByName(name);

        if (reference.isPresent())
            throw new AlreadyExistingException(name);

        World worldNew = new World(name, description, seed);

        // add world
        worldRepository.save(worldNew);

        // set requesting user to the owner of the new world
        Role owner = roleService.getRoleForType(RoleType.Owner);
        UserWorldRole userWorldRole = new UserWorldRole(creator, worldNew, owner);
        userWorldRoleRepository.save(userWorldRole);

        // add default categories
        categoryService.addDefaultCategories(worldNew);

        return UserWorldRoleDto.fromRoleAndWorldAndUser(
                owner,
                worldNew,
                creator
        );
    }

    public Optional<World> findBySeed(String seed) {
        Optional<World> world = worldRepository.findBySeed(seed);

        if (world.isPresent()) {
            World result = world.get();

            for (Person p : result.getPersons())
                personService.enrichPersonStats(p);

            return Optional.of(result);
        }

        return Optional.empty();
    }

    public Optional<World> find(int id) {
        Optional<World> world = worldRepository.findById(id);

        if (world.isPresent()) {
            World result = world.get();

            if(result.getPersons() != null)
                for (Person p : result.getPersons())
                    personService.enrichPersonStats(p);

            return Optional.of(result);
        }

        return Optional.empty();
    }

    public World get(int id, boolean enrich) {
        Optional<World> optionalWorld = find(id);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(id);

        World world = optionalWorld.get();

        if (enrich && world.getPersons() != null)
            for (Person p : world.getPersons())
                personService.enrichPersonStats(p);

        return world;
    }

    public World get(int id) {
        return get(id, true);
    }

    /**
     * @return unused 6-digit seed usable for a new world
     */
    private String generateSeed() {
        SecureRandom srng = new SecureRandom();

        StringBuilder seed = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = srng.nextInt() % 10;

            if (i == 0 && digit == 0)
                digit++;

            seed.append(Math.abs(digit));
        }

        return seed.toString();
    }

    /**
     * create random seed until an unused is found
     *
     * @return unused seed usable for a new world
     */
    private String findSeed() {
        String seed = generateSeed();

        while (worldRepository.findBySeed(seed).isPresent())
            seed = generateSeed();

        return seed;
    }
}
