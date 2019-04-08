package paul.wintz.math;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.ArithmeticUtils;
import paul.wintz.utils.Utils;
import paul.wintz.utils.exceptions.UnimplementedMethodException;

import java.util.Random;

/**
 * There are two modes, reduced and non-reduced.</br>
 * </br>
 * In reduced mode, then any common factors between the numerator and
 * denominator of a fraction be divided out.</br>
 * If they both are negative, then they will both become positive.</br>
 * If they have opposite signs, then the sign will be moved to the
 * numerator. </br>
 * Reduction is handled on a method by method basis, and is not stored for a
 * specific Fraction object.
 *
 *
 * @author PaulWintz
 *
 */
public class Fraction implements Cloneable {
    private static final Random random = new Random();
    private static final int MAX_DENOMINATOR = 300;
    private int numerator;

    // denominator cannot be set to zero
    private int denominator;

    /**
     * Creates a fraction from the given numerator and denominator.
     * @param makeReducedFraction
     *            if isReducedFraction is true, and the value of the fraction is
     *            negative, then the negative sign will be put in the numerator.
     */
    public Fraction(int numerator, int denominator, boolean makeReducedFraction) {
        if (denominator == 0) {
            throw new ZeroDenominatorException();
        }

        if (makeReducedFraction) {
            final int gcd = ArithmeticUtils.gcd(numerator, denominator);

            // make sure negative sign is in the numerator.
            // This works whether both or only the denominator has a negative
            // sign.
            if (denominator < 0) {
                numerator = -numerator;
                denominator = -denominator;
            }
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        } else {
            this.numerator = numerator;
            this.denominator = denominator;
        }

    }

    public Fraction(double value) {
        setValue(value);
    }

    public void setValue(double value) {
        final BigFraction fraction = new BigFraction(value, MAX_DENOMINATOR);
        setNumerator(fraction.getNumeratorAsInt());

        // If value is zero, than set the denominator to 1. Otherwise it is set
        // to 0 and causes trouble.
        setDenominator((value == 0.0) ? 1 : fraction.getDenominatorAsInt());
    }

    public float getValue() {
        return (float) numerator / (float) denominator;
    }

    public boolean isMultiplicativeIdentity() {
        return numerator == denominator && denominator != 0;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) throws ZeroDenominatorException {
        if (denominator == 0)
            throw new ZeroDenominatorException();
        this.denominator = denominator;
    }

    public static Fraction add(Fraction a, Fraction b) {
        throw new UnimplementedMethodException();
        // Fraction result;
        //
        // int denominatorLCD = lcm(a.denominator, b.denominator);
        //
        // int aNumerator = a.numerator * denominatorLCD / b.denominator;
        // int bNumerator = b.numerator * denominatorLCD / a.denominator;
        //
        //// result = new Fraction(aNumerator, bDenominator, isReducedFraction)
        // return null;
    }

    public static Fraction random() {
        return random(5, 5);
    }

    public static Fraction random(int maxNum, int maxDen) {
        int num;
        int den;
        do {
            num = random.nextInt(maxNum);
            den = random.nextInt(maxDen);
        } while (den == 0 || num == den);

        // randomize the polarity
        if (random.nextBoolean()) {
            num = -num;
        }

        return new Fraction(num, den, true);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Object clone() {
        return new Fraction(numerator, denominator, false);
    }

    public static Fraction[] randomFractions(int n) {
        return randomFractions(n, false);
    }

    public static Fraction[] randomFractions(int n, boolean firstIsOne) {
        final Fraction[] fractions = new Fraction[n];

        for (int i = 0; i < n; i++) {
            fractions[i] = random();
        }
        if (firstIsOne) {
            fractions[0] = new Fraction(1, 1, false);
        }

        return fractions;
    }

    public static Fraction[] randomFractions(int n, boolean firstIsOne, boolean forSpiroParams) {
        final Fraction[] fractions = new Fraction[n];

        for (int i = 0; i < n; i++) {
            do {
                fractions[i] = random();
            } while (fractions[i].isMultiplicativeIdentity() || fractions[i].isAdditiveIdentity());
        }
        if (firstIsOne) {
            fractions[0] = new Fraction(1, 1, false);
        }

        return fractions;
    }

    public boolean isAdditiveIdentity() {
        return numerator == 0;
    }

    /**
     * Converts an array of fractions to an array of values such that the
     * ratio from one integer to the next is equal to the fraction
     * </br></br>
     * For a given array of Fractions, F, the resulting array of values, I,
     * will be as follows:
     * </br>
     * (1) I[i] = I[i-1] * F[i-1]</br>
     *
     * (2) The lowest value of I[0] is chosen such that: </br>
     * I[1] = I[0] * F[0], </br>
     * I[2] = I[0] * F[0] * F[1], </br>
     * ... , </br>
     * I[i] = I[0] * F[0] * F[1] * ... * F[i-1]
     *
     * @throws ArithmeticException if any of the denominators are zero.
     */
    public static int[] fractionsToMultipliedIntegers(Fraction[] fractions) throws ArithmeticException {
        final int N = fractions.length + 1;
        final int[] numerators = new int[N];
        final int[] denominators = new int[N];

        numerators[0] = 1;
        denominators[0] = 1;
        for (int i = 0; i < N - 1; i++) {
            numerators[i + 1] = numerators[i] * fractions[i].getNumerator();
            denominators[i + 1] = denominators[i] * fractions[i].getDenominator();
        }

        // least common multiple of the absolute values of the denominators
        final int lcmDenominators = Utils.lcm(denominators);

        final int[] result = new int[N];

        for (int i = 0; i < N; i++) {
            result[i] = numerators[i] * (lcmDenominators / denominators[i]);
        }

        return result;
    }

    public static int[] fractionsToIntegers(Fraction[] fractions) throws ArithmeticException {

        final int N = fractions.length;
        final int[] numerators = new int[N];
        final int[] denominators = new int[N];

        for (int i = 0; i < N; i++) {
            numerators[i] = fractions[i].getNumerator();
            denominators[i] = fractions[i].getDenominator();
        }

        final int lcm = Utils.lcm(denominators);

        final int[] result = new int[N];

        for (int i = 0; i < N; i++) {
            result[i] = numerators[i] * lcm / denominators[i];
            // if(i > 0) result[i] *= result[i-1];
        }
        return result;
    }

    public static Fraction[] integersToFraction(int[] numerators, int[] denominators, boolean makeReducedFractions) {
        final int n = numerators.length;
        if (n != denominators.length)
            throw new IllegalArgumentException("The length of the numerator and denominator arrays do not match");

        final Fraction[] fractions = new Fraction[n];

        for (int i = 0; i < n; i++) {
            fractions[i] = new Fraction(numerators[i], denominators[i], makeReducedFractions);
        }

        return fractions;
    }

    /**
     * Converts a string of values into a series of reduced fractions that are
     * equal to the ratios between the numbers. The length of the array will be
     * one less than the number of given values. For example: [2,4,3,7] -> [
     * 2/1, 3/4, 7/3]. </br>
     * </br>
     * When successive numbers have opposite signs, then the fraction will have
     * a negative value in the numerator.
     *
     */
    public static Fraction[] integerSeriesToFractions(int[] array) {

        final Fraction[] fractions = new Fraction[array.length - 1];

        for (int i = 0; i < fractions.length; i++) {
            fractions[i] = new Fraction(array[i + 1], array[i], true);
        }

        return fractions;
    }

    @SuppressWarnings("serial")
    class ZeroDenominatorException extends ArithmeticException {
        private ZeroDenominatorException() {
            super(String.format("Denominator is zero in the fraction: %s", Fraction.this.toString()));
        }
    }

    @Override
    public String toString() {
        return getNumerator() + "/" + getDenominator();
    }
}