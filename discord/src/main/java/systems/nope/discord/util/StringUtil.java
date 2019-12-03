package systems.nope.discord.util;

public class StringUtil {
    public static String integerToOrderedString(int k) {
        switch (k) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            default:
                return String.format("%sth", k);
        }
    }
}
