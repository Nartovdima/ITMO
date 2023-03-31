package expression.generic;

import expression.operations.*;
import expression.exceptions.*;
import expression.types.Operations;

public class ExpressionParser<T extends Number> {
    private Lexem currLexem;
    private String constValue;
    private String expression;
    private int currPos, lexemStartPos, bracketBalance;
    private String varName;
    private final Operations<T> evaluator;

    public ExpressionParser(Operations<T> evaluator) {
        this.evaluator = evaluator;
    }
    public MyExpression<T> parse(String expression) throws ParsingException {
        currPos = 0;
        lexemStartPos = 1;
        bracketBalance = 0;
        this.expression = expression + "\u0000";
        currLexem = null;
        getLexem();
        MyExpression<T> currRes = parseAddSub();
        if (currLexem == Lexem.RIGHTBRACKET && bracketBalance <= 0) {
            throw new ParsingException("Missing bracket");
        } else if (currLexem != Lexem.END) {
            throw new ParsingException("Expected End of input");
        }
        return currRes;
    }

    private void skipWhitespaces() {
        while (currPos < expression.length() && Character.isWhitespace(expression.charAt(currPos))) {
            currPos++;
        }
    }

    private String getIdentifier() {
        StringBuilder result = new StringBuilder();
        int pos = currPos;
        if (Character.isJavaIdentifierStart(expression.charAt(pos))) {
            result.append(expression.charAt(pos));
            pos++;
            while (expression.charAt(pos) != '\u0000' && Character.isJavaIdentifierPart(expression.charAt(pos))) {
                result.append(expression.charAt(pos));
                pos++;
            }
        }
        return result.toString();
    }

    private boolean isNumber() {
        int pos = currPos;
        StringBuilder result = new StringBuilder();
        if (expression.charAt(pos) == '-' || Character.isDigit(expression.charAt(pos))) {
            result.append(expression.charAt(pos));
            pos++;
            while (Character.isDigit(expression.charAt(pos))) {
                result.append(expression.charAt(pos));
                pos++;
            }
        }
        if (!result.isEmpty()) {
            constValue = result.toString();
        }
        return !result.isEmpty();
    }

    private void getLexem() throws ParsingException {
        lexemStartPos = currPos + 1;
        skipWhitespaces();
        switch (expression.charAt(currPos)) {
            case '+' -> // ADD
                    currLexem = Lexem.ADD;
            case '-' -> { // SUB, NEGATE
                if (currLexem == Lexem.RIGHTBRACKET || currLexem == Lexem.VAR || currLexem == Lexem.CONST) {
                    currLexem = Lexem.SUB;
                } else if (!Character.isDigit(expression.charAt(currPos + 1))) {
                    currLexem = Lexem.NEGATE;
                } else if (isNumber()) {
                    currLexem = Lexem.CONST;
                    currPos += constValue.length() - 1;
                }
            }
            case '*' -> // MUL
                    currLexem = Lexem.MUL;
            case '/' -> // DIV
                    currLexem = Lexem.DIV;
            case 's' -> { // SQUARE or Invalid lexem
                if (getIdentifier().equals("square")) {
                    currLexem = Lexem.SQUARE;
                    currPos += 5;
                } else {
                    throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
            case 'a' -> {
                if (getIdentifier().equals("abs")) {
                    currLexem = Lexem.ABS;
                    currPos += 2;
                } else {
                    throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
            case 'm' -> {
                if (getIdentifier().equals("mod")) {
                    currLexem = Lexem.MOD;
                    currPos += 2;
                } else {
                    throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
            case 'x', 'y', 'z' -> { // VAR
                currLexem = Lexem.VAR;
                varName = String.valueOf(expression.charAt(currPos));
            }
            case '(' -> // LEFTBRACKET
                    currLexem = Lexem.LEFTBRACKET;
            case ')' -> // RIGHTBRACKET
                    currLexem = Lexem.RIGHTBRACKET;
            case '\u0000' -> // END
                    currLexem = Lexem.END;
            default -> { // CONST or Invalid lexem
                if (isNumber()) {
                    currLexem = Lexem.CONST;
                    currPos += constValue.length() - 1;
                } else {
                    throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
        }
        currPos++;
    }

    private MyExpression<T> parseAddSub() throws ParsingException {
        MyExpression<T> currExp = parseDivMul();
        while (currLexem == Lexem.ADD || currLexem == Lexem.SUB) {
            if (currLexem == Lexem.ADD) {
                getLexem();
                currExp = new Add<>(currExp, parseDivMul());
            }
            if (currLexem == Lexem.SUB) {
                getLexem();
                currExp = new Subtract<>(currExp, parseDivMul());
            }
        }
        return currExp;
    }

    private MyExpression<T> parseDivMul() throws ParsingException {
        MyExpression<T> currExp = parseUnaryOperation();
        while (currLexem == Lexem.MUL || currLexem == Lexem.DIV || currLexem == Lexem.MOD) {
            if (currLexem == Lexem.MUL) {
                getLexem();
                currExp = new Multiply<>(currExp, parseUnaryOperation());
            }
            if (currLexem == Lexem.DIV) {
                getLexem();
                currExp = new Divide<>(currExp, parseUnaryOperation());
            }
            if (currLexem == Lexem.MOD) {
                getLexem();
                currExp = new MyMod<>(currExp, parseUnaryOperation());
            }
        }
        return currExp;
    }

    private MyExpression<T> parseUnaryOperation() throws ParsingException {
        MyExpression<T> currExp = null;
        if (currLexem == Lexem.ABS) {
            getLexem();
            currExp = new Abs<>(parseUnaryOperation());
        } else if (currLexem == Lexem.NEGATE) {
            getLexem();
            currExp = new Negate<>(parseUnaryOperation());
        } else if (currLexem == Lexem.SQUARE) {
            getLexem();
            currExp = new Square<>(parseUnaryOperation());
        }  else {
            currExp = parseValues();
        }
        return currExp;
    }

    private MyExpression<T> parseValues() throws ParsingException {
        MyExpression<T> currExp = null;
        if (currLexem == Lexem.CONST) {
            currExp = new Const<>(evaluator.constCast(constValue));
        } else if (currLexem == Lexem.VAR) {
            currExp = new Variable<>(varName);
        } else if (currLexem == Lexem.RIGHTBRACKET && bracketBalance <= 0) {
            throw new ParsingException("Missing bracket");
        } else if (currLexem == Lexem.LEFTBRACKET) {
            bracketBalance++;
            getLexem();
            currExp = parseAddSub();
            if (currLexem != Lexem.RIGHTBRACKET) {
                throw new ParsingException("Expected bracket at position " + lexemStartPos);
            } else {
                bracketBalance--;
            }
        }
        if (currExp == null) {
            throw new ParsingException("Expected operand at position " + lexemStartPos);
        }
        getLexem();
        return currExp;
    }
}
