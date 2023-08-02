package expression.exceptions;

import expression.*;

public class ExpressionParser implements TripleParser {
    private Lexem currLexem;
    private String constValue;
    private String expression;
    private int currPos, lexemStartPos, bracketBalance;
    private String varName;
    @Override
    public TripleExpression parse(String expression) throws ParsingException {
        currPos = 0;
        lexemStartPos = 1;
        bracketBalance = 0;
        this.expression = expression + "\u0000";
        currLexem = null;
        nextLexem();
        TripleExpression currRes = firstLevelToken();
        if (currLexem == Lexem.RIGHT_BRACKET && bracketBalance <= 0) {
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

    private void nextLexem() throws ParsingException {
        lexemStartPos = currPos + 1;
        skipWhitespaces();
        switch (expression.charAt(currPos)) {
            case '+' -> // ADD
                    currLexem = Lexem.ADD;
            case '-' -> { // SUB, NEGATE
                if (currLexem == Lexem.RIGHT_BRACKET || currLexem == Lexem.VAR || currLexem == Lexem.CONST) {
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
            // :NOTE: copypaste
            case 's' -> { // SET or Invalid lexem
                if (getIdentifier().equals("set")) {
                    currLexem = Lexem.SET;
                    currPos += 2;
                } else {
                    throw new ParsingException("Unsupported identifier at position" + lexemStartPos);
                }
            }
            case 'c' -> { // CLEAR, COUNT or Invalid lexem
                switch (getIdentifier()) {
                    case "clear" -> {
                        currLexem = Lexem.CLEAR;
                        currPos += 4;
                    }
                    case "count" -> {
                        currLexem = Lexem.COUNT;
                        currPos += 4;
                    }
                    default -> throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
            case 'l' -> { // log10
                if (getIdentifier().equals("log10")) {
                    currLexem = Lexem.LOG;
                    currPos += 4;
                } else {
                    throw new ParsingException("Unsupported identifier at position " + lexemStartPos);
                }
            }
            case 'p' -> { // pow10
                if (getIdentifier().equals("pow10")) {
                    currLexem = Lexem.POW;
                    currPos += 4;
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
                    currLexem = Lexem.RIGHT_BRACKET;
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

    private MyExpression firstLevelToken() throws ParsingException {
        MyExpression currExp = secondLevelToken();
        while (currLexem == Lexem.SET || currLexem == Lexem.CLEAR) {
            if (currLexem == Lexem.SET) {
                nextLexem();
                currExp = new Set(currExp, secondLevelToken());
            }
            if (currLexem == Lexem.CLEAR) {
                nextLexem();
                currExp = new Clear(currExp, secondLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression secondLevelToken() throws ParsingException {
        MyExpression currExp = thirdLevelToken();
        while (currLexem == Lexem.ADD || currLexem == Lexem.SUB) {
            if (currLexem == Lexem.ADD) {
                nextLexem();
                currExp = new CheckedAdd(currExp, thirdLevelToken());
            }
            if (currLexem == Lexem.SUB) {
                nextLexem();
                currExp = new CheckedSubtract(currExp, thirdLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression thirdLevelToken() throws ParsingException {
        MyExpression currExp = fourthLevelToken();
        while (true) {
            if (test(Lexem.MUL)) {
                currExp = new CheckedMultiply(currExp, fourthLevelToken());
            } else if (test(Lexem.DIV)) {
                currExp = new CheckedDivide(currExp, fourthLevelToken());
//            } else if (test(Lexem.PERCENT)) {
//                currExp = new CheckedDivide(currExp, fourthLevelToken());
            } else {
                return currExp;
            }
        }
    }

    private boolean test(final Lexem expected) throws ParsingException {
        if (currLexem == expected) {
            nextLexem();
            return true;
        } else {
            return false;
        }
    }

    private MyExpression fourthLevelToken() throws ParsingException {
        MyExpression currExp = null;
        if (currLexem == Lexem.COUNT) {
            nextLexem();
            currExp = new Count(fourthLevelToken());
        } else if (currLexem == Lexem.NEGATE) {
            nextLexem();
            currExp = new CheckedNegate(fourthLevelToken());
        } else if (currLexem == Lexem.LOG) {
            nextLexem();
            currExp = new CheckedLog(fourthLevelToken());
        } else if (currLexem == Lexem.POW) {
            nextLexem();
            currExp = new CheckedPow(fourthLevelToken());
        } else {
            currExp = fifthLevelToken();
        }
        return currExp;
    }

    private MyExpression fifthLevelToken() throws ParsingException {
        final MyExpression currExp;
        if (currLexem == Lexem.CONST) {
            currExp = new Const(Integer.parseInt(constValue));
        } else if (currLexem == Lexem.VAR) {
            currExp = new Variable(varName);
        } else if (currLexem == Lexem.RIGHT_BRACKET && bracketBalance <= 0) {
            throw new ParsingException("Missing bracket");
        } else if (currLexem == Lexem.LEFTBRACKET) {
            bracketBalance++;
            nextLexem();
            currExp = firstLevelToken();
            if (currLexem != Lexem.RIGHT_BRACKET) {
                throw new ParsingException("Expected bracket at position " + lexemStartPos);
            } else {
                bracketBalance--;
            }
        } else {
            throw new ParsingException("Expected operand at position " + lexemStartPos);
        }
        nextLexem();
        return currExp;
    }
}
