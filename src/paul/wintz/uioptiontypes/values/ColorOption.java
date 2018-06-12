package paul.wintz.uioptiontypes.values;

import paul.wintz.utils.color.ColorUtils;

import static com.google.common.base.Preconditions.checkState;

public class ColorOption extends ValueOption<Integer> {

    private static final int COLOR_RANGE = 256;
    public final int red;
    public final int green;
    public final int blue;
    public final int alpha;

    private ColorOption(Builder builder) {
        super(builder);
        red = builder.red;
        green = builder.green;
        blue = builder.blue;
        alpha = builder.alpha;
    }

    public void emitViewValueChanged(int red, int green, int blue, int alpha) {
        super.emitViewValueChanged(ColorUtils.rgba(red, green, blue, alpha));
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public static class Builder extends ValueOption.Builder<Integer, Builder> {

        protected int red;
        protected int green;
        protected int blue;
        protected int alpha = 255;

        @Override
        public Builder initial(Integer initial) {
            this.initial = initial;
            red(ColorUtils.red(initial));
            green(ColorUtils.green(initial));
            blue(ColorUtils.blue(initial));
            alpha(ColorUtils.alpha(initial));
            return this;
        }

        public final Builder gray(int gray){
            red(gray);
            green(gray);
            blue(gray);
            return this;
        }

        public final Builder red(int red) {
            this.red = red;
            return this;
        }

        public final Builder green(int green) {
            this.green = green;
            return this;
        }

        public final Builder blue(int blue) {
            this.blue = blue;
            return this;
        }

        public final Builder alpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        protected void checkValues() {
            super.checkValues();
            checkChannel(red);
            checkChannel(green);
            checkChannel(blue);
            checkChannel(alpha);
        }

        private static void checkChannel(int channel){
            checkState(0 <= channel && channel < COLOR_RANGE);
        }

        public ColorOption build() {
            initial(ColorUtils.rgba(red, green, blue, alpha));
            return new ColorOption(this);
        }

        protected Builder() {
            // Prevent instantiation
        }

    }

}