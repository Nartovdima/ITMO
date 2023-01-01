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
                operand.getClass() == Const.class ||
                operand.getClass() == Variable.class ||
                operand.getClass() == Negate.class ||
                operand.getClass() == Count.class
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
