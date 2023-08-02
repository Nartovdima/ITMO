package expression;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyExpression test = new Add(
                new Subtract(
                        new Multiply(
                                new Variable("x"),
                                new Variable("x")
                        ),
                        new Multiply(
                                new Const(2),
                                new Variable("x")
                        )
                ),
                new Const(1)
        );

        Scanner in = new Scanner(System.in);
        if (in.hasNextInt()) {
            int val = in.nextInt();
            System.out.println(test.evaluate(val));
        } else {
            double val = in.nextDouble();
            System.out.println(test.evaluate(val));
        }
    }
}
