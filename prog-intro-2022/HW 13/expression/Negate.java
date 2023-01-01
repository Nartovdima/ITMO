package expression;

public class Negate extends UnaryOperation {
    public Negate(MyExpression operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.NEGATE;
    }

    @Override
    public String toMiniString() {
        if (
                operand instanceof UnaryOperation ||
                operand.getClass() == Const.class ||
                operand.getClass() == Variable.class
        ) {
            return "- " + operand.toMiniString();
        } else {
            return "-(" + operand.toMiniString() + ")";
        }
    }

    @Override
    protected int calculateOperation(int operandResult) {
        return -operandResult;
    }

    @Override
    protected double calculateOperation(double operandResult) {
        return -operandResult;
    }
}
