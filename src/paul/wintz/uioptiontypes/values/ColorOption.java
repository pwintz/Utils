package paul.wintz.uioptiontypes.values;

import paul.wintz.utils.color.ColorUtils;

import static com.google.common.base.Preconditions.checkArgument;

public class ColorOption extends ValueOption<Integer> {

    private static final int COLOR_RANGE = 256;

    private ColorOption(Builder builder) {
        super(builder);
    }

    public void emitViewValueChanged(int red, int green, int blue, int alpha) {
        super.emitViewValueChanged(ColorUtils.rgba(red, green, blue, alpha));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<Integer, Builder> {

        protected int red;
        protected int green;
        protected int blue;
        protected int alpha = 255;

        @Override
        public Builder initial(Integer initial) {
            red(ColorUtils.red(initial));
            green(ColorUtils.green(initial));
            blue(ColorUtils.blue(initial));
            alpha(ColorUtils.alpha(initial));
            return super.initial(initial);
        }

        public final Builder gray(int gray){
            red(gray);
            green(gray);
            blue(gray);
            return this;
        }

        public final Builder red(int red) {
            this.red = checkChannel(red);
            return this;
        }

        public final Builder green(int green) {
            this.green = checkChannel(green);
            return this;
        }

        public final Builder blue(int blue) {
            this.blue = checkChannel(blue);
            return this;
        }

        public final Builder alpha(int alpha) {
            this.alpha = checkChannel(alpha);
            return this;
        }

        private static int checkChannel(int channel){
            checkArgument(0 <= channel && channel < COLOR_RANGE);
            return channel;
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