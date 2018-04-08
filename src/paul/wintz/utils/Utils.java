package paul.wintz.utils;

import static com.google.common.base.Preconditions.*;
import static java.lang.Double.isFinite;
import static java.lang.Math.*;

import java.io.File;
import java.util.*;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.ArithmeticUtils;

import com.google.common.primitives.Ints;

import paul.wintz.utils.exceptions.UnhandledCaseException;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Utils {
    public static final double TAU = 2 * PI;
    public static final Random random = new Random();
    public static final char PHI = '\u03D5';
    public static final char PSI = '\u03C8';
    private static final String NOT_POSITIVE_FORMAT = "%s is not positive";

    public static int min(int... values) {
        if (values.length == 0)
            throw new IllegalArgumentException("No arguments were passed.");
        int minValue = Integer.MAX_VALUE;
        for (final int v : values) {
            if (v < minValue) {
                minValue = v;
            }
        }
        return minValue;
    }

    // RANDOM GENERATORS
    public static int[] randomIntegerArray(int n, int low, int high) {
        return randomIntegerArray(n, low, high, true);
    }

    public static int[] randomNonZeroIntegerArray(int n, int low, int high) {
        return randomIntegerArray(n, low, high, false);
    }

    private static int[] randomIntegerArray(int n, int low, int high, boolean allowZeroValues) {
        final int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = randomInteger(low, high, allowZeroValues);
        }

        return a;
    }

    public static int randomInteger(int low, int high){
        return randomInteger(low, high, true);
    }

    public static int randomNonZeroInteger(int low, int high){
        return randomInteger(low, high, false);
    }

    /**
     * @param allowZeroValue If false, low and high cannot both equal zero.
     * @return a value equal to or greater than low, and less than high
     *         (non-inclusive). [low, high)
     * @throws IllegalArgumentException if low is equal to or greater than high.
     */
    public static int randomInteger(int low, int high, boolean allowZeroValue) {
        checkArgument(low < high, "Illegal range. Low value must be less than high value.");

        int i;
        do {
            int range = Utils.checkPositive(Math.abs(high - low));
            i = random.nextInt(range) + low;
        } while (!allowZeroValue && i == 0);

        return i;
    }

    public static String getRandomHash() {
        return String.format("%08x", Utils.randomInteger(0, Integer.MAX_VALUE));
    }

    /*
     * public static int[] fractionsToIntegers(Fraction[] fractions) throws
     * ArithmeticException {
     *
     * int N = fractions.length; int[] numerators = new int[N]; int[]
     * denominators = new int[N];
     *
     * for(int i = 0; i < N; i++){ numerators[i] = fractions[i].getNumerator();
     * denominators[i] = fractions[i].getDenominator(); }
     *
     * int lcm = lcd(denominators);
     *
     * int[] result = new int[N];
     *
     * for(int i = 0; i < N; i++){ result[i] = numerators[i] * lcm /
     * denominators[i]; } return result; }
     */

    /*
     * public static int[] fractionsToIntegers(Fraction[] fractions){
     *
     * int N = fractions.length; int[] numerators = new int[N]; int[]
     * denominators = new int[N];
     *
     * for(int i = 0; i < N; i++){ numerators[i] = fractions[i].getNumerator();
     * denominators[i] = fractions[i].getDenominator(); }
     *
     * int lcm = lcd(denominators);
     *
     * int[] result = new int[N]; for(int i = 0; i < N; i++){ result[i] =
     * numerators[i] * lcm / denominators[i]; } return result; }
     */

    /**
     * The Sine function scaled and shifted so that it ranges from 0.0 to 1.0,
     * and has a period of 1.0.
     *
     */
    public static float normalizedSin(double x) {
        return normalizedSin(x, 0.0, 1.0);
    }

    /**
     * The Sine function scaled and shifted so that it ranges from min to max,
     * and has a period of 1.0.
     */
    public static float normalizedSin(double x, double min, double max) {
        final double range = max - min;
        return (float) (range * (0.5 + 0.5 * sin(x * TAU)) + min);
    }

    /**
     * The Cosine function scaled and shifted so that its value ranges from min
     * to max, and has a period of 1.0.
     */
    public static float normalizedCos(double x, double min, double max) {
        final double range = max - min;
        return (float) (range * (0.5 + 0.5 * cos(x * TAU)) + min);
    }

    /**
     * Calculates the least common multiple of multiple values. If one of the
     * values is zero, than the result is zero.
     * @throws MathArithmeticException if any of the values are 0.
     */
    public static int lcm(int... values) {
        return lcm(Ints.asList(values));
    }
    
    /**
     * Calculates the least common multiple of multiple values. If one of the
     * values is zero, than the result is zero.
     */
    public static int lcm(List<Integer> values) {
        int lcm = 1;
        for (Integer value : values) {
            lcm = ArithmeticUtils.lcm(lcm, value);
        }
        return lcm;
    }

    /**
     * Calculates the greatest common denominator of multiple values
     * @throws MathArithmeticException if any of the values are zero
     */
    public static int gcd(int... values) {
        int gcd = values[0];
        for (int i = 1; i < values.length; i++) {
            gcd = ArithmeticUtils.gcd(gcd, values[i]);
        }
        return gcd;
    }

    public static String formatPeriodText(float period) {
        if (period / TAU < 1)
            return "frequency: " + (TAU / period);
        else
            return "period: " + (period / TAU);
    }

    public enum TrigFunction {
        SINE, COSINE, NEGATIVE_SINE, NEGATIVE_COSINE;

        public double value(double x) {
            switch (this) {
            case SINE:
                return sin(x);
            case COSINE:
                return cos(x);
            case NEGATIVE_SINE:
                return -sin(x);
            case NEGATIVE_COSINE:
                return -cos(x);
            }

            throw new UnhandledCaseException(this);
        }

        public TrigFunction derivativeFunction(int derivativeOrder) {
            return TrigFunction.values()[(ordinal() + derivativeOrder) % 4];
        }

        /**
         * Calculates the derivative of the trigonometric function. If this ==
         * SINE, the the value of this would be n = 0: sin(a * x) n = 1: a *
         * cos(a * x) n = 2: a^2 * -sin(a * x)
         *
         * @param x
         *            The the value of the variable
         * @param a
         *            A constant multiplied by the variable within the trig
         *            function.
         * @param derivativeDegree
         *            The order of the derivative. E.g. first derivative, second
         *            derivative, etc.
         * @return the evaluated value of the derivative.
         */
        public double evaluateDerivative(double x, double a, int derivativeDegree) {
            return evaluateDerivative(x, a, derivativeDegree, 0);
        }

        /**
         * Calculates the derivative of the trigonometric function. If this ==
         * SINE, the the value of this would be n = 0: sin(a * x) n = 1: a *
         * cos(a * x) n = 2: a^2 * -sin(a * x)
         *
         * @param x
         *            The the value of the variable
         * @param a
         *            A constant multiplied by the variable within the trig
         *            function.
         * @param derivativeDegree
         *            The order of the derivative. E.g. first derivative, second
         *            derivative, etc.
         * @param angleOffset the amount the angle is rotated from zero
         * @return the evaluated value of the derivative.
         */
        public double evaluateDerivative(double x, double a, int derivativeDegree, double angleOffset) {
            return Math.pow(a, derivativeDegree) * derivativeFunction(derivativeDegree).value(a * x + angleOffset);
        }
    }

    public static double randomDouble() {
        return random.nextDouble();
    }

    public static void assertNonNull(Object... objs){
        for(final Object o : objs){
            checkNotNull(o);
        }
    }

    // FIXME: use base two values for sizes?
    public static String fileSize(File f) {

        // Equals the number of non-leading digits. i.e. 1000 = 3, 999 = 2
        final int nonLeadDigits = (int) Math.log10(f.length());
        if (nonLeadDigits < 3)
            return String.format("%.1f bytes", (double) f.length());
        else if (nonLeadDigits < 6)
            return String.format("%.1f kilobytes", f.length() / Math.pow(2, 10));
        else if (nonLeadDigits < 9)
            return String.format("%.1f megabytes", f.length() / Math.pow(2, 20));
        else
            return String.format("%.1f gigabytes", f.length() / Math.pow(2, 30));
    }

    /**
     * The same as the mathematical definition of modulus.
     */
    public static int modulus(int a, int m) {
        return floorMod(a, checkPositive(m));
    }

    public static double modulus(double x, double m) {
        final double quotient = x / checkPositive(m);
        final double remainder = quotient - Math.floor(quotient);
        return remainder * m;
    }

    public static double shiftedModulus(double value, double lowerBound, double upperBound){
        checkArgument(lowerBound < upperBound);

        final double range = upperBound - lowerBound;
        final double valueFromLower = value - lowerBound;

        final double remainder = modulus(valueFromLower, range);

        return remainder + lowerBound;
    }

    public static double clipToRange(double d, double min, double max) {
        if(d < min) return min;
        if(d > max) return max;
        return d;
    }

    public static float clipToRange(float f, float min, float max) {
        if(f < min) return min;
        if(f > max) return max;
        return f;
    }

    private Utils() {
        // Don't allow instantiation
    }

    public static void checkAlmostInteger(double value) {
        checkState(abs(value - round(value)) < 0.0001, "The value of %s is not almost an integer", value);
    }

    public static int checkPositive(int i) {
        checkArgument(i > 0, NOT_POSITIVE_FORMAT, i);
        return i;
    }

    public static double checkPositive(double x) {
        checkArgument(x > 0, NOT_POSITIVE_FORMAT, x);
        return x;
    }

    public static float checkPositive(float x) {
        checkArgument(x > 0, NOT_POSITIVE_FORMAT, x);
        return x;
    }

    public static double checkFinite(double x) {
        checkArgument(isFinite(x), "%s is not finite", x);
        return x;
    }

    /**
     * @param start (inclusive)
     * @param end (inclusive)
     */
    public static void checkInRange(double value, double start, int end) {
        checkArgument(0 <= value && value <= 1, "The value %s is not in the range [%s, %s]", value, start, end);
    }

    /**Updates the averagedSpectrum array by setting each element
     * to a weighted average of its current value and the
     * present value.
     *//*
    private void updateAveragedSpectrum(){
        float currentFrameWeight = 0.7f;

        for(int i = LOWEST_INDEX; i < HIGHEST_INDEX; i++){
            amplitudesAveraged[i - LOWEST_INDEX] = weightedAverage(currentFrameWeight, amplitudesAveraged[i - LOWEST_INDEX], grid.getAmplitude(0, i));
        }
    }*/

    public static float weightedAverage(float weight, float a, float b){
        return (weight * a + (1 - weight) * b);
    }

    public static double log2(double x) {
        return (float) (Math.log(x) / Math.log(2));
    }

    public static float log2(float x) {
        return (float) (Math.log(x) / Math.log(2));
    }

    public static double sq(double x) {
        return x * x;
    }

    public static double map(double value, double inStart, double inStop, double outStart, double outStop) {
        double slope = (outStop - outStart) / (inStop - inStart);
        return slope * (value - inStart) + outStart;
    }

}
