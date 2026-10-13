import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ArrayStackTest {

    @Test
    public void testEmptyStack() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void testPushMakesStackNonEmpty() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        stack.push(1);
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
    }

    @Test
    public void testPeekDoesNotRemove() {
        ArrayStack<String> stack = ArrayStack.emptyStack();
        stack.push("a");
        stack.push("b");
        assertEquals("b", stack.peek());
        assertEquals("b", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    public void testPopRemovesInLifoOrder() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.size());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testPopEmptyThrows() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        assertThrows(IndexOutOfBoundsException.class, () -> stack.pop());
    }

    @Test
    public void testPeekEmptyThrows() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        assertThrows(IndexOutOfBoundsException.class, () -> stack.peek());
    }

    @Test
    public void testPopUntilEmptyThenThrows() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        stack.push(1);
        stack.pop();
        assertThrows(IndexOutOfBoundsException.class, () -> stack.pop());
        assertThrows(IndexOutOfBoundsException.class, () -> stack.peek());
    }

    @Test
    public void testGrowsPastInitialCapacity() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();
        for (int i = 0; i < 1000; i++) {
            stack.push(i);
        }
        assertEquals(1000, stack.size());
        for (int i = 999; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testDifferentTypesAndNulls() {
        ArrayStack<Double> doubles = ArrayStack.emptyStack();
        doubles.push(1.5);
        assertEquals(1.5, doubles.peek());

        ArrayStack<String> strings = ArrayStack.emptyStack();
        strings.push(null);
        assertNull(strings.peek());
        assertEquals(1, strings.size());
    }

    @Test
    public void testStacksAreIndependent() {
        ArrayStack<Integer> a = ArrayStack.emptyStack();
        ArrayStack<Integer> b = ArrayStack.emptyStack();
        a.push(1);
        assertTrue(b.isEmpty());
    }
}