package expression.exceptions;

import expression.Log;
import expression.MyExpression;

public class CheckedLog extends Log {
    public CheckedLog(MyExpression operand) {
        super(operand);
    }

    @Override
    public int calculateOperation(int operandResult) {
        if (operandResult < 1) {
            throw new UnsupportedArgumentException("argument is lower than 1");
        }
        int result = 0;
        while (operandResult >= 10) {
            result++;
            operandResult /= 10;
        }
        return result;
    }
}
