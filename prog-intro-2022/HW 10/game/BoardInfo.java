package game;


public class BoardInfo implements Position {
    private final Cell turn;
    private final Position position;

    BoardInfo(Position position, Cell turn) {
        this.position = position;
        this.turn = turn;
    }
    @Override
    public Cell getTurn() {
        return position.getTurn();
    }

    @Override
    public boolean isValid(Move move) {
        return position.isValid(move);
    }

    @Override
    public Cell getCell(int row, int column) {
        return position.getCell(row, column);
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
