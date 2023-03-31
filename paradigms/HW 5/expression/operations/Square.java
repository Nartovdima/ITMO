package expression.operations;

import expression.types.*;
public class Square<T extends Number> extends UnaryOperation<T> {
    public Square(MyExpression<T> operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.SQUARE;
    }

    @Override
    protected T calculateOperation(T operandResult, Operations<T> operationsList) {
        return operationsList.square(operandResult);
    }


}
