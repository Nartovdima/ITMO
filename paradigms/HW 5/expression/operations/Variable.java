package expression.operations;

import expression.types.Operations;

public class Variable<T extends Number> implements MyExpression<T> {
    private final String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public T evaluate(T x, T y, T z, Operations<T> evaluator) {
        T result;
        switch (varName) {
            case "x" -> result = x;
            case "y" -> result = y;
            case "z" -> result = z;
            default -> throw new IllegalArgumentException("Invalid name of variable");
        }
        return result;
    }
}
