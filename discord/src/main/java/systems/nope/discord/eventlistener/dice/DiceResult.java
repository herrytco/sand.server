package systems.nope.discord.eventlistener.dice;

public class DiceResult {
    private final int diceType;
    private final int result;
    private final int modifier;
    private final String emoji;

    public DiceResult(int diceType, int result, int modifier, String emoji) {
        this.diceType = diceType;
        this.result = result;
        this.modifier = modifier;
        this.emoji = emoji;
    }

    public int getDiceType() {
        return diceType;
    }

    public int getResult() {
        return result;
    }

    public int getModifier() {
        return modifier;
    }

    public int getEffectiveResult() {
        return result+modifier;
    }

    public String getEmoji() {
        return emoji;
    }
}
