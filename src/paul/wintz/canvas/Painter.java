package paul.wintz.canvas;

import java.util.Objects;

import static java.lang.Math.max;

public final class Painter {
    private boolean isFilled = true;
    private int fill;
    private boolean isStroked;
    private float strokeWeight;
    private int stroke;

    public final Painter setStroke(int color) {
        this.stroke = color;
        return this;
    }

    public Painter setStrokeAndWeight(int color, float weight) {
        setStroke(color);
        setStrokeWeight(weight);
        return this;
    }

    public Painter setFill(int color) {
        fill = color;
        return this;
    }

    public final boolean isFilled() {
        return isFilled;
    }

    public final Painter setFilled(boolean isFilled) {
        this.isFilled = isFilled;
        return this;
    }

    public final boolean isStroked() {
        return isStroked;
    }

    public final Painter setStroked(boolean isStroked) {
        this.isStroked = isStroked;
        return this;
    }

    public final Painter setOnlyFilled() {
        isFilled = true;
        isStroked = false;
        return this;
    }

    public final Painter setOnlyStroked() {
        isFilled = false;
        isStroked = true;
        return this;
    }

    public final float getStrokeWeight() {
        return strokeWeight;
    }

    public final Painter setStrokeWeight(float strokeWeight) {
        this.strokeWeight = Math.max(0, strokeWeight);
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
            sb.append("Stroke: ").append(stroke)
                    .append(", StrokeWeight: ").append(strokeWeight);
        }
        return sb.toString();
    }

    public final int getStroke() {
        return stroke;
    }

    public final int getFill() {
        return fill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Painter painter = (Painter) o;
        return isFilled == painter.isFilled &&
                fill == painter.fill &&
                isStroked == painter.isStroked &&
                Float.compare(painter.strokeWeight, strokeWeight) == 0 &&
                stroke == painter.stroke;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFilled, fill, isStroked, strokeWeight, stroke);
    }
}
