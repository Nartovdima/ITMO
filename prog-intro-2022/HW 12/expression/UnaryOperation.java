package expression;

import java.util.Objects;

public abstract class UnaryOperation implements MyExpression {
    protected final MyExpression operand;
    public UnaryOperation(MyExpression operand) {
        this.operand = operand;
    }

    public MyExpression getOperand() {
        return operand;
    }

    public abstract Operation getOperation();
    @Override
    public abstract String toMiniString();

    @Override
    public String toString() {
        return this.getOperation().getSymbol() + "(" + operand.toString() + ")";
    }

    @Override
    public double evaluate(double value) {
        return calculateOperation(operand.evaluate(value));
    }

    @Override
    public int evaluate(int value) {
        return calculateOperation(operand.evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculateOperation(operand.evaluate(x, y, z));
    }

    protected abstract int calculateOperation(int operandResult);
    protected abstract double calculateOperation(double operandResult);

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final UnaryOperation that = (UnaryOperation) obj;
            return this.getOperand().equals(that.getOperand());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getOperand(), getOperation());
    }
}
