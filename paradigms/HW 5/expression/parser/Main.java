package expression.parser;

import expression.*;

public class Main {
    public static void main(String[] argv) {
        ExpressionParser a = new ExpressionParser();
        TripleExpression b = a.parse("- -(x + -2147483648) + x * x * - 100");
        System.out.println("jaja");
    }
}
