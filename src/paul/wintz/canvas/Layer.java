package paul.wintz.canvas;

import java.util.List;

import paul.wintz.math.Vector2D;

public interface Layer<L> {

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

	// Set the persistent state of the layers

	void setScale(float scale);
	void setSize(int width, int height);
	void setCenter(float centerX, float centerY);
	void setRotation(float angle);

	int getWidth();
	int getHeight();
	float getScale();


	void handleNewFrame();
	void clear();
	void background(Painter painter);

	L getImage();
	void drawOnto(L target);

	void line(float x0, float y0, float x1, float y1, Painter painter);
	void endpointToEndpoint(Vector2D center, Vector2D tracer, Painter painter);

	void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter);

	void circle(float x, float y, float radius, Painter painter);

	void dot(float x, float y, float radius, Painter painter);
	void dot(Vector2D tracer, float radius, Painter painter);

	//Complex Shapes
	void rectangle(float x, float y, float width, float height, Painter painter);
	void rectangle(float x, float y, float width, float height, Painter painter, List<Transformation> transforms);

	void ellipse(float xCenter, float yCenter, float width, float height, Painter painter);
	void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, List<Transformation> transforms);

	void quad(Vector2D corner0, Vector2D corner1, Vector2D corner2, Vector2D corner3, Painter painter);

	void drawPath(final List<Vector2D> points, final Painter painter);
	void drawPath(final List<Vector2D> points, final Painter painter, final List<Transformation> transforms);

	void drawPolygon(final List<Vector2D> points, final Painter painter);
	void drawPolygon(final List<Vector2D> points, final Painter painter, List<Transformation> transforms);

}
