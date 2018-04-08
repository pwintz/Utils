package paul.wintz.uioptiontypes.integers;

import static com.google.common.base.Preconditions.*;

public class IntegerOption extends NumberOption<Integer> {
	private final long numberOfValuesInRange;

	private IntegerOption(String description, int value, int min, int max, ValidityEvaluator<Integer> validityEvaluator) {
		super(description, min, max);
		numberOfValuesInRange = max - min + 1L;

		if(validityEvaluator != null) {
			setValidityEvaluator(validityEvaluator);
		}

		setValue(value);
	}

	@Override
	public void setValue(Integer newValue) {
		super.setValue(sanitizeValue(newValue));
	}

	protected Integer sanitizeValue(Integer newValue) {
		int sanitizedValue = skipIllegalValue(newValue);

		if(sanitizedValue < min) {
			sanitizedValue = min;
		} else if(sanitizedValue > max) {
			sanitizedValue = max;
		}

		return sanitizedValue;
	}

	private Integer skipIllegalValue(Integer newValue) {
		if(!isValidValue(newValue)) {
			newValue += newValue - getValue();
		}
		return newValue;
	}

	public long getNumberOfValuesInRange() {
		return numberOfValuesInRange;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private int initialValue;
		private int maxValue = Integer.MAX_VALUE;
		private int minValue = -Integer.MAX_VALUE;
		private ValidityEvaluator<Integer> validityEvaluator;
		private String description = "";

		public final Builder initial(int initialValue) {
			this.initialValue = initialValue;
			return this;
		}

		public final Builder max(int maxValue) {
			this.maxValue = maxValue;
			return this;
		}

		public final Builder min(int minValue) {
			this.minValue = minValue;
			return this;
		}

		public final Builder range(int min, int max) {
			this.minValue = min;
			this.maxValue = max;
			return this;
		}

		public final Builder description(String description) {
			this.description = checkNotNull(description);
			return this;
		}

		public final Builder setValidityEvaluator(ValidityEvaluator<Integer> validityEvaluator) {
			checkState(this.validityEvaluator == null);
			this.validityEvaluator = validityEvaluator;
			return this;
		}

		public final Builder prohibitZero() {
			checkState(validityEvaluator == null);
			validityEvaluator = value -> value != 0;
			return this;
		}

		public final Builder prohibitNonPositive() {
			checkState(validityEvaluator == null);
			validityEvaluator = value -> value > 0;
			return this;
		}

		public final Builder prohibitNegative() {
			checkState(validityEvaluator == null);
			validityEvaluator = value -> value >= 0;
			return this;
		}

		public final IntegerOption build() {
			checkValues();
			return new IntegerOption(description, initialValue, minValue, maxValue, validityEvaluator);
		}

		private void checkValues() {
			checkState(initialValue >= minValue, "initial value %s is less than min value %s", initialValue, minValue);
			checkState(initialValue <= maxValue, "initial value %s is greater than max value %s", initialValue, maxValue);
			if(validityEvaluator != null) {
				checkState(validityEvaluator.isValid(initialValue), "initial value %s is invalid", initialValue);
				checkState(validityEvaluator.isValid(minValue), "min value %s is invalid", minValue);
				checkState(validityEvaluator.isValid(maxValue), "max value %s is invalid", maxValue);
			}
		}

		private Builder() {
			// Prevent instantiation
		}


	}
}
