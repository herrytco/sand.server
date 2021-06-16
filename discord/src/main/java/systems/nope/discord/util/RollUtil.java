package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.Member;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.event.rolls.RollSpecification;
import systems.nope.discord.model.DiceResult;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RollUtil {

    private static final Map<Member, Integer> nTries = new HashMap<>();
    private static final Map<Member, Integer> sum = new HashMap<>();

    public static DiceResult roll(Member member, SecureRandom rng) {
        return roll(member, rng, 1);
    }

    public static DiceResult roll(Member member, SecureRandom rng, int nRolls) {
        return roll(member, rng, nRolls, 20);
    }

    public static DiceResult roll(Member member, SecureRandom rng, int nRolls, int diceType) {
        return roll(member, rng, nRolls, diceType, 0);
    }

    public static DiceResult roll(Member member, SecureRandom rng, int nRolls, int diceType, int modifier) {
        RollSpecification spec = new RollSpecification(nRolls, diceType);

        List<Integer> results = new LinkedList<>();
        List<String> emojis = new LinkedList<>();

        for (int i = 0; i < spec.getnRolls(); i++) {
            Integer ri = rng.nextInt(spec.getDiceType()) + 1;
            String emoji = getEmojiForResult(ri, spec.getDiceType());

            addResultToStatistics(member, ri);

            results.add(ri);
            emojis.add(emoji);
        }

        return new DiceResult(spec, results, modifier, emojis);
    }

    /**
     * sets the tries and sum of the given member to 0
     *
     * @param member - issuer of the command
     */
    public static void resetAvgOfMember(Member member) {
        nTries.put(member, 0);
        sum.put(member, 0);
    }

    /**
     * clears all data in sums and ntries to reset the
     * average calculation
     */
    public static void resetAvgOfSever() {
        nTries.clear();
        sum.clear();
    }

    public static float getServerAverage() {
        int s = 0, n = 0;

        for(Member user : sum.keySet()) {
            s += sum.getOrDefault(user, 0);
            n += nTries.getOrDefault(user, 0);
        }

        if(s > 0 && n > 0)
            return (float) s / n;
        else
            return -1;
    }

    public static float getUserAverage(Member member) {
        int s = sum.getOrDefault(member, 0);
        int n = nTries.getOrDefault(member, 0);

        if(s > 0 && n > 0)
            return (float) s / n;
        else
            return -1;
    }

    /**
     * adds the unmodified result to the statistics of the member
     *
     * @param member - issuer of the diceroll
     * @param result - unmodified result
     */
    private static void addResultToStatistics(Member member, int result) {
        sum.put(member, sum.getOrDefault(member, 0) + result);
        nTries.put(member, nTries.getOrDefault(member, 0) + 1);
    }

    /**
     * @param result - diceroll result (unmodified)
     * @return the correct dice emoji for the diceroll result
     */
    public static String getEmojiForResult(Integer result, Integer diceType) {
        if (result == 1)
            return EmoteConstants.emoteD20Result1;

        if (result.equals(diceType))
            return EmoteConstants.emoteD20Result20;

        return EmoteConstants.emoteD20Result2To19;
    }

    public static String rollString(String emoji, int diceType, int result, int permanentModifier, int temporaryModifier) {
        return rollString("", emoji, diceType, result, permanentModifier, temporaryModifier);
    }

    public static String rollString(String prefix, String emoji, int diceType, int result, int permanentModifier, int temporaryModifier) {
        String r;

        if (permanentModifier == 0 && temporaryModifier == 0)
            r = String.format("%s %s(%dd%d): %d",
                    prefix,
                    emoji,
                    1,
                    diceType,
                    result + permanentModifier + temporaryModifier
            );
        else if(temporaryModifier == 0)
            r = String.format("%s %s(%dd%d): %s",
                    prefix,
                    emoji,
                    1,
                    diceType,
                    calculationString(result, permanentModifier)
            );
        else if(permanentModifier == 0) {
            r = String.format("%s %s(%dd%d): %s",
                    prefix,
                    emoji,
                    1,
                    diceType,
                    calculationString(result, temporaryModifier)
            );
        }
        else {
            r = String.format("%s %s(%dd%d): %s",
                    prefix,
                    emoji,
                    1,
                    diceType,
                    RollUtil.modifiedCalculationString(result, permanentModifier, temporaryModifier)
            );
        }

        if(prefix.length() == 0)
            r = r.substring(1);

        return r;
    }

    private static String modifiedCalculationString(int result, int permanentModifier, int temporaryModifier) {
        return String.format("(%s) %s %d = %d",
                calculationString(result, permanentModifier),
                temporaryModifier < 0 ? "-" : "+",
                Math.abs(temporaryModifier),
                result + permanentModifier + temporaryModifier
        );
    }

    private static String calculationString(int result, int modifier) {
        return String.format("%d %s %d = %d",
                result,
                modifier < 0 ? "-" : "+",
                Math.abs(modifier),
                result + modifier
        );
    }


}
