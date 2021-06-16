package systems.nope.discord.event.rolls;

import systems.nope.discord.exceptions.ParseException;

public final class RollSpecification {
    private final Integer nRolls;
    private final Integer diceType;

    public RollSpecification() {
        this.nRolls = 1;
        this.diceType = 20;
    }

    public RollSpecification(Integer nRolls) {
        this.nRolls = nRolls;
        this.diceType = 20;
    }

    public RollSpecification(Integer nRolls, Integer diceType) {
        this.nRolls = nRolls;
        this.diceType = diceType;
    }

    public Integer getnRolls() {
        return nRolls;
    }

    public Integer getDiceType() {
        return diceType;
    }

    public static RollSpecification fromString(String roll) throws ParseException {
        if (roll.matches("\\d*d\\d+")) {
            String[] parts = roll.split("d");

            int diceType, nRolls;

            if (parts.length == 2) {
                nRolls = parts[0].length() == 0 ? 1 : Integer.parseInt(parts[0]);
                diceType = Integer.parseInt(parts[1]);
            } else if (parts.length == 1) {
                nRolls = 1;
                diceType = Integer.parseInt(parts[1]);
            } else {
                nRolls = 1;
                diceType = 20;
            }

            return new RollSpecification(nRolls, diceType);
        }

        throw new ParseException("'%s' is no valid dice-specification sir! try it with something like 1d20 or 4d6!");
    }

    @Override
    public String toString() {
        return String.format("%dd%d", nRolls, diceType);
    }
}
