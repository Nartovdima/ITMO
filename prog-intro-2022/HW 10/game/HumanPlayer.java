package game;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;

    public HumanPlayer(Scanner in) {
        this.in = in;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(position);
        System.out.println("Enter you move for " + position.getTurn());
        String row = null, column = null;

        do {
            int cnt = 0;
            if (in.hasNext()) {
                cnt++;
                row = in.next();
            }
            if (in.hasNext()) {
                cnt++;
                column = in.next();
            }
            if (cnt != 2) {
                System.out.println("Please enter two numbers");
            }

        } while(!checkInput(row, column, position));


        return new Move(Integer.parseInt(row) - 1, Integer.parseInt(column) - 1, position.getTurn());
    }

    private boolean checkInput(String row, String column, Position position) {
        if (!isNumber(row)) {
            return false;
        }

        if (!isNumber(column)) {
            return false;
        }

        if (
                !position.isValid(new Move(
                    Integer.parseInt(row) - 1,
                    Integer.parseInt(column) - 1,
                    position.getTurn()
                ))
        ) {
            System.out.println("Incorrect cell, please try another.");
            return false;
        }

        return true;
    }

    private boolean isNumber(String num) {
        for (int i = 0; i < num.length(); i++) {
            if (!Character.isDigit(num.charAt(i))) {
                System.out.println("Seems that you don't enter a number");
                return false;
            }
        }
        return true;
    }
}
