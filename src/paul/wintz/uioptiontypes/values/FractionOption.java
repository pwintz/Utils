package paul.wintz.uioptiontypes.values;

import paul.wintz.math.Fraction;

public class FractionOption extends ValueOption<Fraction> {

    private FractionOption(Builder builder) {
        super(builder);
        fraction = new Fraction(builder.numerator, builder.denominator, false);
    }

    private final Fraction fraction;

    public int getNumerator() {
        return fraction.getNumerator();
    }

    public int getDenominator() {
        return fraction.getDenominator();
    }

    public void setNumerator(int numerator) {
        fraction.setNumerator(numerator);
    }

    public void setDenominator(int newValue) {
        fraction.setDenominator(newValue);
    }

    public static class Builder extends ValueOption.Builder<Fraction, Builder> {

        private int numerator;
        private int denominator;

        public Builder numerator(int numerator) {
            this.numerator = numerator;
            return this;
        };

        public Builder denominator(int denominator) {
            this.denominator = denominator;
            return this;
        }

        public FractionOption build() {
            return new FractionOption(this);
        }

    }

}
