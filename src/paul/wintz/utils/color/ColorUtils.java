package paul.wintz.utils.color;

import paul.wintz.utils.exceptions.UnimplementedMethodException;

/**
 * Creates and evaluates colors. The format of the created ints are: [1 byte
 * alpha][1 byte red][1 byte green][1 byte blue]
 * @author PaulWintz
 *
 */

public class ColorUtils {
    private static final int SCALE = 255;
    private static final int ALPHA_BIT_SHIFT = 24;

    // COLOR DEFINITIONS
    public static final int WHITE = 0xffffffff;
    public static final int RED = 0xffff0000;
    public static final int YELLOW = 0xffffff00;
    public static final int GREEN = 0xff00ff00;
    public static final int CYAN = 0xff00ffff;
    public static final int BLUE = 0xff0000ff;
    public static final int BLACK = 0xff000000;
    public static final int TRANSPARENT = 0x00005000;

    // IMPORTANT HUES
    static final double RED_HUE = 0.0;
    static final double GREEN_HUE = 0.333333333;
    static final double BLUE_HUE = 0.666666667;

    private static final int BYTE_MASK = 0xff;
    private static final int RGB_MASK = 0xffffff;
    private static final int RED_BIT_SHIFT = 16;
    private static final int BLUE_BIT_SHIFT = 0;
    private static final int GREEN_BIT_SHIFT = 8;

    private ColorUtils() {
        // Do not instantiate.
    }

    private static int hsb(final double hue, final double saturation, final double brightness) {
        return java.awt.Color.HSBtoRGB(
                clipBetween0and1AsFloat(hue),
                clipBetween0and1AsFloat(saturation),
                clipBetween0and1AsFloat(brightness));
    }

    public static int hsba(final double hue, final double sat, final double black, final double alpha) {
        int hsb = hsb((float) hue, (float) sat, (float) black);
        return ColorUtils.rgba(hsb, (int)(SCALE*alpha));
    }

    public static int hue(int rgb) {
        throw new UnimplementedMethodException();
    }

    public static int saturation(int rgb) {
        throw new UnimplementedMethodException();
    }

    public static int brightness(int rgb) {
        throw new UnimplementedMethodException();
    }

    public static int lerpHsb(int color, int color2, float mixture) {
        throw new UnimplementedMethodException();
    }

    public static int rgb(final int red, final int green, final int blue){
        return (clipBetween0and255(red) << RED_BIT_SHIFT)
                + (clipBetween0and255(green) << GREEN_BIT_SHIFT)
                + (clipBetween0and255(blue) << BLUE_BIT_SHIFT)
                + (SCALE << ALPHA_BIT_SHIFT);
    }

    public static int rgba(final double red, final double green, final double blue, final double alpha){
        int alphaInt = clipBetween0and255((int) (255 * alpha)) << ALPHA_BIT_SHIFT;
        if(alphaInt == 0) {
            return TRANSPARENT;
        }
        return (clipBetween0and255((int)(255 * red)) << RED_BIT_SHIFT)
                + (clipBetween0and255((int)(255 * green)) << GREEN_BIT_SHIFT)
                + (clipBetween0and255((int)(255 * blue)) << BLUE_BIT_SHIFT)
                + alphaInt;
    }

    public static int rgba(final int red, final int green, final int blue, final int alpha){
        return (clipBetween0and255(red) << RED_BIT_SHIFT)
                + (clipBetween0and255(green) << GREEN_BIT_SHIFT)
                + (clipBetween0and255(blue) << BLUE_BIT_SHIFT)
                + (clipBetween0and255(alpha) << ALPHA_BIT_SHIFT);
    }

    public static int rgba(int rgb, int alpha) {
        int alphaInt = clipBetween0and255(alpha);
        if(alphaInt == 0) {
            // This is necessary to prevent a bug (possibly in the Processing library)
            // that displays some fully transparent colors (e.g. 0x0000FF) as white.
            return TRANSPARENT;
        }
        return (rgb & RGB_MASK) + (alphaInt << ALPHA_BIT_SHIFT);
    }

    public static int gray(final int gray) {
        return rgb(gray, gray, gray);
    }

    static int gray(final int gray, final int alpha) {
        return rgba(gray, gray, gray, alpha);
    }

    public static int lerpRgb(final int rgb1, final int rgb2, final int mix) {

        throw new UnimplementedMethodException();
    }

    public static int red(final int rgb) {
        return (rgb >> RED_BIT_SHIFT) & BYTE_MASK;
    }

    public static int green(final int rgb) {
        return (rgb >> GREEN_BIT_SHIFT) & BYTE_MASK;
    }

    public static int blue(final int rgb) {
        return (rgb >> BLUE_BIT_SHIFT) & BYTE_MASK;
    }

    public static int alpha(final int rgb) {
        return (rgb >> ALPHA_BIT_SHIFT) & BYTE_MASK;
    }

    static int clipBetween0and255(int i) {
        if (i > 255)
            return 255;
        else if (i < 0)
            return 0;
        else
            return i;
    }

    private static float clipBetween0and1AsFloat(double i) {
        if (i > 1.0)
            return 1.0f;
        else if (i < 0)
            return 0.0f;
        else
            return (float) i;
    }

    public static String toString(int color) {
        String format = String.format("%08X", color);
        if(format.startsWith("FF")) {
            format = format.substring(2);
        }
        return "#" + format;
    }
}