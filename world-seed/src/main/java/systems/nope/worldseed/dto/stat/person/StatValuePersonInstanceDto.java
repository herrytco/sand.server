package systems.nope.worldseed.dto.stat.person;

import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;

public class StatValuePersonInstanceDto {
    private final int id;

    private final int statValue;

    private final int person;

    private final String type;

    private final int value;

    public StatValuePersonInstanceDto(Integer id, Integer statValue, Integer person, String type, Integer value) {
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

    public static StatValuePersonInstanceDto fromInstance(StatValuePersonInstance instance) {
        return new StatValuePersonInstanceDto(
                instance.getId(),
                instance.getStatValue().getId(),
                instance.getPerson().getId(),
                instance.getType(),
                instance.getValue()
        );
    }
}
