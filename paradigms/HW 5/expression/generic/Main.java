package expression.generic;

public class Main {
    public static void main(String[] args) {
        GenericTabulator test1 = new GenericTabulator();
        Object[][][] values;
        try {
            values = test1.tabulate(args[0], args[1], -2, 2, -2, 2, -2, 2);
            for (int i = -2; i < 2; i++) {
                for (int j = -2; j < 2; j++) {
                    for (int k = -2; k < 2; k++) {
                        System.out.println("(" + i + ", " + j + ", " + k + ") = " + values[i + 2][j + 2][k + 2]);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
