package expression.operations;

import expression.types.Operations;

public class Divide<T extends Number> extends BinaryOperation<T> {
    public Divide(MyExpression<T> leftOperand, MyExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.DIVISION;
    }

    @Override
    protected T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList) {
        return operationsList.divide(leftOperandResult, rightOperandResult);
    }


}
