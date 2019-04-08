package paul.wintz.utils;

/**
 * This class is NOT thread safe.
 */
public class Extrema {
    private double min = Double.MAX_VALUE;
    private double max = -Double.MAX_VALUE;

    public void update(final double measuredValue) {
        if (measuredValue > max)
            max = measuredValue;
        if (measuredValue < min)
            min = measuredValue;
    }

    public void clear() {
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}