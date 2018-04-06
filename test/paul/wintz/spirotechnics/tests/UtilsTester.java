package paul.wintz.spirotechnics.tests;

import static java.lang.Math.floor;

import java.util.*;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.junit.Assert.*;

import paul.wintz.math.Fraction;
import paul.wintz.utils.*;

public final class UtilsTester {

	private static final double TOLERANCE = 0.0001;

	@Test
	public void testMin() {
		assertEquals(3, Utils.min(5, 65, 76, 34, 76, 3, 5));
	}

	@Test
	public void testLeastCommonMultiple() {
		final int[] a = { 0, 1, -1, 1, 2, 3 };
		final int[] b = { 1, 1, -1, 2, 5, 6 };
		final int[] c = { 2, 1, 1, 3, 3, 1 };
		final int[] result = { 0, 1, 1, 6, 30, 6 };

		for (int i = 0; i < a.length; i++) {
			assertEquals(result[i], Utils.lcm(a[i], b[i], c[i]));
		}
	}

	@Test
	public void testGreatestCommonDivisor() {
		final int[] a = { 0, 0, 1, -1, 3, 30, 4 };
		final int[] b = { 0, 1, 1, -1, 3, 15, 6 };
		final int[] c = { 0, 2, 1, 1, 3, 40, 2 };
		final int[] result = { 0, 1, 1, 1, 3, 5, 2 };

		for (int i = 0; i < a.length; i++) {
			assertEquals(String.format("gcd(%d, %d, %d) != %d", a[i], b[i], c[i], result[i]), result[i],
					Utils.gcd(a[i], b[i], c[i]));
		}
	}

	@Test
	public void testFractionsToMultipliedIntegers() {
		// {1, 3/2, 1/2, 10/12, 6/5} -> {120, 180, 90, 150, 180}
		// TODO: (Check example calculation)

		final int[] numerators = 	{1, 2, 3, 4};
		final int[] denominators = 	{1, 1, 1, 1};

		for (int test = 0; test < 100; test++) {

			final Fraction[] fractions = Fraction.integersToFraction(numerators, denominators, true);

			final int[] result = Fraction.fractionsToMultipliedIntegers(fractions);

			validateFractionRatios(fractions, result);
		}
	}

	private void validateFractionRatios(final Fraction[] fractions, final int[] result) {
		//Goal: I[1] = I[0] * F[0],

		for (int i = 1; i < result.length; i++) {
			final float expected = result[i-1] * fractions[i-1].getValue();
			//			final float fractionFromPrevious = (float) result[i] / (float) result[i - 1];
			assertEquals("index " + i + " failed.\n"
					+ "Fraction array: " + Arrays.toString(fractions) + "\n"
					+ "Integer array: " + Arrays.toString(result) + "\n",
					expected,
					result[i], 0.01);
		}
	}

	@Test
	public void testRandomFractionsToMultipliedIntegers() {
		// {1, 3/2, 1/2, 10/12, 6/5} -> {120, 180, 90, 150, 180} TODO: (Check
		// example calculation)

		for (int test = 0; test < 100; test++) {
			final int n = 10;
			final int low = -5;
			final int high = 5;
			final int[] numerators = Utils.randomNonZeroIntegerArray(n, low, high);
			final int[] denominators = Utils.randomNonZeroIntegerArray(n, low, high);

			final Fraction[] fractions = Fraction.integersToFraction(numerators, denominators, true);

			final int[] result = Fraction.fractionsToMultipliedIntegers(fractions);

			validateFractionRatios(fractions, result);
		}
	}

	@Test
	public void testFractionsToIntegers() {
		final int numerator1 = 1;
		final int denominator1 = 2;
		final int numerator2 = 2;
		final int denominator2 = 3;
		final int[] expected = { numerator1 * denominator2, denominator1 * numerator2 };//This does not work in most other cases
		verifyToFractionsToIntegers(expected, numerator1, denominator1, numerator2, denominator2);

	}

	private void verifyToFractionsToIntegers(int[] expected, int numerator1, int denominator1, int numerator2, int denominator2) {
		final Fraction[] fractions = { new Fraction(numerator1, denominator1, false), new Fraction(numerator2, denominator2, false) };

		final int[] result = Fraction.fractionsToIntegers(fractions);

		assertArrayEquals(expected, result);
	}

	@Test
	public void testTrigFunctionsDerivativeFunction() {
		final Utils.TrigFunction f = Utils.TrigFunction.SINE;

		assertEquals("Zeroth derivative.", f.derivativeFunction(0), Utils.TrigFunction.SINE);
		assertEquals("First derivative.", f.derivativeFunction(1), Utils.TrigFunction.COSINE);
		assertEquals("Second derivative.", f.derivativeFunction(2), Utils.TrigFunction.NEGATIVE_SINE);
		assertEquals("Third derivative.", f.derivativeFunction(3), Utils.TrigFunction.NEGATIVE_COSINE);
		assertEquals("Fourth derivative.", f.derivativeFunction(4), Utils.TrigFunction.SINE);
	}

	@Test
	public void testTrigFunctionsDerivativeValues() {
		final Utils.TrigFunction f = Utils.TrigFunction.COSINE;

		for (double x = -100; x < 100; x += 9.9) {
			for (double a = -100; a < 100; a += 9.9) {
				assertEquals("Zeroth derivative.", f.evaluateDerivative(x, a, 0, 0), Math.cos(a * x), 0.01);
				assertEquals("First derivative.", f.evaluateDerivative(x, a, 1, 0), -a * Math.sin(a * x), 0.01);
				assertEquals("Second derivative.", f.evaluateDerivative(x, a, 2, 0), -a * a * Math.cos(a * x), 0.01);
				assertEquals("Third derivative.", f.evaluateDerivative(x, a, 3, 0), a * a * a * Math.sin(a * x), 0.01);
				assertEquals("Fourth derivative.", f.evaluateDerivative(x, a, 4, 0), a * a * a * a * Math.cos(a * x), 0.01);
			}
		}
	}

	@Test
	public void testRandomInteger() {
		final int testCount = 20;

		final int[] values = { 0, -10, 10 };

		for (final Integer low : values) {
			for (final Integer high : values) {
				for (int i = 0; i < testCount; i++) {
					for(final Boolean isAllowZero : Arrays.asList(true, false)) {
						if(high <= low) continue;
						final int randInt = Utils.randomInteger(low, high, isAllowZero);
						assertFalse("Random integer is equal to zero when not allowed!", !isAllowZero && randInt == 0);
						assertFalse("Random integer is less than low!", randInt < low);
						assertFalse("Random integer is greater than or equal to high!", randInt >= high);
						
					}
				}
			}
		}
	}

	@Test
	public void testRandomHash() throws Exception {

		Pattern hashPattern = Pattern.compile("^[a-f|\\d]{8}$");

		Set<String> hashes = new HashSet<>();

		for(int i = 0; i < 100; i++) {
			String hash = Utils.getRandomHash();

			boolean isValid = hashPattern.matcher(hash).matches();
			assertTrue("The hash '" + hash + " is not valid", isValid);

			assertTrue("the hash '" + hash + "' was already in " + hashes, hashes.add(hash));
		}
	}



	@Test
	public void testModulus() {

		// int modulus
		assertEquals(1, Utils.modulus(-3, 2));
		assertEquals(0, Utils.modulus(-2, 2));
		assertEquals(1, Utils.modulus(-1, 2));
		assertEquals(0, Utils.modulus(0, 2));
		assertEquals(1, Utils.modulus(1, 2));
		assertEquals(0, Utils.modulus(2, 2));
		assertEquals(1, Utils.modulus(3, 2));

		assertEquals(0, Utils.modulus(0, 3));
		assertEquals(2, Utils.modulus(-1, 3));
		assertEquals(1, Utils.modulus(-2, 3));
		assertEquals(0, Utils.modulus(-3, 3));

		// double modulus
		assertEquals(1, Utils.modulus(-1.0, 2.0), TOLERANCE);

		// Example of how the modulus method differs form the % operator
		assertEquals(-1, -1 % 2, TOLERANCE);

	}

	@Test
	public void testShiftedModulus() {

		assertEquals(1.0, Utils.shiftedModulus(1.0, 0.0, 2.0), TOLERANCE);
		assertEquals(1.0, Utils.shiftedModulus(1.0, 1.0, 2.0), TOLERANCE);
		assertEquals(1.0, Utils.shiftedModulus(1.0, 1.0, 3.0), TOLERANCE);
		assertEquals(0.0, Utils.shiftedModulus(-1.0, 0.0, 1.0), TOLERANCE);
		assertEquals(0.0, Utils.shiftedModulus(-2.0, 0.0, 1.0), TOLERANCE);
		assertEquals(1.0, Utils.shiftedModulus(-1.0, 0.0, 2.0), TOLERANCE);
		assertEquals(0.0, Utils.shiftedModulus(-2.0, 0.0, 2.0), TOLERANCE);

		for(double d = -30; d < 30; d += 0.847) {
			assertEquals(d - floor(d), Utils.shiftedModulus(d, 0.0, 1.0), TOLERANCE);
		}

	}

	@Test
	public void testMap() throws Exception {

		assertEquals(50, Utils.map(0.5, 0, 1, 0, 100), TOLERANCE);
		assertEquals(50, Utils.map(1.5, 1, 2, 0, 100), TOLERANCE);
		assertEquals(-50, Utils.map(-0.5, 0, 1, 0, 100), TOLERANCE);
		assertEquals(-50, Utils.map(0.5, 1, 2, 0, 100), TOLERANCE);
		assertEquals(-50, Utils.map(0.5, 0, 1, 0, -100), TOLERANCE);
		assertEquals(150, Utils.map(0.5, 0, 1, 100, 200), TOLERANCE);
		assertEquals(150, Utils.map(1.5, 0, 1, 0, 100), TOLERANCE);
		assertEquals(50, Utils.map(-0.5, 0, 1, 100, 200), TOLERANCE);

	}

}
