package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static game.Cell.X;

public class MNKBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0",
            Cell.B, "B"
    );
    private final int m, n, k;
    private final Cell[][] field;
    private Cell turn;

    MNKBoard(int m, int n, int k) {
        this.n = n;
        this.m = m;
        this.k = k;

        field = new Cell[m][n];

        for (Cell [] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    public MNKBoard(MNKBoard board) {
        this.n = board.n;
        this.m = board.m;
        this.k = board.k;
        field = new Cell[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                field[i][j] = board.getCell(i, j);
            }
        }
        turn = X;
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    @Override
    public Position getPosition() {
        return new BoardInfo(this, turn);
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!isValid(move)) {
            return GameResult.LOOSE;
        }

        field[move.getRow()][move.getCol()] = move.getValue();
        if (checkWin(move)) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == X ? Cell.O : X;
        return GameResult.UNKNOWN;
    }
    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getCol() && move.getCol() < n
                && field[move.getRow()][move.getCol()] == Cell.E
                && turn == move.getValue();
    }

    private boolean checkDraw() {
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (field[i][j] == Cell.E) {
                    count++;
                }
            }
        }
        return count == 0;
    }

    private int countInLine(Move move, int dr, int dc) {
        int x = move.getRow(), y = move.getCol();
        int cnt = 0;
        Cell prev = move.getValue();
        while (
                0 <= x && x < m &&
                0 <= y && y < n &&
                prev == getCell(x, y)
        ) {
                cnt++;
                x += dr;
                y += dc;
        }
        return cnt;
    }
    private boolean checkWin(Move move) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if (countInLine(move, i, j) + countInLine(move, -i, -j) - 1 >= k) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column];
    }

    public void addBarrier(int row, int column) {
        field[row][column] = Cell.B;
    }
    @Override
    public String toString() {

        int horizontalNumberLength = Integer.toString(n).length();
        int verticalNumberLength = Integer.toString(m).length();

        StringBuilder boardView = new StringBuilder();

        boardView.append(" ".repeat(verticalNumberLength));
        for (int i = 1; i <= n; i++) {
            boardView.append(String.format("%0" + Integer.toString(horizontalNumberLength)  + "d", i));
        }
        boardView.append(System.lineSeparator());

        for (int i = 0; i < m; i++) {
            boardView.append(String.format("%0" + Integer.toString(verticalNumberLength) + "d", i + 1));
            for (int j = 0; j < n; j++) {
                boardView.append(" ".repeat(horizontalNumberLength - 1))
                         .append(CELL_TO_STRING.get(getCell(i, j)));
            }
            boardView.append(System.lineSeparator());
        }
        return boardView.toString();
    }
}
