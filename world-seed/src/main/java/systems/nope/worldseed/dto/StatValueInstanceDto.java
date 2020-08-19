package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.stat.instance.StatValueInstance;

public class StatValueInstanceDto {
    private final int id;

    private final int statValue;

    private final int person;

    private final String type;

    private final int value;

    public StatValueInstanceDto(Integer id, Integer statValue, Integer person, String type, Integer value) {
        this.id = id;
        this.statValue = statValue;
        this.person = person;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getStatValue() {
        return statValue;
    }

    public int getPerson() {
        return person;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static StatValueInstanceDto fromInstance(StatValueInstance instance) {
        return new StatValueInstanceDto(
                instance.getId(),
                instance.getStatValue().getId(),
                instance.getPerson().getId(),
                instance.getType(),
                instance.getValue()
        );
    }
}
