package expression.types;

import expression.exceptions.DivisionByZeroException;

public class ShortOp implements Operations<Short> {

    @Override
    public Short add(Short leftOperand, Short rightOperand) {
        return (short) (leftOperand + rightOperand);
    }

    @Override
    public Short subtract(Short leftOperand, Short rightOperand) {
        return (short) (leftOperand - rightOperand);
    }

    @Override
    public Short multiply(Short leftOperand, Short rightOperand) {
        return (short) (leftOperand * rightOperand);
    }

    @Override
    public Short divide(Short leftOperand, Short rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return (short) (leftOperand / rightOperand);
    }

    @Override
    public Short negate(Short operand) {
        return (short) (-operand);
    }

    @Override
    public Short abs(Short operand) {
        return (short) (Math.abs(operand));
    }

    @Override
    public Short square(Short operand) {
        return (short) (operand * operand);
    }

    @Override
    public Short mod(Short leftOperand, Short rightOperand) {
        if (rightOperand == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return (short) (leftOperand % rightOperand);
    }

    @Override
    public Short constCast(String value) {
        return (short) Integer.parseInt(value);
    }
}
