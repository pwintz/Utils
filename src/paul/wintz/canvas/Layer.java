package paul.wintz.canvas;

import java.util.*;

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

	void line(float x0, float y0, float x1, float y1, Painter painter);

	void endpointToEndpoint(Vector2D center, Vector2D tracer, Painter painter);

	void ellipse(float xCenter, float yCenter, float width, float height, Painter painter,
			Queue<Transformation> transforms);

	void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle,
			Painter painter);

	void circle(float x, float y, float radius, Painter painter);

	void dot(float x, float y, float radius, Painter painter);

	void dot(Vector2D tracer, float radius, Painter painter);

	void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Painter painter);

	void quad(Vector2D left0, Vector2D left1, Vector2D right1, Vector2D right0, Painter tracerPainter);

	void drawPath(List<Vector2D> points, Painter painter, Queue<Transformation> transforms);

	void drawPolygon(List<Vector2D> points, Painter painter, Queue<Transformation> transforms);

	void handleNewFrame();

	void clear();

	void background(Painter painter);

	int getWidth();

	int getHeight();

	double getScale();

	L getImage();

	void drawOnto(L target);

}
