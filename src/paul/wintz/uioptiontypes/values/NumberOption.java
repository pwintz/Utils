package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class NumberOption<T extends Number> extends ValueOption<T> {

    public final T min;
    public final T max;
    public final T increment;

    protected NumberOption(Builder<T, ?> builder) {
        super(builder);
        builder.checkValues();
        min = checkNotNull(builder.min);
        max = checkNotNull(builder.max);
        increment = checkNotNull(builder.increment);
    }

    public T getMin() { return min; }
    public T getMax() { return max; }

    public T getIncrement() { return increment; }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends Number, B extends ValueOption.Builder> extends ValueOption.Builder<T, B> {

        protected T increment;
        protected T max;
        protected T min;

        public final B increment(T increment) {
            this.increment = increment;
            return (B) this;
        }

        /**
         * If max is set multiple times, then the smallest of the values will be used.
         * @param max an upper bound for the value of the option
         * @return this builder
         */
        public final B max(T max) {
            if(max.doubleValue() < this.max.doubleValue()) {
                this.max = max;
            }
            return (B) this;
        }

        /**
         * If min is set multiple times, then the largest of the values will be used.
         * @param min a lower bound for the value of the option
         * @return this builder
         */
        public final B min(T min) {
            if(min.doubleValue() > this.min.doubleValue()) {
                this.min = min;
            }
            return (B) this;
        }

        public final B range(T min, T max) {
            min(min);
            max(max);
            return (B) this;
        }

        private void checkValues() {
            checkValue(min, "min");
            checkValue(max, "max");
        }

        protected Builder() {
            // Prevent instantiation
        }

        T getIncrement() {
            return increment;
        }

        T getMax() {
            return max;
        }

        T getMin() {
            return min;
        }

    }

}