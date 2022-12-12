package expression;

import java.util.Objects;

public abstract class BinaryOperation implements MyExpression {
    protected final MyExpression leftOperand;
    protected final MyExpression rightOperand;

    protected BinaryOperation (
            MyExpression leftOperand,
            MyExpression rightOperand
    ) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public abstract Operation getOperation();

    public MyExpression getLeftOperand() {
        return leftOperand;
    }

    public MyExpression getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return (
                "(" +
                leftOperand.toString() +
                " " +
                this.getOperation().getSymbol() +
                " " +
                rightOperand.toString() + ")"
        );
    }


    public String toMiniString() {
        StringBuilder expr = new StringBuilder();

        if (leftExprNeedBrackets(leftOperand)) {
            expr.append("(").append(leftOperand.toMiniString()).append(")");
        } else {
            expr.append(leftOperand.toMiniString());
        }

        expr.append(" ").append(this.getOperation().getSymbol()).append(" ");

        if (rightExprNeedBrackets(rightOperand)) {
            expr.append("(").append(rightOperand.toMiniString()).append(")");
        } else {
            expr.append(rightOperand.toMiniString());
        }

        return expr.toString();
    }

    private boolean leftExprNeedBrackets(MyExpression operand) {
        if (operand instanceof BinaryOperation that) {
            return this.getOperation().getPriority() < that.getOperation().getPriority();
        }
        return false;
    }

    private boolean rightExprNeedBrackets(MyExpression operand) {
        if (operand instanceof BinaryOperation that) {
            return  this.getOperation().getPriority() < that.getOperation().getPriority() || (
                    (this.getOperation().getPriority() == that.getOperation().getPriority()) &&
                    (this.getOperation() == Operation.SUBTRACTION || that.getOperation() == Operation.DIVISION) ||
                    this.getOperation() == Operation.DIVISION
            );
        }
        return false;
    }

    public int evaluate(int value) {
        return calculateOperation(leftOperand.evaluate(value), rightOperand.evaluate(value));
    }

    public double evaluate(double value) {
        return calculateOperation(leftOperand.evaluate(value), rightOperand.evaluate(value));
    }

    public int evaluate(int x, int y, int z) {
        return calculateOperation(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }

    protected abstract int calculateOperation(int leftOperandResult, int rightOperandResult);
    protected abstract double calculateOperation(double leftOperandResult, double rightOperandResult);
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation that) {
            return (
                    this.getOperation().equals(that.getOperation()) &&
                    this.getLeftOperand().equals(that.getLeftOperand()) &&
                    this.getRightOperand().equals(that.getRightOperand())
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLeftOperand(), this.getRightOperand(), this.getOperation());
    }
}
