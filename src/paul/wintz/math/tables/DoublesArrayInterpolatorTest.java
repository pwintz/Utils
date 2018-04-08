package paul.wintz.math.tables;

import static paul.wintz.math.tables.DoublesArrayInterpolator.positiveModulus;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import paul.wintz.math.tables.DoublesArrayInterpolator.EdgeBehavior;

public class DoublesArrayInterpolatorTest {


	@Test
	public final void testInterpolateMiddle() {

		final double[] to = {0, 1, 2, 3};

		final DoublesArrayInterpolator interpolator = new DoublesArrayInterpolator(0, 4, to);

		for(double d = 0.0; d < 3; d += 0.1){
			assertEquals(d, interpolator.interpolate(d), 0.00001);
		}

	}

	@Test
	public final void testGetIndexBefore() {

		final double[] to = {0, 1, 2, 3};

		final DoublesArrayInterpolator interpolator = new DoublesArrayInterpolator(0, 4, to);

		assertEquals(-1, interpolator.getIndexBefore(-0.5));
		assertEquals(0, interpolator.getIndexBefore(0));
		assertEquals(0, interpolator.getIndexBefore(0.1));
		assertEquals(3, interpolator.getIndexBefore(3));
		assertEquals(3, interpolator.getIndexBefore(3.5));


	}


	@Test
	public void testEdgesBehavior() {
		final int TOO_SMALL = -1;
		final int TOO_LARGE = 4;
		final double[] to = {0, 1, 2};

		final DoublesArrayInterpolator interpolator = new DoublesArrayInterpolator(0, 3, to);

		interpolator.setEdgeBehavior(EdgeBehavior.EXTEND_CONSTANT);
		myAssertEquals(0, interpolator.getValueAtIndex(TOO_SMALL));
		myAssertEquals(2, interpolator.getValueAtIndex(TOO_LARGE));

		interpolator.setEdgeBehavior(EdgeBehavior.EXTEND_LINEAR);
		myAssertEquals(-1, interpolator.getValueAtIndex(-1));
		myAssertEquals(4, interpolator.getValueAtIndex(4));
		myAssertEquals(-10, interpolator.getValueAtIndex(-10));
		myAssertEquals(40, interpolator.getValueAtIndex(40));

		interpolator.setEdgeBehavior(EdgeBehavior.WRAP);
		myAssertEquals(interpolator.interpolate(0), interpolator.interpolate(-3));
		myAssertEquals(interpolator.interpolate(0), interpolator.interpolate(0));
		myAssertEquals(interpolator.interpolate(0), interpolator.interpolate(3));
		myAssertEquals(interpolator.interpolate(0), interpolator.interpolate(6));
		myAssertEquals(interpolator.interpolate(0.5), interpolator.interpolate(6.5));
		myAssertEquals(interpolator.interpolate(2.5), interpolator.interpolate(5.5));

		interpolator.setEdgeBehavior(EdgeBehavior.ERROR);
		try{
			interpolator.getValueAtIndex(TOO_SMALL);
			fail("Should have thrown an exception");
		} catch(final Exception e){
			//Good.
		}
		try{
			interpolator.getValueAtIndex(TOO_LARGE);
			fail("Should have thrown an exception");
		} catch(final Exception e){
			//Good.
		}


	}

	@Test
	public void testDeltaTime() {
		final double[] to = {0, 1, 2, 3};
		final DoublesArrayInterpolator interpolator = new DoublesArrayInterpolator(0, 3, to);

		myAssertEquals(3.0 / 4.0, interpolator.getDeltaTime());

	}


	@Test
	public void testPositiveModulus() {
		assertEquals(0, positiveModulus(0, 3));
		assertEquals(2, positiveModulus(2, 3));
		assertEquals(2, positiveModulus(-1, 3));
		assertEquals(0, positiveModulus(-3, 3));
		assertEquals(0, positiveModulus(6, 3));

		try{
			assertEquals(-1, positiveModulus(-1, -3));
			fail("Should have thrown an exception");
		} catch(final IllegalArgumentException e){
			//good
		}
	}



	private static void myAssertEquals(double d, double result) {
		assertEquals(d, result, 0.00001);
	}

}














