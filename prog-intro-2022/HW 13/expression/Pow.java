package expression;

public class Pow extends UnaryOperation {

    public Pow(MyExpression operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.POW;
    }

    @Override
    public String toMiniString() {
        if (
                operand instanceof UnaryOperation ||
                operand.getClass() == Const.class ||
                operand.getClass() == Variable.class
        ) {
            return "pow10 " + operand.toMiniString();
        } else {
            return "pow10(" + operand.toMiniString() + ")";
        }
    }

    @Override
    protected int calculateOperation(int operandResult) {
        int result = 1;
        for (int i = 0; i < operandResult; i++) {
            result *= 10;
        }
        return result;
    }

    @Override
    protected double calculateOperation(double operandResult) {
        return 0;
    }
}
