package expression.operations;

import expression.types.Operations;

public class Multiply<T extends Number> extends BinaryOperation<T> {
    public Multiply(MyExpression<T> leftOperand, MyExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.MULTIPLICATION;
    }

    @Override
    protected T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList) {
        return operationsList.multiply(leftOperandResult, rightOperandResult);
    }


}
