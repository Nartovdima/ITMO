package expression.exceptions;

public class Main {
    public static void main(String[] args) throws ParsingException {
        ExpressionParser a = new ExpressionParser();
        a.parse("+");
    }
}
