package expression.operations;

import expression.types.Operations;

public class Abs<T extends Number> extends UnaryOperation<T> {
    public Abs(MyExpression<T> operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.ABS;
    }

    @Override
    protected T calculateOperation(T operandResult, Operations<T> operationsList) {
        return operationsList.abs(operandResult);
    }
}
