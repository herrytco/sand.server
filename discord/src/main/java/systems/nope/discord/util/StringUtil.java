package systems.nope.discord.util;

import java.util.Optional;

public class StringUtil {

    public static Optional<Integer> parseInt(String numberString) {
        try {
            return Optional.of(Integer.parseInt(numberString));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }


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
