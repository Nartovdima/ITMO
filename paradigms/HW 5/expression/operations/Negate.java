package expression.operations;

import expression.types.Operations;

public class Negate<T extends Number> extends UnaryOperation<T> {
    public Negate(MyExpression<T> operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.NEGATE;
    }

    @Override
    protected T calculateOperation(T operandResult, Operations<T> operationsList) {
        return operationsList.negate(operandResult);
    }
}
