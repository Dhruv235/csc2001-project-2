import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ASTTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testNumNodeEval() {
        assertEquals(3.5, new NumNode(3.5).eval(), DELTA);
    }

    @Test
    public void testAdd() {
        assertEquals(5.0, new BinopNode("+", new NumNode(2), new NumNode(3)).eval(), DELTA);
    }

    @Test
    public void testSubtract() {
        assertEquals(-1.0, new BinopNode("-", new NumNode(2), new NumNode(3)).eval(), DELTA);
    }

    @Test
    public void testMultiply() {
        assertEquals(6.0, new BinopNode("*", new NumNode(2), new NumNode(3)).eval(), DELTA);
    }

    @Test
    public void testDivide() {
        assertEquals(1.5, new BinopNode("/", new NumNode(3), new NumNode(2)).eval(), DELTA);
    }

    @Test
    public void testExponent() {
        assertEquals(512.0, new BinopNode("^", new NumNode(2), new NumNode(9)).eval(), DELTA);
    }

    @Test
    public void testNestedTree() {
        AST tree = new BinopNode("*",
                new NumNode(2),
                new BinopNode("+", new NumNode(3), new NumNode(4)));
        assertEquals(14.0, tree.eval(), DELTA);
    }

    @Test
    public void testUnknownOperatorThrows() {
        AST tree = new BinopNode("%", new NumNode(2), new NumNode(3));
        assertThrows(IllegalStateException.class, () -> tree.eval());
    }

    @Test
    public void testParseInfixThenEval() {
        assertEquals(14.0, Parser.parseInfix("2 * ( 3 + 4 )").eval(), DELTA);
    }

    @Test
    public void testParsePostfixThenEval() {
        assertEquals(14.0, Parser.parsePostfix("2 3 4 + *").eval(), DELTA);
    }

    @Test
    public void testExponentIsRightAssociative() {
        assertEquals(512.0, Parser.parseInfix("2 ^ 3 ^ 2").eval(), DELTA);
    }

    @Test
    public void testExponentBindsTighterThanMultiply() {
        assertEquals(128.0, Parser.parseInfix("2 * 4 ^ 3").eval(), DELTA);
    }

    @Test
    public void testLeftAssociativeOperators() {
        assertEquals(1.5, Parser.parseInfix("2 * 3 / 4").eval(), DELTA);
        assertEquals(1.0, Parser.parseInfix("5 - 3 - 1").eval(), DELTA);
    }
}