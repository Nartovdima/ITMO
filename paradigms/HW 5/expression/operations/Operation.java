package expression.operations;
public enum Operation {
    ADDITION(2, "+"),
    SUBTRACTION(2, "-"),
    MULTIPLICATION(1, "*"),
    DIVISION(1, "/"),
    NEGATE(0, "-"),
    ABS(0, "abs"),
    SQUARE(0, "square"),
    MOD(1, "mod");
    private final int priority;
    private final String symbol;

    Operation(int priority, String symbol) {
        this.priority = priority;
        this.symbol = symbol;
    }

    public int getPriority() {
        return priority;
    }

    public String getSymbol() {
        return symbol;
    }
}
