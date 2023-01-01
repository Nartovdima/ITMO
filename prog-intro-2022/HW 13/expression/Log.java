package expression;

public class Log extends UnaryOperation {

    public Log(MyExpression operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.LOG;
    }

    @Override
    public String toMiniString() {
        if (
                operand instanceof UnaryOperation ||
                operand.getClass() == Const.class ||
                operand.getClass() == Variable.class
        ) {
            return "log10 " + operand.toMiniString();
        } else {
            return "log10(" + operand.toMiniString() + ")";
        }
    }

    @Override
    protected int calculateOperation(int operandResult) {
        int result = 0;
        while (operandResult >= 10) {
            result++;
            operandResult /= 10;
        }
        return result;
    }

    @Override
    protected double calculateOperation(double operandResult) {
        return 0;
    }
}
