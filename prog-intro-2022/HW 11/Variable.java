package expression;

import java.util.Objects;

public class Variable implements MyExpression {
    private final String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }

    @Override
    public String toMiniString() {
        return varName;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    public double evaluate(double value) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int result = 0;
        switch (varName) {
            case "x" -> result = x;
            case "y" -> result = y;
            case "z" -> result = z;
        }
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable that) {
            return Objects.equals(this.getVarName(), that.getVarName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getVarName().hashCode();
    }
}