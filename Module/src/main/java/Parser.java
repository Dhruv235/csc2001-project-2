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

    private static int precedence(String op) {
        return switch (op) {
            case "^" -> 3;
            case "*", "/" -> 2;
            default -> 1;
        };
    }

    private static boolean shouldApplyFirst(String onStack, String incoming) {
        if (!isOperator(onStack)) {
            return false;
        }
        int stackPrec = precedence(onStack);
        int incomingPrec = precedence(incoming);
        return stackPrec > incomingPrec
                || (stackPrec == incomingPrec && !incoming.equals("^"));
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

    public static AST parseInfix(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }
        ArrayStack<AST> expressions = ArrayStack.emptyStack();
        ArrayStack<String> operators = ArrayStack.emptyStack();
        String[] tokens = input.split("\\s+");
        for (String token : tokens) {
            Double number = tryParseNumber(token);
            if (number != null) {
                expressions.push(new NumNode(number));
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    applyOperator(expressions, operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("mismatched close paren");
                }
                operators.pop();
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && shouldApplyFirst(operators.peek(), token)) {
                    applyOperator(expressions, operators.pop());
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(")) {
                throw new IllegalArgumentException("mismatched open paren");
            }
            applyOperator(expressions, op);
        }
        if (expressions.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }
        return expressions.pop();
    }
}