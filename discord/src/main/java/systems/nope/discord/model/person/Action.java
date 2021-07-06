package systems.nope.discord.model.person;

public class Action {
    private final Integer id;
    private final String name;
    private final String description;
    private final String formula;

    public Action(Integer id, String name, String description, String formula) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.formula = formula;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFormula() {
        return formula;
    }
}
