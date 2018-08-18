package paul.wintz.uioptiontypes.values;

import paul.wintz.math.Fraction;

public class FractionOption extends ValueOption<Fraction> {

    private FractionOption(Builder builder) {
        super(builder);
//        new Fraction(
//                builder.numerator,
//                builder.denominator,
//                false);
    }

//    public int getNumerator() {
//        return getValue().getNumerator();
//    }
//
//    public int getDenominator() {
//        return getValue().getDenominator();
//    }
//
//    public void setNumerator(int numerator) {
//        getValue().setNumerator(numerator);
//    }
//
//    public void setDenominator(int newValue) {
//        getValue().setDenominator(newValue);
//    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<Fraction, Builder> {

//        private int numerator;
//        private int denominator;
//
//        public Builder numerator(int numerator) {
//            this.numerator = numerator;
//            return this;
//        };
//
//        public Builder denominator(int denominator) {
//            this.denominator = denominator;
//            return this;
//        }

        public FractionOption build() {
            return new FractionOption(this);
        }

        private Builder() {
            // prohibit external instantiation
        }

    }

}
