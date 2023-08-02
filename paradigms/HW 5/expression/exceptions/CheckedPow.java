package expression.exceptions;

import expression.MyExpression;
import expression.Pow;

public class CheckedPow extends Pow {
    public CheckedPow(MyExpression operand) {
        super(operand);
    }

    @Override
    public int calculateOperation(int operandResult) {
        if (operandResult < 0) {
            throw new UnsupportedArgumentException("argument is lower 0");
        }
        int result = 1;
        for (int i = 0; i < operandResult; i++) {
            if (result * 10 / 10 != result) {
                throw new OverflowException("overflow");
            }
            result *= 10;
        }
        return result;
    }
}
