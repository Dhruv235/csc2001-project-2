public record NumNode(double value) implements AST {
    @Override
    public double eval() {
        return this.value;
    }
}