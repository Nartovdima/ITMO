package expression.exceptions;

public enum Lexem {
    ADD(0),
    SUB(0),
    MUL(0),
    DIV(0),
    SET(0),
    CLEAR(0),
    NEGATE(1),
    COUNT(1),
    LOG(1),
    POW(1),
    CONST(2),
    VAR(2),
    LEFTBRACKET(3),
    RIGHTBRACKET(3),
    END(4);

    private int type;

    Lexem(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
