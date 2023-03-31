package expression.types;

import expression.exceptions.DivisionByZeroException;

public class IntegerOp implements Operations<Integer> {
    @Override
    public Integer add(Integer leftOperand, Integer rightOperand) {
        return leftOperand + rightOperand;
    }

    @Override
    public Integer subtract(Integer leftOperand, Integer rightOperand) {
        return leftOperand - rightOperand;
    }

    @Override
    public Integer multiply(Integer leftOperand, Integer rightOperand) {
        return leftOperand * rightOperand;
    }

    @Override
    public Integer divide(Integer leftOperand, Integer rightOperand) {
        if (rightOperand.equals(0)) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand / rightOperand;
    }

    @Override
    public Integer negate(Integer operand) {
        return -operand;
    }

    @Override
    public Integer abs(Integer operand) {
        return Math.abs(operand);
    }

    @Override
    public Integer square(Integer operand) {
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
