package expression.operations;

import expression.types.Operations;

public class Add<T extends Number> extends BinaryOperation<T> {
    public Add(MyExpression<T> leftOperand, MyExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.ADDITION;
    }

    @Override
    protected T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList) {
        return operationsList.add(leftOperandResult, rightOperandResult);
    }


}
