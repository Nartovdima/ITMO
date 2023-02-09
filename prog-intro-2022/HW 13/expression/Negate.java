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
        return super.toMiniString("-");
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
