package systems.nope.discord.model;

import systems.nope.discord.event.rolls.RollSpecification;

import java.util.List;
import java.util.stream.Collectors;

public class DiceResult {
    private final RollSpecification specification;
    private final List<Integer> result;
    private final Integer modifier;
    private final List<String> emoji;

    public DiceResult(RollSpecification specification, List<Integer> result, Integer modifier, List<String> emoji) {
        this.specification = specification;
        this.result = result;
        this.modifier = modifier;
        this.emoji = emoji;
    }

    public int getDiceType() {
        return specification.getDiceType();
    }

    public List<Integer> getResult() {
        return result;
    }

    public int getModifier() {
        return modifier;
    }

    public List<Integer> getEffectiveResult(int tempModifier) {
        return result.stream().map((i) -> i + modifier + tempModifier).collect(Collectors.toList());
    }

    public List<Integer> getEffectiveResult() {
        return result.stream().map((i) -> i + modifier).collect(Collectors.toList());
    }

    public List<String> getEmoji() {
        return emoji;
    }

    public RollSpecification getSpecification() {
        return specification;
    }
}
