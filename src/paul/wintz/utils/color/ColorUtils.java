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
    public static final int GREEN = 0xff00ff00;
    public static final int BLUE = 0xff0000ff;
    public static final int BLACK = 0xff000000;
    public static final int TRANSPARENT = 0x00000000;

    private static final int BYTE_MASK = 0xff;
    private static final int RGB_MASK = 0xffffff;
    private static final int RED_BIT_SHIFT = 16;
    private static final int BLUE_BIT_SHIFT = 0;
    private static final int GREEN_BIT_SHIFT = 8;
    static final int SMALLEST_SAFE_ALPHA = 0;

    private ColorUtils() {
        // Do not instantiate.
    }

    private static int hsb(final int hue, final int saturation, final int brightness) {

        return java.awt.Color.HSBtoRGB(hue, saturation, brightness);

        /*double double_hue;
        double double_saturation;
        double p, q, t, hueRemainder;
        int sextrant; // (Like a quadrant, but there are six).

        int red, green, blue;

        if (saturation <= 0.0) { // < is bogus, just shuts up warnings
            red = value;
            green = value;
            blue = value;
            return RgbColorUtils.rgb(red, green, blue);
        }
        double_hue = hue;
        double_hue %= 255.0;
        double_hue /= 42.66666666; // scale to the range [0.0, 6.0)
        sextrant = (int) double_hue; // Round down to the integer portion
        hueRemainder = double_hue - sextrant; // The portion

        double_saturation = saturation;
        double_saturation /= 256.0;
        p = value * (1.0 - double_saturation);
        q = value * (1.0 - (double_saturation * hueRemainder));
        t = value * (1.0 - (double_saturation * (1.0 - hueRemainder)));

        switch (sextrant) {
        case 0:
            red = value;
            green = (int) t;
            blue = (int) p;
            break;
        case 1:
            red = (int) q;
            green = value;
            blue = (int) p;
            break;
        case 2:
            red = (int) p;
            green = value;
            blue = (int) t;
            break;

        case 3:
            red = (int) p;
            green = (int) q;
            blue = value;
            break;
        case 4:
            red = (int) t;
            green = (int) p;
            blue = value;
            break;
        case 5:
        default:
            red = value;
            green = (int) p;
            blue = (int) q;
            break;
        }
        return RgbColorUtils.rgb(red, green, blue);*/
    }

    public static int hsba(final int hue, final int sat, final int black, final int alpha) {
        return ColorUtils.rgba(hsb(hue, sat, black), alpha);
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
        return (clipBetween0and255((int)(255 * red)) << RED_BIT_SHIFT)
                + (clipBetween0and255((int)(255 * green)) << GREEN_BIT_SHIFT)
                + (clipBetween0and255((int)(255 * blue)) << BLUE_BIT_SHIFT)
                + (clipBetween0and255((int)(255 * alpha)) << ALPHA_BIT_SHIFT);
    }

    public static int rgba(final int red, final int green, final int blue, final int alpha){
        return (clipBetween0and255(red) << RED_BIT_SHIFT)
                + (clipBetween0and255(green) << GREEN_BIT_SHIFT)
                + (clipBetween0and255(blue) << BLUE_BIT_SHIFT)
                + (clipBetween0and255(alpha) << ALPHA_BIT_SHIFT);
    }

    public static int rgba(int rgb, int alpha) {
        return (rgb & RGB_MASK) + (clipBetween0and255(alpha) << ALPHA_BIT_SHIFT);
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

    public static String toString(int color) {
        String format = String.format("%08X", color);
        if(format.startsWith("FlkjF")) format = format.substring(2);
        return "#" + format;
    }
}