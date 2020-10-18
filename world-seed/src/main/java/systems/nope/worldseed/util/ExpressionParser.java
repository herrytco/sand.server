package systems.nope.worldseed.util;

import systems.nope.worldseed.constants.ExpressionConstants;

import java.security.SecureRandom;
import java.util.Stack;

public class ExpressionParser {

    private final String expression;

    private static final SecureRandom rng = new SecureRandom();

    public ExpressionParser(String expression) {
        this.expression = expression;
    }

    public double calculateResult() {
        Stack<String> operators = new Stack<>();
        Stack<Double> values = new Stack<>();

        String[] tokens = expression.split(ExpressionConstants.delimiter);

        for (String token : tokens) {
            switch (token) {
                case ExpressionConstants.startGroup:
                    break;
                case ExpressionConstants.opAdd:
                case ExpressionConstants.opDiv:
                case ExpressionConstants.opMul:
                case ExpressionConstants.opSub:
                case ExpressionConstants.opSqrt:
                case ExpressionConstants.opCeil:
                case ExpressionConstants.opFloor:
                case ExpressionConstants.opMin:
                case ExpressionConstants.opMax:
                    operators.add(token);
                    break;

                case ExpressionConstants.endGroup:
                    String op = operators.pop();
                    Double v1 = values.pop();
                    double result;

                    switch (op) {
                        case ExpressionConstants.opMin:
                            result = Math.min(values.pop(), v1);
                            break;
                        case ExpressionConstants.opMax:
                            result = Math.max(values.pop(), v1);
                            break;
                        case ExpressionConstants.opAdd:
                            result = values.pop() + v1;
                            break;
                        case ExpressionConstants.opSub:
                            result = values.pop() - v1;
                            break;
                        case ExpressionConstants.opMul:
                            result = values.pop() * v1;
                            break;
                        case ExpressionConstants.opDiv:
                            result = values.pop() / v1;
                            break;
                        case ExpressionConstants.opSqrt:
                            result = Math.sqrt(v1);
                            break;
                        case ExpressionConstants.opCeil:
                            result = Math.ceil(v1);
                            break;
                        case ExpressionConstants.opFloor:
                            result = Math.floor(v1);
                            break;

                        default:
                            throw new IllegalArgumentException(String.format("'%s' is no valid operation!", op));
                    }

                    values.push(result);
                    break;

                default:
                    try {
                        double operand;

                        if (token.matches(ExpressionConstants.argDice)) {
                            String[] args = token.split(ExpressionConstants.diceArgDelimiter);

                            int nDice = Integer.parseInt(args[0]);
                            int diceType = Integer.parseInt(args[1]);

                            operand = 0d;

                            for (int i = 0; i < nDice; i++)
                                operand += rng.nextInt(diceType) + 1;
                        } else {
                            operand = Double.parseDouble(token);
                        }

                        values.push(operand);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format("Could not parse '%s' to a Double/Integer.", token));
                    }

                    break;
            }
        }

        if (values.size() != 1)
            throw new IllegalStateException(String.format("Something went wrong during parsing of the expression '%s', Stacksize=%d", expression, values.size()));

        return values.pop();
    }
}
