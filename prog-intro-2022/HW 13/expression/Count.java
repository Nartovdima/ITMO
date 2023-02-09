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
        return super.toMiniString("count");
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
