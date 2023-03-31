package expression.types;

import expression.exceptions.DivisionByZeroException;

public class LongOp implements Operations<Long> {
    @Override
    public Long add(Long leftOperand, Long rightOperand) {
        return leftOperand + rightOperand;
    }

    @Override
    public Long subtract(Long leftOperand, Long rightOperand) {
        return leftOperand - rightOperand;
    }

    @Override
    public Long multiply(Long leftOperand, Long rightOperand) {
        return leftOperand * rightOperand;
    }

    @Override
    public Long divide(Long leftOperand, Long rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand / rightOperand;
    }

    @Override
    public Long negate(Long operand) {
        return -operand;
    }

    @Override
    public Long abs(Long operand) {
        return Math.abs(operand);
    }

    @Override
    public Long square(Long operand) {
        return operand * operand;
    }

    @Override
    public Long mod(Long leftOperand, Long rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand % rightOperand;
    }

    @Override
    public Long constCast(String value) {
        return Long.parseLong(value);
    }
}
