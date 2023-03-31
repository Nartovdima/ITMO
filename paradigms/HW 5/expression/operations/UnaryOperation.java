package expression.operations;

import expression.types.Operations;

public abstract class UnaryOperation<T extends Number> implements MyExpression<T> {
    protected final MyExpression<T> operand;
    public UnaryOperation(MyExpression<T> operand) {
        this.operand = operand;
    }

    public MyExpression<T> getOperand() {
        return operand;
    }

    public abstract Operation getOperation();

    @Override
    public String toString() {
        return this.getOperation().getSymbol() + "(" + operand.toString() + ")";
    }

    @Override
    public T evaluate(T x, T y, T z, Operations<T> evaluator) {
        return calculateOperation(operand.evaluate(x, y, z, evaluator), evaluator);
    }

    protected abstract T calculateOperation(T operandResult, Operations<T> operationsList);

}
