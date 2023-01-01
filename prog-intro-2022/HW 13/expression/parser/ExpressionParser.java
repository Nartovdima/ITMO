package expression.parser;

import expression.*;

public class ExpressionParser implements TripleParser {
    private Lexem currLexem;
    private String constValue;
    private String expression;
    private int currPos;
    private String varName;
    @Override
    public TripleExpression parse(String expression) {
        currPos = 0;
        this.expression = expression + "@";
        currLexem = null;
        getLexem();
        return firstLevelToken();
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

    private void getLexem() {
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
                    //TODO throw exception default:
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
                    //TODO throw exception default:
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
            case '@' : // END
                currLexem = Lexem.END;
                break;
            default : // CONST or Invalid lexem
                if (isNumber()) {
                    currLexem = Lexem.CONST;
                    currPos += constValue.length() - 1;
                }
                break;
        }
        currPos++;
    }

    private MyExpression firstLevelToken() {
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
        return currExp;
    }

    private MyExpression secondLevelToken() {
        MyExpression currExp = thirdLevelToken();
        while (currLexem == Lexem.ADD || currLexem == Lexem.SUB) {
            if (currLexem == Lexem.ADD) {
                getLexem();
                currExp = new Add(currExp, thirdLevelToken());
            }
            if (currLexem == Lexem.SUB) {
                getLexem();
                currExp = new Subtract(currExp, thirdLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression thirdLevelToken() {
        MyExpression currExp = fourthLevelToken();
        while (currLexem == Lexem.MUL || currLexem == Lexem.DIV) {
            if (currLexem == Lexem.MUL) {
                getLexem();
                currExp = new Multiply(currExp, fourthLevelToken());
            }
            if (currLexem == Lexem.DIV) {
                getLexem();
                currExp = new Divide(currExp, fourthLevelToken());
            }
        }
        return currExp;
    }

    private MyExpression fourthLevelToken() {
        MyExpression currExp = null;
        if (currLexem == Lexem.COUNT) {
            getLexem();
            currExp = new Count(fourthLevelToken());
        } else if (currLexem == Lexem.NEGATE) {
            getLexem();
            currExp = new Negate(fourthLevelToken());
        } else {
            currExp = fifthLevelToken();
        }
        return currExp; // TODO throw exception
    }

    private MyExpression fifthLevelToken() {
        MyExpression currExp = new Add(new Const(3), new Const(5));
        if (currLexem == Lexem.CONST) {
            currExp = new Const(Integer.parseInt(constValue));
        } else if (currLexem == Lexem.VAR) {
            currExp = new Variable(varName);
        } else if (currLexem == Lexem.LEFTBRACKET) {
            getLexem();
            currExp = firstLevelToken();
        }
        getLexem();
        return currExp;
    }
}
