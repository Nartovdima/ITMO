package expression.operations;

import expression.types.Operations;

import java.util.Objects;

public abstract class BinaryOperation<T extends Number> implements MyExpression<T> {
    private final MyExpression<T> leftOperand;
    private final MyExpression<T> rightOperand;

    protected BinaryOperation (
            MyExpression<T> leftOperand,
            MyExpression<T> rightOperand
    ) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public abstract Operation getOperation();

    @Override
    public String toString() {
        return (
                "(" +
                leftOperand.toString() +
                " " +
                this.getOperation().getSymbol() +
                " " +
                rightOperand.toString() + ")"
        );
    }

    public T evaluate(T x, T y, T z, Operations<T> evaluator) {
        return calculateOperation(
                leftOperand.evaluate(x, y, z, evaluator),
                rightOperand.evaluate(x, y, z, evaluator),
                evaluator
        );
    }

    protected abstract T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList);
}
