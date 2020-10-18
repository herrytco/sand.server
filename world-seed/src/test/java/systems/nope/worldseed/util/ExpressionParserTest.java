package systems.nope.worldseed.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionParserTest {

    @Test
    public void testDice2() {
        for (int i = 0; i < 100; i++) {
            double result = ExpressionUtil.parseExpression("10d20");

            assertTrue(10 <= result && result <= 200);
        }
    }

    @Test
    public void testDice1() {
        for (int i = 0; i < 100; i++) {
            double result = ExpressionUtil.parseExpression("1d20");

            assertTrue(1 <= result && result <= 20);
        }
    }

    @Test
    public void testMin() {
        double result = ExpressionUtil.parseExpression("min ( 0 1 )");

        assertEquals(0, result);
    }

    @Test
    public void testMin2() {
        double result = ExpressionUtil.parseExpression("min ( 0 -1 )");

        assertEquals(-1, result);
    }

    @Test
    public void testMin3() {
        double result = ExpressionUtil.parseExpression("( min ( 0 -1 ) + 5 )");

        assertEquals(4, result);
    }

    @Test
    public void testFloor() {
        double result = ExpressionUtil.parseExpression("floor ( -0.1 )");

        assertEquals(-1, result);
    }

    @Test
    public void testMax() {
        double result = ExpressionUtil.parseExpression("max ( 0 1 )");

        assertEquals(1, result);
    }

    @Test
    public void testMax2() {
        double result = ExpressionUtil.parseExpression("max ( 0 -1 )");

        assertEquals(0, result);
    }

    @Test
    public void testMax3() {
        double result = ExpressionUtil.parseExpression("( max ( 0 -1 ) + 5 )");

        assertEquals(5, result);
    }

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
