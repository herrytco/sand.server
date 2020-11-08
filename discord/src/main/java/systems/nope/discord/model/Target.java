package systems.nope.discord.model;

public class Target {
    private final String name;
    private final int numberOfRolls;

    public Target(String name, int numberOfRolls) {
        this.name = name;
        this.numberOfRolls = numberOfRolls;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfRolls() {
        return numberOfRolls;
    }
}
