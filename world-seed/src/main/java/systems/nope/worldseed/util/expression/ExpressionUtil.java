package systems.nope.worldseed.util.expression;

import java.util.List;

public class ExpressionUtil {

    public static String groundFormula(String formula, List<Symbol> symbols) {
        for (Symbol symbol : symbols)
            formula = groundFormula(formula, symbol);

        return formula;
    }

    public static String groundFormula(String formula, Symbol symbol) {
        return formula.replaceAll(
                " " + symbol.getSymbolIdentifier() + " ",
                " " + String.valueOf(symbol.getValue()) + " "
        );
    }

    public static double parseExpression(String input) {
        ExpressionParser parser = new ExpressionParser(input);
        return parser.calculateResult();
    }
}
