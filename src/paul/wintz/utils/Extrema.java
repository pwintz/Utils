package paul.wintz.utils;

public class Extrema {
	private double min = Double.MAX_VALUE;
	private double max = -Double.MAX_VALUE;

	public void update(final double value) {
		if (value > max)
			max = value;
		if (value < min)
			min = value;
	}

	public double normalize(final double value) {
		return (value - min) / (max - min);
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