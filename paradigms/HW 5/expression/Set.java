package expression;

public class Set extends BinaryOperation {
    public Set(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    public Operation getOperation() {
        return Operation.SET;
    }

    @Override
    protected int calculateOperation(int leftOperandResult, int rightOperandResult) {
        return leftOperandResult | (1 << rightOperandResult);
    }

    @Override
    protected double calculateOperation(double leftOperandResult, double rightOperandResult) {
        return 0;
    }
}
