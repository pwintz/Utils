package paul.wintz.uioptiontypes.values;

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
            initial = Boolean.FALSE;
        }

    }

}
