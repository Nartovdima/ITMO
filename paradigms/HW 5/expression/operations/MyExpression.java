package expression.operations;

import expression.types.Operations;

public interface MyExpression<T extends Number> {
    T evaluate(T x, T y, T z, Operations<T> evaluator);
}
