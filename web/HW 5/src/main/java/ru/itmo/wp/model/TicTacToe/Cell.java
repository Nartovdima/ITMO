package ru.itmo.wp.model.TicTacToe;

public enum Cell {
    X("X"),
    O("O"),
    E(" ");
    private final String sign;
    Cell(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return sign;
    }
}
