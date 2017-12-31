package paul.wintz.canvas;

import java.util.List;

import paul.wintz.math.Vector2D;

public abstract class Layer<L> {

	public interface Transformation {
	}

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

		layer = createLayer();
	}

	protected L layer;
	private int width;
	private int height;

	private float scaleX = 1;
	private float scaleY = 1;
	private float rotation = 0;
	private float centerX = 0;
	private float centerY = 0;

	//******************************************
	//* Set the persistent state of the layers.*
	//******************************************
	public void setScale(float scale) {
		setScaleX(scale);
		setScaleY(scale);
	}

	/**
	 * Set the center point as a fraction of the distance across the layer.
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

	//////////////////////////
	//      LAYER SIZE      //
	//////////////////////////

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		layer = createLayer();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	protected abstract L createLayer();

	public abstract void handleNewFrame();
	public abstract void clear();
	public abstract void background(Painter painter);

	public abstract L getImage();
	public abstract void drawOnto(L target);

	public abstract void line(float x0, float y0, float x1, float y1, Painter painter);
	public abstract void line(Vector2D start, Vector2D end, Painter painter);

	public abstract void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter);


	public abstract void rectangle(float x, float y, float width, float height, Painter painter);
	public abstract void rectangle(float x, float y, float width, float height, Painter painter, List<Transformation> transforms);

	public abstract void circle(float x, float y, float radius, Painter painter);
	public abstract void circle(Vector2D center, float radius, Painter painter);
	public abstract void ellipse(float xCenter, float yCenter, float width, float height, Painter painter);
	public abstract void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, List<Transformation> transforms);

	public abstract void quad(Vector2D corner0, Vector2D corner1, Vector2D corner2, Vector2D corner3, Painter painter);

	public abstract void drawPath(final List<Vector2D> points, final Painter painter);
	public abstract void drawPath(final List<Vector2D> points, final Painter painter, final List<Transformation> transforms);

	public abstract void drawPolygon(final List<Vector2D> points, final Painter painter);
	public abstract void drawPolygon(final List<Vector2D> points, final Painter painter, List<Transformation> transforms);

	@Override
	public String toString() {
		return String.format("Layer(%d x %d){scaleX=%.2f, scaleY=%.2f, rotation=%.2f}", width, height, scaleX, scaleY, rotation);
	}

}
