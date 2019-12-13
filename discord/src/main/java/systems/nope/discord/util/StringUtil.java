package systems.nope.discord.util;

public class StringUtil {
    public static String integerToOrderedString(int k) {
        switch (k) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return String.format("%sth", k);
        }
    }
}
