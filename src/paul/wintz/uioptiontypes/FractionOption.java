package paul.wintz.uioptiontypes;

import paul.wintz.math.Fraction;

public abstract class FractionOption extends UserInputOption<Fraction> {

	public FractionOption(final String description) {
		super(description);
	}

	public abstract int getNumerator();

	public abstract int getDenominator();

	public abstract void setNumerator(int newValue);

	public abstract void setDenominator(int newValue);
}
