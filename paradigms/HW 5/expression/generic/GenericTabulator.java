package expression.generic;

import expression.exceptions.ParsingException;
import expression.operations.MyExpression;
import expression.types.*;

public class GenericTabulator implements Tabulator{

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Operations<?> evaluator =
                switch(mode) {
                    case "i" -> new IntegerOverflowOp();
                    case "d" -> new DoubleOp();
                    case "bi" -> new BigIntegerOp();
                    case "u" -> new IntegerOp();
                    case "l" -> new LongOp();
                    case "s" -> new ShortOp();
                    default -> throw new IllegalArgumentException("Unsupported mode");
                };
        return calculate(expression, evaluator, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] calculate(String expression, Operations<T> evaluator, int x1, int x2, int y1, int y2, int z1, int z2) {
        MyExpression<T> expr;
        try {
            expr = new ExpressionParser<>(evaluator).parse(expression);
        } catch (ParsingException e){
            System.out.println(e.getMessage());
            return null;
        }

        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    T x = evaluator.constCast(String.valueOf(i));
                    T y = evaluator.constCast(String.valueOf(j));
                    T z = evaluator.constCast(String.valueOf(k));

                    try{
                        ans[i - x1][j - y1][k - z1] = expr.evaluate(x, y, z, evaluator);
                    } catch (ArithmeticException e) {
                        ans[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return ans;
    }
}
