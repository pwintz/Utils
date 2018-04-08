package paul.wintz.math.tables;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import paul.wintz.math.tables.DoublesArrayInterpolator.EdgeBehavior;

public class InterpolationTableTest {

	@Test
	public final void testInterpolate() {

		final HashDoublesTable table = new HashDoublesTable(4);

		final double[] from = {0, 1, 2, 3};
		final double[] to   = {3, 4, 5, 6};

		final DoublesArrayInterpolator interpolator = new DoublesArrayInterpolator(0, 4, to);
		interpolator.setEdgeBehavior(EdgeBehavior.EXTEND_LINEAR);

		table.addUnsortedArray("to", to);

		for(int i = 0; i < table.getSteps(); i++){

			final double result = interpolator.interpolate(from[i]);
			final double expected = to[i];

			assertEquals(expected, result, 0.00001);
		}

		for(int i = 0; i < table.getSteps() - 1; i++){

			final double result = interpolator.interpolate(from[i] + 0.5);
			final double expected = to[i] + 0.5;

			assertEquals(expected, result, 0.00001);
		}
	}

}
