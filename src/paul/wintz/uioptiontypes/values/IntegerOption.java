package paul.wintz.uioptiontypes.values;

public class IntegerOption extends NumberOption<Integer> {
    private final long numberOfValuesInRange;

    private IntegerOption(Builder builder) {
        super(builder);
        numberOfValuesInRange = (long) builder.max - (long) builder.min + 1L;
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
            min = Integer.MIN_VALUE;
        }

        public final Builder prohibitZero() {
            addValidityEvaluator(value -> value != 0);
            return this;
        }

        public final Builder prohibitNonPositive() {
            min(1);
            return this;
        }

        public final Builder prohibitNegative() {
            min(0);
            return this;
        }

        public final IntegerOption build() {
            addValidityEvaluator(value -> value >= min);
            addValidityEvaluator(value -> value <= max);
            return new IntegerOption(this);
        }

    }

    @Override
    public String toString() {
        return String.format("IntegerOption{initial=%d, range=[%d, %d], increment=%d]}", getValue(), min, max, increment);
    }
}
