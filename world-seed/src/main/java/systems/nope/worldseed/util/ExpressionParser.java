package systems.nope.worldseed.util;

import java.util.Stack;

public class ExpressionParser {

    private final String expression;

    public ExpressionParser(String expression) {
        this.expression = expression;
    }

    public double calculateResult() {
        Stack<String> operators = new Stack<>();
        Stack<Double> values = new Stack<>();

        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            switch (token) {
                case "(":
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "sqrt":
                    operators.add(token);
                    break;

                case ")":
                    String op = operators.pop();
                    Double v1 = values.pop();
                    double result;

                    switch (op) {
                        case "+":
                            result = values.pop() + v1;
                            break;
                        case "-":
                            result = values.pop() - v1;
                            break;
                        case "*":
                            result = values.pop() * v1;
                            break;
                        case "/":
                            result = values.pop() / v1;
                            break;
                        case "sqrt":
                            result = Math.sqrt(v1);
                            break;

                        default:
                            throw new IllegalArgumentException(String.format("'%s' is no valid operation!", op));
                    }

                    values.push(result);
                    break;

                default:
                    Double operand = Double.parseDouble(token);
                    values.push(operand);
                    break;
            }
        }

        if (values.size() != 1)
            throw new IllegalStateException(String.format("Something went wrong during parsing of the expression '%s', Stacksize=%d", expression, values.size()));

        return values.pop();
    }
}
