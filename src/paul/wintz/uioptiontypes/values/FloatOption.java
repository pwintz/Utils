package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.*;

public class FloatOption extends NumberOption<Float> {

    private FloatOption (Builder builder){
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(float initial) {
        return builder().initial(initial);
    }

    public static final class Builder extends NumberOption.Builder<Float, Builder> {

        private Builder() {
            // Prevent external instantiation and load defaults
            initial = 0f;
            increment = 1f;
            min = Float.NEGATIVE_INFINITY;
            max = Float.POSITIVE_INFINITY;
        }

        public final Builder prohibitNonPositive() {
            if(min <= 0) min(1f);
            addValidityEvaluator(value -> value > 0f);
            return this;
        }

        public final FloatOption build() {
            checkValues();
            return new FloatOption(this);
        }

        protected void checkValues() {
            super.checkValues();
            checkState(initial >= min, "initial value %s is less than min value %s", initial, min);
            checkState(initial <= max, "initial value %s is greater than max value %s", initial, max);
        }

    }
}

