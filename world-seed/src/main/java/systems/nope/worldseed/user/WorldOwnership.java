package systems.nope.worldseed.user;

import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.world.OutWorld;
import systems.nope.worldseed.world.UserWorldRole;
import systems.nope.worldseed.world.World;

import java.util.LinkedList;
import java.util.List;

public class WorldOwnership {

    private final OutWorld world;
    private final Role role;

    public static WorldOwnership fromUserWorldRole(UserWorldRole role) {
        return new WorldOwnership(role.getWorld(), role.getRole());
    }

    public static List<WorldOwnership> fromUser(User user) {
        List<WorldOwnership> result = new LinkedList<>();

        for (UserWorldRole role : user.getWorldRoles())
            result.add(fromUserWorldRole(role));

        return result;
    }

    public WorldOwnership(World world, Role role) {
        this.world = OutWorld.fromWorld(world);
        this.role = role;
    }

    public OutWorld getWorld() {
        return world;
    }

    public Role getRole() {
        return role;
    }
}
