package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tournament {
    private final Board board;
    private final List<Player> players;
    private List<LeaderBoardElement> result;

    public Tournament(Board board, List<Player> players, List<LeaderBoardElement> result) {
        this.board = board;
        this.players = players;
        this.result = result;
    }

    public void play() {
        int n = players.size();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                System.out.println("New round" +
                        System.lineSeparator() +
                        result.get(i).getName() +
                        " vs " +
                        result.get(j).getName()
                );

                int resultOfMatch = new TwoPlayerGame(
                        new MNKBoard((MNKBoard) board),
                        players.get(i),
                        players.get(j)
                ).play(true);
                switch (resultOfMatch) {
                    case 1:
                        System.out.println(result.get(i).getName() + " won");
                        result.get(i).increasePoints(3);
                        break;
                    case 2:
                        System.out.println(result.get(j).getName() + " won");
                        result.get(j).increasePoints(3);
                        break;
                    case 0:
                        System.out.println("Draw");
                        result.get(i).increasePoints(1);
                        result.get(i).increasePoints(1);
                        break;
                    default:
                        throw new AssertionError("Unknown result " + result);
                }
            }
        }
    }

    public List<LeaderBoardElement> getResult() {
        return result;
    }
}
