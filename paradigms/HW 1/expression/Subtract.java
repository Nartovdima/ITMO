package expression;
public class Subtract extends BinaryOperation {
    public Subtract(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.SUBTRACTION;
    }

    @Override
    protected double calculateOperation(double leftOperandResult, double rightOperandResult) {
        return leftOperandResult - rightOperandResult;
    }

    @Override
    protected int calculateOperation(int leftOperandResult, int rightOperandResult) {
        return leftOperandResult - rightOperandResult;
    }
}
