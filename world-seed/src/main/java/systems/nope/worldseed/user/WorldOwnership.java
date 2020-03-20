package systems.nope.worldseed.user;

import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.world.UserWorldRole;
import systems.nope.worldseed.world.World;

import java.util.LinkedList;
import java.util.List;

public class WorldOwnership {

    public static WorldOwnership fromUserWorldRole(UserWorldRole role) {
        return new WorldOwnership(role.getWorld(), role.getRole());
    }

    public static List<WorldOwnership> fromUser(User user) {
        List<WorldOwnership> result = new LinkedList<>();

        for (UserWorldRole role : user.getWorldRoles())
            result.add(fromUserWorldRole(role));

        return result;
    }

    private final World world;

    private final Role role;

    public WorldOwnership(World world, Role role) {
        this.world = world;
        this.role = role;
    }

    public World getWorld() {
        return world;
    }

    public Role getRole() {
        return role;
    }
}
