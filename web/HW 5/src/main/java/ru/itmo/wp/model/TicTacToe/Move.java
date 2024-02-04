package ru.itmo.wp.model.TicTacToe;

public class Move {
    private final Cell turn;
    private final int row;
    private final int column;

    public Move(Cell turn, int row, int column) {
        this.turn = turn;
        this.row = row;
        this.column = column;
    }

    public Cell getTurn() {
        return turn;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
