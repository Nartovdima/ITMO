package expression.operations;

import expression.types.Operations;

public class Subtract<T extends Number> extends BinaryOperation<T> {
    public Subtract(MyExpression<T> leftOperand, MyExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.SUBTRACTION;
    }

    @Override
    protected T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList) {
        return operationsList.subtract(leftOperandResult, rightOperandResult);
    }


}
