package expression.types;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerOverflowOp implements Operations<Integer> {
    @Override
    public Integer add(Integer leftOperand, Integer rightOperand) {
        if (
                rightOperand > 0 && Integer.MAX_VALUE - rightOperand < leftOperand ||
                rightOperand < 0 && Integer.MIN_VALUE - rightOperand > leftOperand
        ) {
            throw new OverflowException("overflow");
        }
        return leftOperand + rightOperand;
    }

    @Override
    public Integer subtract(Integer leftOperand, Integer rightOperand) {
        if (
                rightOperand > 0 && Integer.MIN_VALUE + rightOperand > leftOperand ||
                rightOperand < 0 && Integer.MAX_VALUE + rightOperand < leftOperand
        ) {
            throw new OverflowException("overflow");
        }
        return leftOperand - rightOperand;
    }

    @Override
    public Integer multiply(Integer leftOperand, Integer rightOperand) {
        if (
                leftOperand == Integer.MIN_VALUE && rightOperand == -1 ||
                rightOperand != 0 &&
                leftOperand * rightOperand / rightOperand != leftOperand
        ) {
            throw new OverflowException("overflow");
        }
        return leftOperand * rightOperand;
    }

    @Override
    public Integer divide(Integer leftOperand, Integer rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        if (leftOperand == Integer.MIN_VALUE && rightOperand == -1) {
            throw new OverflowException("overflow");
        }
        return leftOperand / rightOperand;
    }

    @Override
    public Integer negate(Integer operand) {
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return -operand;
    }

    @Override
    public Integer abs(Integer operand) {
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return Math.abs(operand);
    }

    @Override
    public Integer square(Integer operand) {
        if (
                operand != 0 &&
                operand * operand / operand != operand
        ) {
            throw new OverflowException("overflow");
        }
        return operand * operand;
    }

    @Override
    public Integer mod(Integer leftOperand, Integer rightOperand) {
        if (rightOperand.equals(0)) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand % rightOperand;
    }

    @Override
    public Integer constCast(String value) {
        return Integer.parseInt(value);
    }
}
