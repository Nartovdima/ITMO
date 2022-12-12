package expression;
public class Multiply extends BinaryOperation {
    public Multiply(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.MULTIPLICATION;
    }

    @Override
    protected double calculateOperation(double leftOperandResult, double rightOperandResult) {
        return leftOperandResult * rightOperandResult;
    }

    @Override
    protected int calculateOperation(int leftOperandResult, int rightOperandResult) {
        return leftOperandResult * rightOperandResult;
    }
}
