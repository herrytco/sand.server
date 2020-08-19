package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.Role;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.UserWorldRole;
import systems.nope.worldseed.model.World;

import java.util.LinkedList;
import java.util.List;

public class WorldOwnershipDto {

    private final WorldDto world;
    private final Role role;

    public static WorldOwnershipDto fromUserWorldRole(UserWorldRole role) {
        return new WorldOwnershipDto(role.getWorld(), role.getRole());
    }

    public static List<WorldOwnershipDto> fromUser(User user) {
        List<WorldOwnershipDto> result = new LinkedList<>();

        for (UserWorldRole role : user.getWorldRoles())
            result.add(fromUserWorldRole(role));

        return result;
    }

    public WorldOwnershipDto(World world, Role role) {
        this.world = WorldDto.fromWorld(world);
        this.role = role;
    }

    public WorldDto getWorld() {
        return world;
    }

    public Role getRole() {
        return role;
    }
}
