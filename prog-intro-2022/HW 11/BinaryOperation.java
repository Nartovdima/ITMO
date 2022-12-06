import java.util.Map;

public abstract class BinaryOperation implements MyExpression {
    protected final MyExpression leftOperand;
    protected final MyExpression rightOperand;
    protected final Operation operation;
    protected final int priority;
    private static final Map<Operation, String> OPERATION_SYMBOL = Map.of(
            Operation.ADDITION, "+",
            Operation.SUBTRACTION, "-",
            Operation.MULTIPLICATION, "*",
            Operation.DIVISION, "/"
    );

    private static final Map<Operation, Integer> OPERATION_PRIORITY = Map.of(
            Operation.ADDITION, 1,
            Operation.SUBTRACTION, 1,
            Operation.MULTIPLICATION, 0,
            Operation.DIVISION, 0
    );
    protected BinaryOperation (
            MyExpression leftOperand,
            MyExpression rightOperand,
            Operation operation
    ) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operation = operation;
        this.priority = OPERATION_PRIORITY.get(operation);
    }

    public Operation getOperation() {
        return operation;
    }

    public MyExpression getLeftOperand() {
        return leftOperand;
    }

    public MyExpression getRightOperand() {
        return rightOperand;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return (
                "(" +
                leftOperand.toString() +
                " " +
                OPERATION_SYMBOL.get(operation) +
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

        expr.append(" ").append(OPERATION_SYMBOL.get(operation)).append(" ");

        if (rightExprNeedBrackets(rightOperand)) {
            expr.append("(").append(rightOperand.toMiniString()).append(")");
        } else {
            expr.append(rightOperand.toMiniString());
        }

        return expr.toString();
    }

    private boolean leftExprNeedBrackets(MyExpression operand) {
        if (operand instanceof BinaryOperation that) {
            return this.getPriority() < that.getPriority();
        }
        return false;
    }

    private boolean rightExprNeedBrackets(MyExpression operand) {
        if (operand instanceof BinaryOperation that) {
            return  this.getPriority() < that.getPriority() || (
                    (this.getPriority() == that.getPriority()) &&
                    (this.getOperation() == Operation.SUBTRACTION || that.getOperation() == Operation.DIVISION) ||
                    this.getOperation() == Operation.DIVISION
            );
        }
        return false;
    }

    public int evaluate(int value) {
        int operationResult = 0;
        switch (operation) {
            case ADDITION -> operationResult = leftOperand.evaluate(value)+ rightOperand.evaluate(value);
            case SUBTRACTION -> operationResult = leftOperand.evaluate(value) - rightOperand.evaluate(value);
            case MULTIPLICATION -> operationResult = leftOperand.evaluate(value) * rightOperand.evaluate(value);
            case DIVISION -> operationResult = leftOperand.evaluate(value) / rightOperand.evaluate(value);
        }
        return operationResult;
    }

    public double evaluate(double value) {
        double operationResult = 0;
        switch (operation) {
            case ADDITION -> operationResult = leftOperand.evaluate(value)+ rightOperand.evaluate(value);
            case SUBTRACTION -> operationResult = leftOperand.evaluate(value) - rightOperand.evaluate(value);
            case MULTIPLICATION -> operationResult = leftOperand.evaluate(value) * rightOperand.evaluate(value);
            case DIVISION -> operationResult = leftOperand.evaluate(value) / rightOperand.evaluate(value);
        }
        return operationResult;
    }

    public int evaluate(int x, int y, int z) {
        int operationResult = 0;
        switch (operation) {
            case ADDITION -> operationResult = leftOperand.evaluate(x, y, z)+ rightOperand.evaluate(x, y, z);
            case SUBTRACTION -> operationResult = leftOperand.evaluate(x, y, z)- rightOperand.evaluate(x, y, z);
            case MULTIPLICATION -> operationResult = leftOperand.evaluate(x, y, z) * rightOperand.evaluate(x, y, z);
            case DIVISION -> operationResult = leftOperand.evaluate(x, y, z)/ rightOperand.evaluate(x, y, z);
        }
        return operationResult;
    }
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
        return this.toString().hashCode();
    }
}
