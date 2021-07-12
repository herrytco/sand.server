package systems.nope.worldseed.dto;

import lombok.Data;
import java.util.List;

@Data
public class WorldDto {
    private Integer id;

    private String name;

    private String description;

    private List<Integer> persons;

    private List<Integer> sheets;

    private List<UserWorldRoleDto> joinedUsers;

    private String seed;

    public WorldDto() {
    }

    public WorldDto(Integer id, String name, String description, String seed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seed = seed;
    }

    public WorldDto(Integer id, String name, String description, List<Integer> persons, List<Integer> sheets,
                    List<UserWorldRoleDto> joinedUsers, String seed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.persons = persons;
        this.sheets = sheets;
        this.joinedUsers = joinedUsers;
        this.seed = seed;
    }
}
