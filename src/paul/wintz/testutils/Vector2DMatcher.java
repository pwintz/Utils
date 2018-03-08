package paul.wintz.testutils;

import org.hamcrest.*;

import paul.wintz.math.Vector2D;

public final class Vector2DMatcher extends BaseMatcher<Vector2D> {
	private static final double MAX_DIFF = 0.000001;
	private final Vector2D expected;

	protected Vector2DMatcher(Vector2D expected) {
		this.expected = expected;
	}

	@Override
	public boolean matches(Object actual) {
		if(actual instanceof Vector2D) {
			return distanceFromExpected(actual) < MAX_DIFF;
		}
		return false;
	}

	private double distanceFromExpected(Object actual) {
		return differenceFromExpected(actual).magnitude();
	}

	private Vector2D differenceFromExpected(Object actual) {
		return Vector2D.diff(expected, (Vector2D) actual);
	}

	private double angledDifferenceFromExpected(Object actual) {
		return ((Vector2D) actual).direction() - expected.direction();
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		super.describeMismatch(item, description);
		
		description.appendText("\nThe distance between the vectors is ")
			.appendValue(distanceFromExpected(item));
		
		description.appendText("\nThe Cartesian difference between the vectors is ")
			.appendValue(differenceFromExpected(item));
		
		description.appendText("\nThe angle difference between the vectors is ")
			.appendValue(180.0 / Math.PI * angledDifferenceFromExpected(item))
			.appendText(" degrees");
	}

	public static Matcher<Vector2D> closeTo(Vector2D expected) {
		return new Vector2DMatcher(expected);
	}
}