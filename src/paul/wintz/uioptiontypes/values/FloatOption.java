package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.*;

public class FloatOption extends NumberOption<Float> {

    private FloatOption (Builder builder){
        super(builder);
        builder.checkValues();
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
            increment = Float.NaN;
            min = Float.NEGATIVE_INFINITY;
            max = Float.POSITIVE_INFINITY;
        }

        public final Builder prohibitNonPositive() {
            if(min <= 0) {
                min(Float.MIN_VALUE); // i.e. the smallest positive number.
            }
            addValidityEvaluator(value -> value > 0f);
            return this;
        }

        public final FloatOption build() {
            addValidityEvaluator(value -> value >= min);
            addValidityEvaluator(value -> value <= max);
            return new FloatOption(this);
        }

        private void checkValues() {
            checkValue(min, "min");
            checkValue(max, "max");

            checkState(initial >= min, "initial value %s is less than min value %s", initial, min);
            checkState(initial <= max, "initial value %s is greater than max value %s", initial, max);
        }
    }
}

