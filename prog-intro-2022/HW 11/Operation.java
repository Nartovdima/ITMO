package expression;
public enum Operation {
    ADDITION(1, "+"),
    SUBTRACTION(1, "-"),
    MULTIPLICATION(0, "*"),
    DIVISION(0, "/");

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
