package paul.wintz.canvas;

import paul.wintz.math.Vector2D;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static paul.wintz.utils.RegexUtils.INTEGER_REG_EX;

/**
 * This is a mock class to verify that calls are correctly made to Layers.
 */
public class MockLayer extends Layer<Void> {

	private final Queue<DrawingAction> recordedDrawingActions = new LinkedList<>();

	public MockLayer(int size) {
		super(size, size);
	}

	public MockLayer(int width, int height) {
		super(width, height);
	}

	private static class DrawingAction {
		private final String name;
		private final List<String> coordinates;

		private static final Pattern coordinatePattern = Pattern.compile("^" + INTEGER_REG_EX + "x" + INTEGER_REG_EX + "$");

		public DrawingAction(String name, String... coordinates){
			this.name = name;
			this.coordinates = Arrays.asList(coordinates);
			this.coordinates.forEach(coord -> checkArgument(coordinatePattern.matcher(coord).matches(),
													"coord: " + coord + " does not match regEx"));
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof DrawingAction))
				return false;
			DrawingAction other = (DrawingAction) obj;

			if(!this.name.equals(other.name)) return false;
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
	private String asPixelCoordinates(float x, float y) {
		int pixelX = (int) (getCenterX() + x * getScaleX());
		int pixelY = (int) (getCenterY() + y * getScaleY());
		return pixelX + "x" + pixelY;
	}

	private String asPixelCoordinates(Vector2D v) {
		return asPixelCoordinates((float) v.x(), (float) v.y());
	}

	private String[] asPixelCoordinatesArray(List<Vector2D> points) {
		String[] coordinates = new String[points.size()];
		for(int i = 0; i < points.size(); i++) {
			coordinates[i] = asPixelCoordinates(points.get(i));
		}
		return coordinates;
	}

	@Override
	protected Void createImageObject() {
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
	public void fill(int fillColor) {
		addRecordedAction("background");
	}

    @Override
	public void drawOnto(Void target) {
		addRecordedAction("drawOnto");
	}

	@Override
	public void line(float x0, float y0, float x1, float y1, Painter painter) {
		addRecordedAction("line", asPixelCoordinates(x0, y0), asPixelCoordinates(x1, y1));
	}

	@Override
	public void line(Vector2D start, Vector2D end, Painter painter) {
		addRecordedAction("line", asPixelCoordinates(start), asPixelCoordinates(end));
	}

	@Override
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle,
			Painter painter) {
		addRecordedAction("arc", asPixelCoordinates(xCenter, yCenter));
	}


	@Override
	public void circle(Vector2D center, float radius, Painter painter) {
		addRecordedAction("circle", asPixelCoordinates(center));
	}

	@Override
	public void circle(float x, float y, float radius, Painter painter) {
		addRecordedAction("circle", asPixelCoordinates(x, y));
	}

	@Override
	public void rectangle(float x, float y, float width, float height, Painter painter) {
		addRecordedAction("rectangle", asPixelCoordinates(x, y));
	}

	@Override
	public void rectangle(float x, float y, float width, float height, Painter painter,
			List<Transformation> transforms) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter) {
		addRecordedAction("ellipse", asPixelCoordinates(xCenter, yCenter));
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter,
			List<Transformation> transforms) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void quad(Vector2D corner0, Vector2D corner1, Vector2D corner2, Vector2D corner3, Painter painter) {
		addRecordedAction("quad", asPixelCoordinates(corner0), asPixelCoordinates(corner1), asPixelCoordinates(corner2), asPixelCoordinates(corner3));
	}

	@Override
	public void drawPath(List<Vector2D> points, Painter painter) {
		addRecordedAction("drawPath", asPixelCoordinatesArray(points));
	}

	@Override
	public void drawPath(List<Vector2D> points, Painter painter, List<Transformation> transforms) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawPolygon(List<Vector2D> points, Painter painter) {
		addRecordedAction("drawPolygon", asPixelCoordinatesArray(points));
	}

	@Override
	public void drawPolygon(List<Vector2D> points, Painter painter, List<Transformation> transforms) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawText(String text, int x, int y) {
		throw new UnsupportedOperationException();
	}

}
