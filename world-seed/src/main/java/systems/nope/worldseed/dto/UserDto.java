package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.User;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private int id;

    private List<UserWorldRoleDto> worlds;

    private String name;

    private String email;

    public UserDto(int id, String name, String email, List<UserWorldRoleDto> worlds) {
        this.id = id;
        this.worlds = worlds;
        this.name = name;
        this.email = email;
    }

    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getWorldRoles().stream().map(UserWorldRoleDto::fromUserWorldRole).collect(Collectors.toList())
        );
    }
}
