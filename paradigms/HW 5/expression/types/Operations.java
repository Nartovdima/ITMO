package expression.types;

public interface Operations<T extends Number> {
    T add(T leftOperand, T rightOperand);

    T subtract(T leftOperand, T rightOperand);

    T multiply(T leftOperand, T rightOperand);

    T divide(T leftOperand, T rightOperand);

    T negate(T operand);

    T abs(T operand);

    T square(T operand);

    T mod(T leftOperand, T rightOperand);

    T constCast(String value);
}
