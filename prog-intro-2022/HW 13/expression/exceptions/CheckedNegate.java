package expression.exceptions;

import expression.MyExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(MyExpression operand) {
        super(operand);
    }

    @Override
    public int calculateOperation(int operandResult) {
        if (operandResult == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return -operandResult;
    }

}
