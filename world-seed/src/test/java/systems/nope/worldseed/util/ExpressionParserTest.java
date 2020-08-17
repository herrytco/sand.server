package systems.nope.worldseed.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionParserTest {

    @Test
    public void testSimpleExpression1() {
        double result = ExpressionUtil.parseExpression("( 1 + 1 )");

        assertEquals(2, result);
    }

    @Test
    public void testSimpleExpression2() {
        double result = ExpressionUtil.parseExpression("( 1 - 1 )");

        assertEquals(0, result);
    }

    @Test
    public void testSimpleExpression3() {
        double result = ExpressionUtil.parseExpression("( 1 * 1 )");

        assertEquals(1, result);
    }

    @Test
    public void testSimpleExpression4() {
        double result = ExpressionUtil.parseExpression("( 1 / 1 )");

        assertEquals(1, result);
    }

    @Test
    public void testSimpleExpression5() {
        double result = ExpressionUtil.parseExpression("sqrt ( 4 )");

        assertEquals(2, result);
    }

    @Test
    public void testComplexExpression1() {
        double result = ExpressionUtil.parseExpression("( ( 1 + sqrt ( 5.0 ) ) / 2.0 )");

        assertEquals("1.61803", String.valueOf(result).substring(0,7));
    }

}
