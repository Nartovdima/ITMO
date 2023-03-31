package expression.types;

import expression.exceptions.DivisionByZeroException;

public class DoubleOp implements Operations<Double> {
    @Override
    public Double add(Double leftOperand, Double rightOperand) {
        return leftOperand + rightOperand;
    }

    @Override
    public Double subtract(Double leftOperand, Double rightOperand) {
        return leftOperand - rightOperand;
    }

    @Override
    public Double multiply(Double leftOperand, Double rightOperand) {
        return leftOperand * rightOperand;
    }

    @Override
    public Double divide(Double leftOperand, Double rightOperand) {
        return leftOperand / rightOperand;
    }

    @Override
    public Double negate(Double operand) {
        return -operand;
    }

    @Override
    public Double abs(Double operand) {
        return Math.abs(operand);
    }

    @Override
    public Double square(Double operand) {
        return Math.pow(operand, 2);
    }

    @Override
    public Double mod(Double leftOperand, Double rightOperand) {
        return leftOperand % rightOperand;
    }

    @Override
    public Double constCast(String value) {
        return Double.parseDouble(value);
    }
}
