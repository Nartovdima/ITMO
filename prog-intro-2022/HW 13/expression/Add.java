package expression;
public class Add extends BinaryOperation {
    public Add(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.ADDITION;
    }

    @Override
    protected double calculateOperation(double leftOperandResult, double rightOperandResult) {
        return leftOperandResult + rightOperandResult;
    }

    @Override
    protected int calculateOperation(int leftOperandResult, int rightOperandResult) {
        return leftOperandResult + rightOperandResult;
    }
}
