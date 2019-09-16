package paul.wintz.uioptiontypes.values;

import com.google.common.base.MoreObjects;

public class StringOption extends ValueOption<String> {

    protected StringOption(Builder<?> builder) {
        super(builder);
    }

    public static Builder<StringOption.Builder> builder() {
        return new Builder<>();
    }

    public static class Builder <B extends Builder> extends ValueOption.Builder<String, B> {

        public StringOption build() {
            return new StringOption(this);
        }

        protected Builder() {
            initial = "";
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("initial", initial)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", getValue())
                .toString();
    }
}
