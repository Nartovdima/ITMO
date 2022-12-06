public interface MyExpression extends Expression, DoubleExpression, TripleExpression {
    String toString();
    String toMiniString();

    double evaluate(double value);

    int evaluate(int value);

    int evaluate(int x, int y, int z);
    boolean equals(Object obj);
    int hashCode();
}
