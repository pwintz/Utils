package paul.wintz.uioptiontypes.values;

public class IntegerOption extends NumberOption<Integer> {
    private final long numberOfValuesInRange;

    protected IntegerOption(Builder builder) {
        super(builder);
        numberOfValuesInRange = (long) builder.max - (long) builder.min + 1L;
    }

    public long getNumberOfValuesInRange() {
        return numberOfValuesInRange;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends NumberOption.Builder<Integer, Builder> {

        protected Builder() {
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

        public IntegerOption build() {
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
