package expression;

public class Count extends UnaryOperation {
    public Count(MyExpression operand) {
        super(operand);
    }

    @Override
    public Operation getOperation() {
        return Operation.COUNT;
    }

    @Override
    public String toMiniString() {
        if (
                operand.getClass() == Const.class ||
                operand.getClass() == Variable.class ||
                operand.getClass() == Negate.class ||
                operand.getClass() == Count.class
        ) {
            return "count " + operand.toMiniString();
        } else {
            return "count(" + operand.toMiniString() + ")";
        }
    }

    @Override
    protected int calculateOperation(int operandResult) {
        return Integer.bitCount(operandResult);
    }

    @Override
    protected double calculateOperation(double operandResult) {
        return 0;
    }
}
