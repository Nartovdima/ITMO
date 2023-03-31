package expression;

import java.util.Objects;

public class Const {
    private final Number value;

    public Const (int value) {
        this.value = value;
    }

    public Const (double value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const that) {
            return this.getValue().equals(that.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public int evaluate(int value) {
        return this.value.intValue();
    }

    public double evaluate(double value) {
        return this.value.doubleValue();
    }

    public int evaluate(int x, int y, int z) {
        return evaluate(x);
    }
}
