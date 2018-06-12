package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.checkState;

public class BooleanOption extends ValueOption<Boolean> {

    private BooleanOption(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<Boolean, Builder> {

        public BooleanOption build() {
            return new BooleanOption(this);
        }

        protected Builder() {
            // Prevent instantiation
        }

    }

}
