package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.Member;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DiceUtils {
    private static final Map<Member, Integer> nTries = new HashMap<>();
    private static final Map<Member, Integer> sum = new HashMap<>();
    private static final Map<Member, Integer> diceTypes = new HashMap<>();
    private static final Map<Member, Integer> difficulties = new HashMap<>();
    private static final Map<Member, SecureRandom> rngs = new HashMap<>();

    public static void setDiceTypeForMember(Member member, int modifier) {
        diceTypes.put(member, modifier);
    }

    public static int getDiceModifierForMember(Member member) {
        return difficulties.getOrDefault(member, 0);
    }

    public static void setModifierForMember(Member member, int modifier) {
        difficulties.put(member, modifier);
    }

    /**
     * @param result - diceroll result (unmodified)
     * @return the correct dice emoji for the diceroll result
     */
    public static String getEmojiForResult(Member member, int result) {
        int best = diceTypes.getOrDefault(member, 20);

        if (result == 1)
            return ServerConstants.emoteD20Result1;

        if (result == best)
            return ServerConstants.emoteD20Result20;

        return ServerConstants.emoteD20Result2To19;
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
     * calculates 1 diceroll and adds the result to the statistics of the issuer
     *
     * @param member - issuer of the diceroll
     * @return - diceroll details
     */
    public static DiceResult rollOnce(Member member) {
        if (!rngs.containsKey(member))
            rngs.put(member, new SecureRandom());

        int diceType = diceTypes.getOrDefault(member, 20);
        int result = rngs
                .get(member)
                .nextInt(diceType) + 1;

        addResultToStatistics(member, result);

        return new DiceResult(
                diceType,
                result,
                difficulties.getOrDefault(member, 0),
                getEmojiForResult(member, result));
    }

    public static String getModifiedCalculation(DiceResult result, int modifier) {
        return getCalculation(result) + getDiceModificationString(result, modifier);
    }

    public static String getDiceModificationString(DiceResult result, int modifier) {
        return getDiceModificationString(result, modifier, result.getEffectiveResult() + modifier);
    }

    public static String getDiceModificationString(DiceResult result, int modifier, int calculationResult) {
        return String.format(
                ServerConstants.rollModification,
                modifier >= 0 ? "+" : "-",
                Math.abs(modifier),
                calculationResult
        );
    }


    /**
     * builds the string for a diceroll calculation.
     *
     * @return string in the form a + b = c
     */
    public static String getCalculation(DiceResult result) {
        if (result.getModifier() == 0)
            return String.format(
                    ServerConstants.singleResult,
                    result.getEmoji(),
                    result.getDiceType(),
                    result.getResult()
            );

        String sign = result.getModifier() < 0 ? "-" : "+";

        return String.format(
                ServerConstants.calculation,
                result.getEmoji(),
                result.getDiceType(),
                result.getResult(),
                sign,
                Math.abs(result.getModifier()),
                result.getEffectiveResult()
        );
    }

    public static String rollOnceString(Member member) {
        DiceResult result = rollOnce(member);

        return diceResultToString(member.getEffectiveName(), result);
    }

    public static String diceResultToString(String memberName, DiceResult result) {
        return String.format(ServerConstants.regularRoll, memberName, DiceUtils.getCalculation(result));
    }

    public static String modifiedDiceResultToString(String memberName, DiceResult result, int modifier) {
        return String.format(ServerConstants.regularRoll, memberName, DiceUtils.getCalculation(result));
    }

    /**
     * calculates the average roll score for the given member
     *
     * @param member - subject of the calculation
     * @return average roll score || -1 if no data is stored
     */
    public static float avgOfMember(Member member) {
        if (nTries.getOrDefault(member, 0) > 0)
            return (float) sum.get(member) / nTries.get(member);
        else
            return -1;
    }

    /**
     * @return the average roll for the whole server
     */
    public static float avgOfServer() {
        float avg = 0;
        int k = 0;

        for (Member u : nTries.keySet()) {
            avg += avgOfMember(u);
            k++;
        }

        if (k > 0)
            avg /= k;

        return avg;
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
}
