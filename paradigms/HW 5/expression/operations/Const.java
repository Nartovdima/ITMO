package expression.operations;

import expression.types.Operations;

public class Const<T extends Number> implements MyExpression<T> {
    private final T value;

    public Const (T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public T evaluate(T x, T y, T z, Operations<T> evaluator) {
        return value;
    }
}
