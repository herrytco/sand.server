package systems.nope.worldseed.dto.stat.person;

import systems.nope.worldseed.model.stat.instance.StatValueInstance;

public class StatValuePersonInstanceDto {
    private final int id;

    private final int statValue;

    private final String type;

    private final int value;

    public StatValuePersonInstanceDto(Integer id, Integer statValue, String type, Integer value) {
        this.id = id;
        this.statValue = statValue;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getStatValue() {
        return statValue;
    }


    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static StatValuePersonInstanceDto fromInstance(StatValueInstance instance) {
        return new StatValuePersonInstanceDto(
                instance.getId(),
                instance.getStatValue().getId(),
                instance.getType(),
                instance.getValue()
        );
    }
}
