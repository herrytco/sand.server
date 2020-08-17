package systems.nope.worldseed.util;

public class ExpressionUtil {
    public static double parseExpression(String input) {
        ExpressionParser parser = new ExpressionParser(input);
        return parser.calculateResult();
    }
}
