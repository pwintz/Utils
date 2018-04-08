package paul.wintz.uioptiontypes;

import static com.google.common.base.Preconditions.*;

import paul.wintz.uioptiontypes.integers.NumberOption;

public class FloatOption extends NumberOption<Float> {

    private FloatOption(String description, float initial, float min, float max, ValidityEvaluator<Float> validityEvaluator) {
        super(description, min, max);
        if(validityEvaluator != null) {
            setValidityEvaluator(validityEvaluator);
        }
        setValue(initial);
    }

    public final void setValue(int value) {
        super.setValue((float) value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private float initialValue;
        private float maxValue = Float.MAX_VALUE;
        private float minValue = -Integer.MAX_VALUE;
        private ValidityEvaluator<Float> validityEvaluator;
        private String description = "";

        public final Builder initialValue(float initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public final Builder setMaxValue(float maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public final Builder setMinValue(float minValue) {
            this.minValue = minValue;
            return this;
        }

        public final Builder range(float min, float max) {
            this.minValue = min;
            this.maxValue = max;
            return this;
        }

        public final Builder description(String description) {
            this.description = checkNotNull(description);
            return this;
        }

        public final Builder setValidityEvaluator(ValidityEvaluator<Float> validityEvaluator) {
            checkState(this.validityEvaluator == null);
            this.validityEvaluator = validityEvaluator;
            return this;
        }

        public final Builder prohibitZero() {
            checkState(validityEvaluator == null);
            validityEvaluator = value -> value != 0;
            return this;
        }

        public final FloatOption build() {
            checkValues();
            return new FloatOption(description, initialValue, minValue, maxValue, validityEvaluator);
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

