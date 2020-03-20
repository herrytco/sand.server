package systems.nope.worldseed.world;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.category.CategoryService;
import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.role.RoleService;
import systems.nope.worldseed.role.RoleType;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.WorldOwnership;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class WorldService {

    private final WorldRepository worldRepository;
    private final RoleService roleService;
    private final UserWorldRoleRepository userWorldRoleRepository;
    private final CategoryService categoryService;

    public WorldRepository getWorldRepository() {
        return worldRepository;
    }

    public WorldService(WorldRepository worldRepository, RoleService roleService, UserWorldRoleRepository userWorldRoleRepository, CategoryService categoryService) {
        this.worldRepository = worldRepository;
        this.roleService = roleService;
        this.userWorldRoleRepository = userWorldRoleRepository;
        this.categoryService = categoryService;
    }

    private String findSeed() {
        String seed = generateSeed();

        while (worldRepository.findBySeed(seed).isPresent())
            seed = generateSeed();

        return seed;
    }

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
     * Adds a new world to the system. The user creating the world will automatically added as owner of this world.
     * All default article categories are added to the system tied to this world as well.
     *
     * @param creator
     * @param name
     * @param description
     * @param seed
     * @return
     */
    public WorldOwnership add(User creator, String name, String description, String seed) {
        Optional<World> reference = worldRepository.findByName(name);

        if (reference.isPresent())
            throw new IllegalStateException(String.format("Duplicate World with name %s", name));

        World worldNew = new World(name, description, seed);

        // add world
        worldRepository.save(worldNew);

        // set requesting user to the owner of the new world
        Role owner = roleService.getRoleForType(RoleType.Owner);
        UserWorldRole userWorldRole = new UserWorldRole(creator, worldNew, owner);
        userWorldRoleRepository.save(userWorldRole);

        // add default categories
        categoryService.addDefaultCategories(worldNew);

        return new WorldOwnership(
                worldNew,
                owner
        );
    }

    public WorldOwnership add(User creator, String name, String description) {
        return add(creator, name, description, findSeed());
    }
}
