package paul.wintz.canvas;

import static java.lang.Math.max;

public class Painter {
	private boolean isFilled;
	private int fill;
	private boolean isStroked;
	private float strokeWeight;
	private int stroke;

	public Painter setStroke(int color, float weight) {
		setOnlyStroked();

		stroke = color;
		strokeWeight = max(0, weight);

		return this;
	}

	public Painter setFill(int color) {
		setOnlyFilled();

		fill = color;

		return this;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		if (isFilled) {
			sb.append("Fill: ").append(fill);
		}
		if (isFilled && isStroked) {
			sb.append(", ");
		}
		if (isStroked) {
			sb.append("Stroke: ").append(stroke).append(", StrokeWeight: ").append(strokeWeight);
		}
		return sb.toString();
	}

	public final boolean isFilled() {
		return isFilled;
	}

	public final void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	public final boolean isStroked() {
		return isStroked;
	}

	public final void setStroked(boolean isStroked) {
		this.isStroked = isStroked;
	}

	public final void setOnlyFilled() {
		isFilled = true;
		isStroked = false;
	}

	public final void setOnlyStroked() {
		isFilled = false;
		isStroked = true;
	}

	public final float getStrokeWeight() {
		return strokeWeight;
	}

	public final void setStrokeWeight(float strokeWeight) {
		if (strokeWeight < 0) {
			this.strokeWeight = 0;
		} else {
			this.strokeWeight = strokeWeight;
		}
	}

	public final int getStroke() {
		return stroke;
	}

	public final void setStroke(int stroke) {
		this.stroke = stroke;
	}

	public final int getFill() {
		return fill;
	}
}
