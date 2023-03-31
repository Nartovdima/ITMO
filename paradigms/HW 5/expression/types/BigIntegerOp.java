package expression.types;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerOp implements Operations<BigInteger> {

    @Override
    public BigInteger add(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.add(rightOperand);
    }

    @Override
    public BigInteger subtract(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.subtract(rightOperand);
    }

    @Override
    public BigInteger multiply(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.multiply(rightOperand);
    }

    @Override
    public BigInteger divide(BigInteger leftOperand, BigInteger rightOperand) {
        if (rightOperand.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand.divide(rightOperand);
    }

    @Override
    public BigInteger negate(BigInteger operand) {
        return operand.negate();
    }

    @Override
    public BigInteger abs(BigInteger operand) {
        return operand.abs();
    }

    @Override
    public BigInteger square(BigInteger operand) {
        return operand.pow(2);
    }


    @Override
    public BigInteger mod(BigInteger leftOperand, BigInteger rightOperand) {
        if (rightOperand.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("division by zero");
        }
        return leftOperand.mod(rightOperand);
    }

    @Override
    public BigInteger constCast(String value) {
        return new BigInteger(value);
    }


}
