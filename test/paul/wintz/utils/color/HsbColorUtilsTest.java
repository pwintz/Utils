package paul.wintz.utils.color;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static paul.wintz.utils.color.ColorUtils.*;
import static paul.wintz.testutils.ColorMatcher.*;

public class HsbColorUtilsTest {

    @Test
    public void hsbaTest() {
        // Hue doesn't affect WHITE
        assertThat(hsba(0.4, 0.0, 1.0, 1.0), isWhite());
        assertThat(hsba(0.9, 0.0, 1.0, 1.0), isWhite());

        // Hue and saturation don't affect BLACK
        assertThat(hsba(0.2, 0.1, 0.0, 1.0), isBlack());
        assertThat(hsba(0.6, 0.7, 0.0, 1.0), isBlack());

        // Red is at either hue = 0.0 or hue = 1.0.
        assertThat(hsba(0.0, 1.0, 1.0, 1.0), isRed());
        assertThat(hsba(1.0, 1.0, 1.0, 1.0), isRed());

        // Other colors
        assertThat(hsba(BLUE_HUE, 1.0, 1.0, 1.0), isBlue());
        assertThat(hsba(GREEN_HUE, 1.0, 1.0, 1.0), isGreen());

        // Hue, saturation, and blackness don't affect TRANSPARENT.
        assertThat(hsba(0.0, 0.0, 0.0, 0.0), isFullyTransparent());
        assertThat(hsba(0.4, 0.2, 0.1, 0.0), isFullyTransparent());
        assertThat(hsba(1.0, 1.0, 1.0, 0.0), isFullyTransparent());
    }

    @Test
    public void hsbaArgumentsAreClipped() {
        // Red is at either hue = 0.0 or hue = 1.0.
        assertThat(hsba(-0.1, 1.2, 1.1, 1.3), isRed());
        assertThat(hsba(Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), isWhite());
        assertThat(hsba(-Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, 1.0), isBlack());
        assertThat(hsba(1.0, 1.0, 1.0, -Double.MAX_VALUE), isFullyTransparent());
    }

    @Test
    public void toStringTest() {
        assertThat(ColorUtils.toString(0x00000000), is(equalTo("#00000000")));
        assertThat(ColorUtils.toString(0x11223344), is(equalTo("#11223344")));
        assertThat(ColorUtils.toString(0x00FFFFFF), is(equalTo("#00FFFFFF")));

        // If transparency is 100%, then the first leading "FF" is truncated.
        assertThat(ColorUtils.toString(0xFF332211), is(equalTo("#332211")));
        assertThat(ColorUtils.toString(0xFFFFFFFF), is(equalTo("#FFFFFF")));
    }
}