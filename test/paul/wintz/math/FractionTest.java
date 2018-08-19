package paul.wintz.math;

import static org.apache.commons.math3.util.ArithmeticUtils.gcd;

import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import paul.wintz.math.Fraction;
import paul.wintz.utils.Utils;

public class FractionTest {
    private static final int RANDOM_RANGE = 100;
    Random rand = new Random();

    @Test
    public void testReducedFractions() {
        final int[] numerators = { 1, 52, 7, 9, 4, -3, -3, -1, -9, -100, 0, 0 };
        final int[] denominator = { 4, 1, 5, -1, 6, -3, 2, 5, -21, 10, -6, 1 };
        final int[] expectedNumerator = { 1, 52, 7, -9, 2, 1, -3, -1, 3, -10, 0, 0 };
        final int[] expectedDenominator = { 4, 1, 5, 1, 3, 1, 2, 5, 7, 1, 1, 1 };

        for (int i = 0; i < numerators.length; i++) {
            final Fraction f = new Fraction(numerators[i], denominator[i], true);
            assertEquals("numerators are not equal! i = " + i, expectedNumerator[i], f.getNumerator());
            assertEquals("denominators are not equal! i = " + i, expectedDenominator[i], f.getDenominator());
            assertEquals("Values are not equal! i = " + i, (double) numerators[i] / (double) denominator[i], f.getValue(), 0.001);
        }
    }

    @Test
    public void testFractionIntIntBoolean() {

        for (int i = 0; i < 1000; i++) {
            final int num = rand.nextInt(RANDOM_RANGE);
            final int den = rand.nextInt(RANDOM_RANGE);
            if (den == 0) {
                continue;
            }

            final Fraction fraction = new Fraction(num, den, false);
            final Fraction fractionReduced = new Fraction(num, den, true);


            final double errorDelta = 0.001;
            assertEquals("The value of the fractions are not equal", (double) num / (double) den, fraction.getValue(),
                    errorDelta);
            assertEquals("The value of the reduced fraction does not match the unreduced fraction", fraction.getValue(),
                    fractionReduced.getValue(), errorDelta);

            final boolean isReduced = gcd(fractionReduced.getNumerator(), fractionReduced.getDenominator()) == 1;
            assertTrue("The reduced fraction '" + fractionReduced.toString() + "' is not reduced", isReduced);
        }
    }

    @Test
    public void testFractionDouble() {
        for (int i = 0; i < 1000; i++) {
            final int num = rand.nextInt(RANDOM_RANGE) + 1;
            final int den = rand.nextInt(RANDOM_RANGE) + 1;

            final double value = (double) num / (double) den;
            Fraction fraction;
            try {
                fraction = new Fraction(value);
            } catch (final Exception e) {
                System.err.printf("%d / %d = %.3f\n", num, den, value);
                System.err.println(e.getMessage());
                continue;
            }

            final double errorDelta = 0.001;
            assertEquals("The value of the fractions are not equal", value, fraction.getValue(), errorDelta);

            final boolean isReduced = gcd(fraction.getNumerator(), fraction.getDenominator()) == 1;
            assertTrue("The reduced fraction '" + fraction.toString() + "' is not reduced", isReduced);
        }
    }

    @Test
    public void testSetValue() {
        final Fraction fraction = new Fraction(1);
        for (int i = 0; i < 1000; i++) {
            final int num = rand.nextInt(RANDOM_RANGE) + 1;
            final int den = rand.nextInt(RANDOM_RANGE) + 1;

            final double value = (double) num / (double) den;

            fraction.setValue(value);

            final double errorDelta = 0.001;
            assertEquals("The value of the fractions are not equal", (double) num / (double) den, fraction.getValue(),
                    errorDelta);

            final boolean isReduced = gcd(fraction.getNumerator(), fraction.getDenominator()) == 1;
            assertTrue("The reduced fraction '" + fraction.toString() + "' is not reduced", isReduced);
        }
    }

    @Test
    public void integerSeriesToFractions() {
        final int[] array = { 1, 2, -4, -2, 5 };
        final int[] expectedNumerators = { 2, -2, 1, -5 };
        final int[] expectedDenominator = { 1, 1, 2, 2 };

        final Fraction[] fractions = Fraction.integerSeriesToFractions(array);
        for (int i = 0; i < fractions.length; i++) {
            assertEquals("Numerators do not match!", expectedNumerators[i], fractions[i].getNumerator());
            assertEquals("Denominators do not match!", expectedDenominator[i], fractions[i].getDenominator());
        }
    }

    @Test
    public void testConstructorUnreduced() {

        for (int i = 0; i < 300; i++) {
            final int n = Utils.randomInteger(-100, 100);
            final int d = Utils.randomNonZeroInteger(-100, 100);
            System.out.printf("%d / %d", n, d);
            final double value = (double) n / (double) d;

            final Fraction f = new Fraction(n, d, false);

            assertEquals("The value of the faction does not equal the value used to create it.", value, f.getValue(),
                    0.001);
            assertEquals("The numerator does not match what was given!", n, f.getNumerator());
            assertEquals("The denominator does not match what was given!", d, f.getDenominator());
        }
    }

    // @Test
    // public void testAdd() {
    // fail("Not yet implemented");
    // }

}
