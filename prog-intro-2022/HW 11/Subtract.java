package expression;

public class Subtract extends BinaryOperation {
    public Subtract(MyExpression leftOperand, MyExpression rightOperand) {
        super(leftOperand, rightOperand, Operation.SUBTRACTION);
    }
}
