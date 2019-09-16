package paul.wintz.canvas;

import paul.wintz.math.Vector2D;

import java.util.Collections;
import java.util.List;

public abstract class Layer<I> {

    private static final List<Transformation> noTransforms = Collections.emptyList();

    public interface Transformation {}

    public static final class Rotation implements Transformation {
        public final float angle;

        public Rotation(float angle) {
            this.angle = angle;
        }
    }

    public static final class Translation implements Transformation {
        public final float x;
        public final float y;

        public Translation(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static final class Scale implements Transformation {
        public final float x;
        public final float y;

        public Scale(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public Layer(int width, int height) {
        this.width = width;
        this.height = height;

        image = createLayer();
    }

    private I image;
    private int width;
    private int height;

    private float scaleX = 1;
    private float scaleY = 1;
    private float rotation = 0;
    private float centerX = 0;
    private float centerY = 0;


    protected I getImage() {
        return image;
    }

    //******************************************
    //* Set the persistent state of the layers.*
    //******************************************
    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

    /**
     * Set the center point as a fraction of the distance across the image.
     * For example, for the vector (0, 0) to be displayed at the center of the
     * image, then call setCenter(0.5f, 0.5f).
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setRotation(float radians) {
        rotation = radians;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getAverageScale() {
        return (scaleX + scaleY) / 2f;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        image = createLayer();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected abstract I createLayer();

    public abstract void handleNewFrame();
    public abstract void clear();
    public abstract void background(Painter painter);

    public abstract void drawOnto(I target);

    public abstract void line(float x0, float y0, float x1, float y1, Painter painter);

    public void line(Vector2D start, Vector2D end, Painter painter) {
        line((float) start.x(), (float) start.y(), (float) end.x(), (float) end.y(), painter);
    }

    public abstract void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter);

    public void rectangle(float x, float y, float width, float height, Painter painter) {
        rectangle(x, y, width, height, painter, noTransforms);
    }
    public abstract void rectangle(float x, float y, float width, float height, Painter painter, List<Transformation> transforms);

    /**
     * Draw an empty circle.
     *
     * @param x x-coordinate of center of circle
     * @param y y-coordinate of center of circle
     */
    public void circle(float x, float y, float radius, Painter painter) {
        ellipse(x, y, (2 * radius), (2 * radius), painter, noTransforms);
    }

    public void vector2D(Vector2D startPos, Vector2D vector2D, Painter painter) {
        vector2D(startPos, vector2D, 1.0, painter);
    }

    public void vector2D(Vector2D startPos, Vector2D vector2D, double scale, Painter painter) {
        line((float) startPos.x(), (float) startPos.y(), (float) (startPos.x() + scale * vector2D.x()),
                (float) (startPos.y() + scale * vector2D.y()), painter);
    }

    public void circle(Vector2D center, float radius, Painter painter) {
        circle((float) center.x(), (float) center.y(), radius, painter);
    }
    public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter){
        ellipse(xCenter, yCenter, width, height, painter, noTransforms);
    }
    public abstract void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, List<Transformation> transforms);

    public abstract void quad(Vector2D corner0, Vector2D corner1, Vector2D corner2, Vector2D corner3, Painter painter);

    public void drawPath(List<Vector2D> points, Painter painter) {
        drawPath(points, painter, noTransforms);
    }
    public abstract void drawPath(final List<Vector2D> points, final Painter painter, final List<Transformation> transforms);

    public void drawPolygon(final List<Vector2D> points, final Painter painter) {
        drawPolygon(points, painter, noTransforms);
    }
    public abstract void drawPolygon(final List<Vector2D> points, final Painter painter, List<Transformation> transforms);

    public abstract void drawText(String text, int x, int y);

    @Override
    public String toString() {
        return String.format("Layer(%d x %d){scaleX=%.2f, scaleY=%.2f, rotation=%.2f}", width, height, scaleX, scaleY, rotation);
    }

}
