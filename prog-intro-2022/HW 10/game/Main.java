package game;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter the first dimension of field: ");
        int m = in.nextInt();
        System.out.print("Please enter the second dimension of field: ");
        int n = in.nextInt();
        System.out.print("Please enter the number of signs in a row: ");
        int k = in.nextInt();
        System.out.println("Select game mode:");
        System.out.println("1 - Classic Game");
        System.out.println("2 - Tournament");
        int mode = in.nextInt();

        if (mode == 1) {
            final int result = new TwoPlayerGame(
                    new MNKBoard(m, n, k),
                    //new TicTacToeBoard(),
                    new HumanPlayer(in),
                    new HumanPlayer(in)
            ).play(true);
            switch (result) {
                case 1:
                    System.out.println("First player won");
                    break;
                case 2:
                    System.out.println("Second player won");
                    break;
                case 0:
                    System.out.println("Draw");
                    break;
                default:
                    throw new AssertionError("Unknown result " + result);
            }
        } else {
            List<Player> listOfPlayers = new ArrayList<>();
            List<LeaderBoardElement> result = new ArrayList<>();
            System.out.println("Enter a number of players: ");
            int numOfPlayers = in.nextInt();
            for (int i = 0; i < numOfPlayers; i++) {
                System.out.println("Enter a type of Player number" + (i + 1));
                System.out.println("1 - Random player");
                System.out.println("2 - Sequential player");
                System.out.println("3 - Human player");
                int type = in.nextInt();
                switch (type) {
                    case 1 -> listOfPlayers.add(new RandomPlayer());
                    case 2 -> listOfPlayers.add(new SequentialPlayer());
                    case 3 -> listOfPlayers.add(new HumanPlayer(in));
                }
                System.out.print("Enter " + (i + 1) + " player name: ");
                result.add(new LeaderBoardElement(in.next()));
            }
            MNKBoard tmpBoard = new MNKBoard(n, m, k);
            for (int i = 0; i < n; i++) {
                tmpBoard.addBarrier(i, i);
            }
            Tournament tmp = new Tournament(
                    tmpBoard,
                    listOfPlayers,
                    result
            );
            tmp.play();
            result = tmp.getResult();
            Collections.sort(result);
            for (LeaderBoardElement i : result) {
                System.out.println(i);
            }
        }
    }

    private void addBarrier() {

    }
}
