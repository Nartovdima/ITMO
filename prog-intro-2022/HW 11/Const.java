import java.util.Objects;

public class Const implements MyExpression {
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
        if (value instanceof Integer intVal) {
            return Integer.toString(intVal);
        }
        if (value instanceof Double dblVal) {
            return Double.toString(dblVal);
        }
        return null;
    }

    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const that) {
            return Objects.equals(this.getValue(), that.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (value instanceof Integer intVal) {
            return Integer.hashCode(intVal);
        }
        if (value instanceof Double dblVal) {
            return Double.hashCode(dblVal);
        }
        return 0;
    }

    @Override
    public int evaluate(int value) {
        return this.value.intValue();
    }

    @Override
    public double evaluate(double value) {
        return this.value.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return evaluate(x);
    }
}
