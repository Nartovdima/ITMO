package expression;
public class Divide extends BinaryOperation {
    public Divide(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Operation getOperation() {
        return Operation.DIVISION;
    }

    @Override
    protected double calculateOperation(double leftOperandResult, double rightOperandResult) {
        return leftOperandResult / rightOperandResult;
    }

    @Override
    protected int calculateOperation(int leftOperandResult, int rightOperandResult) {
        return leftOperandResult / rightOperandResult;
    }
}
