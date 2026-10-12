public class Parser {

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-")
                || token.equals("*") || token.equals("/")
                || token.equals("^");
    }

    private static Double tryParseNumber(String token) {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void applyOperator(ArrayStack<AST> stack, String op) {
        if (stack.size() < 2) {
            throw new IllegalArgumentException("insufficient operands");
        }
        AST right = stack.pop();
        AST left = stack.pop();
        stack.push(new BinopNode(op, left, right));
    }

    public static AST parsePostfix(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }
        ArrayStack<AST> stack = ArrayStack.emptyStack();
        String[] tokens = input.split("\\s+");
        for (String token : tokens) {
            Double number = tryParseNumber(token);
            if (number != null) {
                stack.push(new NumNode(number));
            } else if (isOperator(token)) {
                applyOperator(stack, token);
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }
        if (stack.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }
        return stack.pop();
    }
}