package expression.exceptions;

import expression.Add;
import expression.MyExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public int calculateOperation(int leftOperandResult, int rightOperandResult) {
        if (
                rightOperandResult > 0 && Integer.MAX_VALUE - rightOperandResult < leftOperandResult ||
                rightOperandResult < 0 && Integer.MIN_VALUE - rightOperandResult > leftOperandResult
        ) {
            throw new OverflowException("overflow");
        }
        return leftOperandResult + rightOperandResult;
    }
}
