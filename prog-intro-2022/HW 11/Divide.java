package expression;

public class Divide extends BinaryOperation {
    public Divide(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand, Operation.DIVISION);
    }
}
