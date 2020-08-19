package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Role;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.UserWorldRole;
import systems.nope.worldseed.model.World;

@Data
public class UserWorldRoleDto {
    private int user;

    private int world;

    private String role;

    public UserWorldRoleDto(int user, int world, String role) {
        this.user = user;
        this.world = world;
        this.role = role;
    }

    public static UserWorldRoleDto fromUserWorldRole(UserWorldRole role) {
        return new UserWorldRoleDto(
                role.getUser().getId(),
                role.getWorld().getId(),
                role.getRole().getName()
        );
    }

    public static UserWorldRoleDto fromRoleAndWorldAndUser(Role role, World world, User user) {
        return new UserWorldRoleDto(
                user.getId(),
                world.getId(),
                role.getName()
        );
    }
}
