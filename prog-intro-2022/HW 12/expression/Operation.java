package expression;
public enum Operation {
    ADDITION(2, "+"),
    SUBTRACTION(2, "-"),
    MULTIPLICATION(1, "*"),
    DIVISION(1, "/"),
    SET(3, "set"),
    CLEAR(3, "clear"),
    COUNT(0, "count"),
    NEGATE(0, "-");
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
