package expression.operations;

import expression.types.Operations;

public class MyMod<T extends Number> extends BinaryOperation<T> {
    public MyMod(MyExpression<T> leftOperand, MyExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.MOD;
    }

    @Override
    protected T calculateOperation(T leftOperandResult, T rightOperandResult, Operations<T> operationsList) {
        return operationsList.mod(leftOperandResult, rightOperandResult);
    }


}
