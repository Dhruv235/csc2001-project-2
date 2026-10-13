public record BinopNode(String op, AST left, AST right) implements AST {
    @Override
    public double eval() {
        double l = this.left.eval();
        double r = this.right.eval();
        return switch (this.op) {
            case "+" -> l + r;
            case "-" -> l - r;
            case "*" -> l * r;
            case "/" -> l / r;
            case "^" -> Math.pow(l, r);
            default -> throw new IllegalStateException("unknown operator");
        };
    }
}