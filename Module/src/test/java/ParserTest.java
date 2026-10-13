import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ParserTest {

    private static NumNode num(double d) {
        return new NumNode(d);
    }

    private static void assertPostfixError(String input, String message) {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> Parser.parsePostfix(input));
        assertEquals(message, e.getMessage());
    }

    private static void assertInfixError(String input, String message) {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> Parser.parseInfix(input));
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testParserCanBeConstructed() {
        assertNotNull(new Parser());
    }

    @Test
    public void testPostfixSingleNumber() {
        assertEquals(num(7), Parser.parsePostfix("7"));
    }

    @Test
    public void testPostfixDecimalNumber() {
        assertEquals(num(3.5), Parser.parsePostfix("3.5"));
    }

    @Test
    public void testPostfixSimple() {
        assertEquals(new BinopNode("-", num(5), num(3)), Parser.parsePostfix("5 3 -"));
    }

    @Test
    public void testPostfixNested() {
        AST expected = new BinopNode("*", num(2), new BinopNode("+", num(3), num(4)));
        assertEquals(expected, Parser.parsePostfix("2 3 4 + *"));
    }

    @Test
    public void testPostfixAllOperators() {
        assertEquals(new BinopNode("+", num(1), num(2)), Parser.parsePostfix("1 2 +"));
        assertEquals(new BinopNode("*", num(1), num(2)), Parser.parsePostfix("1 2 *"));
        assertEquals(new BinopNode("/", num(1), num(2)), Parser.parsePostfix("1 2 /"));
        assertEquals(new BinopNode("^", num(1), num(2)), Parser.parsePostfix("1 2 ^"));
    }

    @Test
    public void testPostfixEmptyInput() {
        assertPostfixError("", "empty input");
    }

    @Test
    public void testPostfixInvalidToken() {
        assertPostfixError("2 a +", "invalid token");
    }

    @Test
    public void testPostfixInsufficientOperands() {
        assertPostfixError("2 +", "insufficient operands");
        assertPostfixError("+", "insufficient operands");
    }

    @Test
    public void testPostfixTooManyOperands() {
        assertPostfixError("2 3 4 +", "too many operands");
    }

    @Test
    public void testInfixSingleNumber() {
        assertEquals(num(7), Parser.parseInfix("7"));
    }

    @Test
    public void testInfixSimple() {
        assertEquals(new BinopNode("+", num(2), num(3)), Parser.parseInfix("2 + 3"));
    }

    @Test
    public void testInfixParentheses() {
        AST expected = new BinopNode("*", num(2), new BinopNode("+", num(3), num(4)));
        assertEquals(expected, Parser.parseInfix("2 * ( 3 + 4 )"));
    }

    @Test
    public void testInfixPrecedence() {
        AST expected = new BinopNode("+", num(2), new BinopNode("*", num(3), num(4)));
        assertEquals(expected, Parser.parseInfix("2 + 3 * 4"));
    }

    @Test
    public void testInfixLowerPrecedenceAfterHigher() {
        AST expected = new BinopNode("+", new BinopNode("*", num(2), num(3)), num(4));
        assertEquals(expected, Parser.parseInfix("2 * 3 + 4"));
    }

    @Test
    public void testInfixLeftAssociative() {
        AST expected = new BinopNode("-", new BinopNode("-", num(5), num(3)), num(1));
        assertEquals(expected, Parser.parseInfix("5 - 3 - 1"));
    }

    @Test
    public void testInfixExponentRightAssociative() {
        AST expected = new BinopNode("^", num(2), new BinopNode("^", num(3), num(2)));
        assertEquals(expected, Parser.parseInfix("2 ^ 3 ^ 2"));
    }

    @Test
    public void testInfixExponentHigherThanMultiply() {
        AST expected = new BinopNode("*", num(2), new BinopNode("^", num(4), num(3)));
        assertEquals(expected, Parser.parseInfix("2 * 4 ^ 3"));
    }

    @Test
    public void testInfixMultiplyAfterExponent() {
        AST expected = new BinopNode("*", new BinopNode("^", num(2), num(3)), num(4));
        assertEquals(expected, Parser.parseInfix("2 ^ 3 * 4"));
    }

    @Test
    public void testInfixNestedParentheses() {
        AST expected = new BinopNode("*",
                new BinopNode("+", num(1), num(2)),
                new BinopNode("-", num(3), num(4)));
        assertEquals(expected, Parser.parseInfix("( 1 + 2 ) * ( 3 - 4 )"));
    }

    @Test
    public void testInfixOperatorStopsAtOpenParen() {
        AST expected = new BinopNode("*", num(2), new BinopNode("+", num(3), num(4)));
        assertEquals(expected, Parser.parseInfix("2 * ( 3 + 4 )"));
        AST expected2 = new BinopNode("+", num(2), new BinopNode("*", num(3), num(4)));
        assertEquals(expected2, Parser.parseInfix("2 + ( 3 * 4 )"));
    }

    @Test
    public void testInfixEmptyInput() {
        assertInfixError("", "empty input");
    }

    @Test
    public void testInfixInvalidToken() {
        assertInfixError("2 + a", "invalid token");
    }

    @Test
    public void testInfixInsufficientOperands() {
        assertInfixError("2 +", "insufficient operands");
    }

    @Test
    public void testInfixInsufficientOperandsInsideParens() {
        assertInfixError("( 2 + )", "insufficient operands");
    }

    @Test
    public void testInfixTooManyOperands() {
        assertInfixError("2 + 3 4", "too many operands");
    }

    @Test
    public void testInfixMismatchedOpenParen() {
        assertInfixError("( 2 + 3", "mismatched open paren");
    }

    @Test
    public void testInfixMismatchedCloseParen() {
        assertInfixError("2 + 3 )", "mismatched close paren");
    }
}