package ru.itmo.wp.model.TicTacToe;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Board {
    protected final int SIZE = 3;
    protected final Cell[][] cells;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
    }

    public int getSIZE() {
        return SIZE;
    }

    public Cell getCell(int row, int column) {
        return  cells[row][column];
    }

    private int countInLine(Move lastMove, int dr, int dc) {
        int x = lastMove.getRow(), y = lastMove.getColumn();
        int cnt = 0;
        Cell prev = lastMove.getTurn();
        while (
                0 <= x && x < SIZE &&
                0 <= y && y < SIZE &&
                prev == getCell(x, y)
        ) {
            cnt++;
            x += dr;
            y += dc;
        }
        return cnt;
    }

    private boolean isWin(Move lastMove) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if (countInLine(lastMove, i, j) + countInLine(lastMove, -i, -j) - 1 >= SIZE) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDraw() {
        int count = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c] == Cell.E) {
                    count++;
                }
            }
        }
        return count == 0;
    }

    public Phase getGameStage() {
        if (isDraw()) {
            return Phase.DRAW;
        }
        Cell[] sides = new Cell[]{Cell.X, Cell.O};
        for (Cell currTurn : sides) {
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    if (isWin(new Move(currTurn, i, j))) {
                        return (currTurn.equals(Cell.X) ? Phase.WON_X : Phase.WON_O);
                    }
                }
            }
        }
        return Phase.RUNNING;
    }
}
