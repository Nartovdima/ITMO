package expression.exceptions;

import expression.Multiply;
import expression.MyExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public int calculateOperation(int leftOperandResult, int rightOperandResult) {
        if (
                leftOperandResult == Integer.MIN_VALUE && rightOperandResult == -1 ||
                rightOperandResult != 0 &&
                leftOperandResult * rightOperandResult / rightOperandResult != leftOperandResult
        ) {
            throw new OverflowException("overflow");
        }
        return leftOperandResult * rightOperandResult;
    }
}
