package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.Member;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.event.rolls.RollSpecification;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.constants.ServerConstants;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DiceUtils {
    private static final Map<Member, Integer> diceTypes = new HashMap<>();
    private static final Map<Member, Integer> difficulties = new HashMap<>();
    private static final Map<Member, SecureRandom> rngs = new HashMap<>();

    public static void setDiceTypeForMember(Member member, int type) {
        diceTypes.put(member, type);
    }

    public static int getDiceTypeForMember(Member member) {
        return diceTypes.getOrDefault(member, 20);
    }

    public static int getDiceModifierForMember(Member member) {
        return difficulties.getOrDefault(member, 0);
    }

    public static void setModifierForMember(Member member, int modifier) {
        difficulties.put(member, modifier);
    }


    public static DiceResult rollOnce(Member member, RollSpecification specification) {
        if (!rngs.containsKey(member))
            rngs.put(member, new SecureRandom());

        int diceType = specification.getDiceType();
        int result = 0;

        for (int i = 0; i < specification.getnRolls(); i++)
            result += rngs.get(member).nextInt(diceType) + 1;

        return null;
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

        RollSpecification specification = new RollSpecification(1, diceType);

        return null;
    }

    public static String getModifiedCalculation(DiceResult result, int modifier) {
        return getCalculation(result) + getDiceModificationString(result, modifier);
    }

    public static String getDiceModificationString(DiceResult result, int modifier) {
        return "";
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

        return diceResultToString(DiscordUtil.getMemberName(member), result);
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
        return -1;
    }

    /**
     * @return the average roll for the whole server
     */
    public static float avgOfServer() {
        float avg = 0;
        int k = 0;

//        for (Member u : nTries.keySet()) {
//            avg += avgOfMember(u);
//            k++;
//        }

        if (k > 0)
            avg /= k;

        return avg;
    }
}
