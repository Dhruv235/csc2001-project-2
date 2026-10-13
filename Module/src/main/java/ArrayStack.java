import java.util.Arrays;

public class ArrayStack<T> {
    private static final int INITIAL_CAPACITY = 10;

    private Object[] array;
    private int size;

    private ArrayStack() {
        this.array = new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    public static <T> ArrayStack<T> emptyStack() {
        return new ArrayStack<>();
    }

    public void push(T value) {
        if (this.size == this.array.length) {
            this.array = Arrays.copyOf(this.array, this.array.length * 2);
        }
        this.array[this.size] = value;
        this.size++;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (this.isEmpty()) {
            throw new IndexOutOfBoundsException("pop from empty stack");
        }
        this.size--;
        T result = (T) this.array[this.size];
        this.array[this.size] = null;
        return result;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (this.isEmpty()) {
            throw new IndexOutOfBoundsException("peek at empty stack");
        }
        return (T) this.array[this.size - 1];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }
}