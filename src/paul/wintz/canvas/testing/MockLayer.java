package paul.wintz.canvas.testing;

import static com.google.common.base.Preconditions.checkArgument;
import static paul.wintz.utils.RegexUtils.INTEGER_REG_EX;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

import paul.wintz.canvas.*;
import paul.wintz.math.Vector2D;
import paul.wintz.utils.exceptions.UnimplementedMethodException;

/**
 * This is a mock class to verify that calls are correctly made to Layers.
 */
public class MockLayer extends Layer<Void> {

	private final Queue<DrawingAction> recordedDrawingActions = new LinkedList<>();

	public MockLayer(int width, int height) {
		super(width, height);
	}

	private static class DrawingAction {
		private final String name;
		private final List<String> coordinates;

		private static final Pattern coordPattern = Pattern.compile("^" + INTEGER_REG_EX + "x" + INTEGER_REG_EX + "$");

		public DrawingAction(String name, String... coordinates){
			this.name = name;
			this.coordinates = Arrays.asList(coordinates);
			this.coordinates.forEach(coord -> checkArgument(coordPattern.matcher(coord).matches()));
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof DrawingAction))
				return false;
			DrawingAction other = (DrawingAction) obj;

			if(this.name != other.name) return false;
			if(this.coordinates.size() != other.coordinates.size()) return false;
			return this.coordinates.containsAll(other.coordinates);
		}

		@Override
		public String toString() {
			return name + ": " + coordinates;
		}

	}

	//Coordinates must be in the form "12x345"
	private void addRecordedAction(String name, String... coordinates) {
		recordedDrawingActions.add(new DrawingAction(name, coordinates));
	}

	public void assertEqualToRecorded(String name, String... coordinates) {
		DrawingAction expected = new DrawingAction(name, coordinates);
		DrawingAction actual = recordedDrawingActions.poll();
		assertEquals(expected, actual);
	}

	//NOTE: This rounds down the pixel coordinate and does not account for rotation.
	private String asPixelCoord(float x, float y) {
		int pixelX = (int) (getCenterX() + x * getScaleX());
		int pixelY = (int) (getCenterY() + y * getScaleY());
		return pixelX + "x" + pixelY;
	}

	private String asPixelCoord(Vector2D v) {
		return asPixelCoord((float) v.x(), (float) v.y());
	}

	private String[] asPixelCoordsArray(List<Vector2D> points) {
		String[] coords = new String[points.size()];
		for(int i = 0; i < points.size(); i++) {
			coords[i] = asPixelCoord(points.get(i));
		}
		return coords;
	}

	@Override
	protected Void createLayer() {
		// Do not record this method with addRecordedAction() because it is
		// called in the constructor before the recorded actions list is created.
		return null;
	}

	@Override
	public void handleNewFrame() {
		addRecordedAction("handleNewFrame");
	}

	@Override
	public void clear() {
		addRecordedAction("clear");
	}

	@Override
	public void background(Painter painter) {
		addRecordedAction("background");
	}

	@Override
	public Void getImage() {
		addRecordedAction("getImage");
		return null;
	}

	@Override
	public void drawOnto(Void target) {
		addRecordedAction("drawOnto");
	}

	@Override
	public void line(float x0, float y0, float x1, float y1, Painter painter) {
		addRecordedAction("line", asPixelCoord(x0, y0), asPixelCoord(x1, y1));
	}

	@Override
	public void endpointToEndpoint(Vector2D start, Vector2D end, Painter painter) {
		addRecordedAction("endpointToEndpoint", asPixelCoord(start), asPixelCoord(end));
	}

	@Override
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle,
			Painter painter) {
		addRecordedAction("arc", asPixelCoord(xCenter, yCenter));
	}

	@Override
	public void circle(float x, float y, float radius, Painter painter) {
		addRecordedAction("circle", asPixelCoord(x, y));
	}

	@Override
	public void dot(float x, float y, float radius, Painter painter) {
		addRecordedAction("dot", asPixelCoord(x, y));
	}

	@Override
	public void dot(Vector2D pos, float radius, Painter painter) {
		addRecordedAction("dot", asPixelCoord(pos));
	}

	@Override
	public void rectangle(float x, float y, float width, float height, Painter painter) {
		addRecordedAction("rectangle", asPixelCoord(x, y));
	}

	@Override
	public void rectangle(float x, float y, float width, float height, Painter painter,
			List<Transformation> transforms) {
		throw new UnimplementedMethodException();
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter) {
		addRecordedAction("ellipse", asPixelCoord(xCenter, yCenter));
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter,
			List<Transformation> transforms) {
		throw new UnimplementedMethodException();
	}

	@Override
	public void quad(Vector2D corner0, Vector2D corner1, Vector2D corner2, Vector2D corner3, Painter painter) {
		addRecordedAction("quad", asPixelCoord(corner0), asPixelCoord(corner1), asPixelCoord(corner2), asPixelCoord(corner3));
	}

	@Override
	public void drawPath(List<Vector2D> points, Painter painter) {
		addRecordedAction("drawPath", asPixelCoordsArray(points));
	}

	@Override
	public void drawPath(List<Vector2D> points, Painter painter, List<Transformation> transforms) {
		throw new UnimplementedMethodException();
	}

	@Override
	public void drawPolygon(List<Vector2D> points, Painter painter) {
		addRecordedAction("drawPolygon", asPixelCoordsArray(points));
	}

	@Override
	public void drawPolygon(List<Vector2D> points, Painter painter, List<Transformation> transforms) {
		throw new UnimplementedMethodException();
	}

}
