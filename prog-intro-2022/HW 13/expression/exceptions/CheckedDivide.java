package expression.exceptions;

import expression.Divide;
import expression.MyExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public int calculateOperation(int leftOperandResult, int rightOperandResult) {
        if (rightOperandResult == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        if (leftOperandResult == Integer.MIN_VALUE && rightOperandResult == -1) {
            throw new OverflowException("overflow");
        }
        return leftOperandResult / rightOperandResult;
    }
}
