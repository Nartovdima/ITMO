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
        return super.toMiniString("pow10");
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
