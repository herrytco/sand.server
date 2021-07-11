package systems.nope.worldseed.dto.stat.person;

import lombok.Data;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;

@Data
public class StatValuePersonInstanceDto {
    private int id;

    private int statValue;

    private String type;

    private int value;

    private Integer modifier;

    public StatValuePersonInstanceDto(Integer id, Integer statValue, String type, Integer value, Integer modifier) {
        this.id = id;
        this.statValue = statValue;
        this.type = type;
        this.value = value;
        this.modifier = modifier;
    }

    public static StatValuePersonInstanceDto fromInstance(StatValueInstance instance) {
        return new StatValuePersonInstanceDto(
                instance.getId(),
                instance.getStatValue().getId(),
                instance.getType(),
                instance.getValue(),
                instance.getModifier()
        );
    }
}
