package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.*;

public class IntegerOption extends NumberOption<Integer> {
    private final long numberOfValuesInRange;

    public IntegerOption(Builder builder) {
        super(builder);
        numberOfValuesInRange = builder.max - builder.min + 1L;
    }

    public long getNumberOfValuesInRange() {
        return numberOfValuesInRange;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends NumberOption.Builder<Integer, Builder> {

        private Builder() {
            initial = 0;
            increment = 1;
            max = Integer.MAX_VALUE;
            min = -Integer.MAX_VALUE;
        }

        public final Builder prohibitZero() {
            addValidityEvaluator(value -> value != 0);
            return this;
        }

        public final Builder prohibitNonPositive() {
            if(min < 1) min(1);
            addValidityEvaluator(value -> value > 0);
            return this;
        }

        public final Builder prohibitNegative() {
            if(min < 0) min(0);
            addValidityEvaluator(value -> value >= 0);
            return this;
        }

        public final IntegerOption build() {
            checkValues();
            return new IntegerOption(this);
        }

        protected final void checkValues() {
            super.checkValues();
            checkState(initial >= min, "initial value %s is less than min value %s", initial, min);
            checkState(initial <= max, "initial value %s is greater than max value %s", initial, max);
        }
    }
}
