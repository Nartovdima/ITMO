package ru.itmo.wp.model.TicTacToe;

public class State extends Board {
    private Cell turn;
    public State() {
        super();
        turn = Cell.X;
    }

    private boolean isValidMove(Move move) {
        return 0 <= move.getRow() && move.getRow() < SIZE
                && 0 <= move.getColumn() && move.getColumn() < SIZE
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == move.getTurn();
    }
    public void makeMove(Move move) {
        if (isValidMove(move)) {
            cells[move.getRow()][move.getColumn()] = move.getTurn();
            turn = (turn == Cell.X ? Cell.O : Cell.X);
        }
    }

    public Cell getTurn() {
        return turn;
    }
}
