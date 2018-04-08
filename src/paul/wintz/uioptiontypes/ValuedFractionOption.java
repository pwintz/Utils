package paul.wintz.uioptiontypes;

import paul.wintz.math.Fraction;
import paul.wintz.utils.*;

public class ValuedFractionOption extends FractionOption {
    private final Fraction v;

    public ValuedFractionOption(String description) {
        super(description);
        v = new Fraction(Utils.randomInteger(1, 6), Utils.randomNonZeroInteger(1, 6), true);
    }

    @Override
    public int getNumerator() {
        return v.getNumerator();
    }

    @Override
    public int getDenominator() {
        return v.getDenominator();
    }

    @Override
    public void setNumerator(int newValue) {
        v.setNumerator(newValue);
    }

    @Override
    public void setDenominator(int newValue) {
        v.setDenominator(newValue);
    }

}