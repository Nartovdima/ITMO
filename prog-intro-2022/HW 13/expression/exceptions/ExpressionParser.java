package expression.exceptions;

import expression.*;
import expression.exceptions.TripleParser;

public class ExpressionParser implements TripleParser {
    private Lexem currLexem;
    private String constValue;
    private String expression;
    private int currPos;
    private String varName;
    @Override
    public TripleExpression parse(String expression) throws ParsingException {
        currPos = 0;
        this.expression = expression + "\u0000";
        currLexem = null;
        getLexem();
        TripleExpression currRes = firstLevelToken();
        if (currLexem != Lexem.END) {
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
            while (Character.isJavaIdentifierPart(expression.charAt(pos))) {
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
        skipWhitespaces();
        switch (expression.charAt(currPos)) {
            case '+': // ADD
                currLexem = Lexem.ADD;
                break;
            case '-': // SUB, NEGATE
                if (currLexem == Lexem.RIGHTBRACKET || currLexem == Lexem.VAR || currLexem == Lexem.CONST) {
                    currLexem = Lexem.SUB;
                } else if (!Character.isDigit(expression.charAt(currPos + 1))) {
                    currLexem = Lexem.NEGATE;
                } else if (isNumber()) {
                    currLexem = Lexem.CONST;
                    currPos += constValue.length() - 1;
                }
                break;
            case '*': // MUL
                currLexem = Lexem.MUL;
                break;
            case '/': // DIV
                currLexem = Lexem.DIV;
                break;
            case 's': // SET or Invalid lexem
                switch (getIdentifier()) {
                    case "set":
                        currLexem = Lexem.SET;
                        currPos += 2;
                        break;
                    default:
                        throw new ParsingException("Unsupported identifier");
                }
                break;
            case 'c': // CLEAR, COUNT or Invalid lexem
                switch (getIdentifier()) {
                    case "clear":
                        currLexem = Lexem.CLEAR;
                        currPos += 4;
                        break;
                    case "count":
                        currLexem = Lexem.COUNT;
                        currPos += 4;
                        break;
                    default:
                        throw new ParsingException("Unsupported identifier");
                }
                break;
            case 'l' : // log10
                switch (getIdentifier()) {
                    case "log10":
                        currLexem = Lexem.LOG;
                        currPos += 4;
                        break;
                    default:
                        throw new ParsingException("Unsupported identifier");
                }
                break;
            case 'p' : // pow10
                switch (getIdentifier()) {
                    case "pow10":
                        currLexem = Lexem.POW;
                        currPos += 4;
                        break;
                    default:
                        throw new ParsingException("Unsupported identifier");
                }
                break;
            case 'x', 'y', 'z': // VAR
                currLexem = Lexem.VAR;
                varName = String.valueOf(expression.charAt(currPos));
                break;
            case '(' : // LEFTBRACKET
                currLexem = Lexem.LEFTBRACKET;
                break;
            case ')' : // RIGHTBRACKET
                currLexem = Lexem.RIGHTBRACKET;
                break;
            case '\u0000' : // END
                currLexem = Lexem.END;
                break;
            default : // CONST or Invalid lexem
                if (isNumber()) {
                    currLexem = Lexem.CONST;
                    currPos += constValue.length() - 1;
                } else {
                    throw new ParsingException("Unsupported identifier");
                }
                break;
        }
        currPos++;
    }

    private MyExpression firstLevelToken() throws ParsingException {
        MyExpression currExp = secondLevelToken();
        while (currLexem == Lexem.SET || currLexem == Lexem.CLEAR) {
            if (currLexem == Lexem.SET) {
                getLexem();
                currExp = new Set(currExp, secondLevelToken());
            }
            if (currLexem == Lexem.CLEAR) {
                getLexem();
                currExp = new Clear(currExp, secondLevelToken());
            }
        }
        if (currLexem != Lexem.END) {
            throw new ParsingException("Unsupported Identifier");
        }
        return currExp;
    }

    private MyExpression secondLevelToken() throws ParsingException {
        MyExpression currExp = thirdLevelToken();
        while (currLexem == Lexem.ADD || currLexem == Lexem.SUB) {
            if (currLexem == Lexem.ADD) {
                getLexem();
                if (
                       currLexem.getType() == 1 ||
                       currLexem.getType() == 2 ||
                       currLexem == Lexem.LEFTBRACKET
                ) {
                    currExp = new CheckedAdd(currExp, thirdLevelToken());
                } else {
                    throw new ParsingException("Expected number, bracket or unary operation");
                }
            }
            if (currLexem == Lexem.SUB) {
                getLexem();

                currExp = new CheckedSubtract(currExp, thirdLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression thirdLevelToken() throws ParsingException {
        MyExpression currExp = fourthLevelToken();
        while (currLexem == Lexem.MUL || currLexem == Lexem.DIV) {
            if (currLexem == Lexem.MUL) {
                getLexem();
                currExp = new CheckedMultiply(currExp, fourthLevelToken());
            }
            if (currLexem == Lexem.DIV) {
                getLexem();
                currExp = new CheckedDivide(currExp, fourthLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression fourthLevelToken() throws ParsingException {
        MyExpression currExp = null;
        if (currLexem == Lexem.COUNT) {
            getLexem();
            currExp = new Count(fourthLevelToken());
        } else if (currLexem == Lexem.NEGATE) {
            getLexem();
            currExp = new CheckedNegate(fourthLevelToken());
        } else if (currLexem == Lexem.LOG) {
            getLexem();
            currExp = new CheckedLog(fourthLevelToken());
        } else if (currLexem == Lexem.POW) {
            getLexem();
            currExp = new CheckedPow(fourthLevelToken());
        } else {
            currExp = fifthLevelToken();
        }
        if (currExp == null) {
            throw new ParsingException("Unsupported Identifier");
        }
        return currExp;
    }

    private MyExpression fifthLevelToken() throws ParsingException {
        MyExpression currExp = new Add(new Const(3), new Const(5));
        if (currLexem == Lexem.CONST) {
            currExp = new Const(Integer.parseInt(constValue));
        } else if (currLexem == Lexem.VAR) {
            currExp = new Variable(varName);
        } else if (currLexem == Lexem.LEFTBRACKET) {
            getLexem();
            currExp = firstLevelToken();
            if (currLexem != Lexem.RIGHTBRACKET) {
                throw new ParsingException("Expected bracket");
            }
        }
        getLexem();
        return currExp;
    }
}
