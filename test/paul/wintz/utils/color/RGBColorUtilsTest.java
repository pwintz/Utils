package paul.wintz.utils.color;

import java.awt.Color;
import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// TODO: Move to tests/
public class RGBColorUtilsTest {

    Random random = new Random();

    @Test
    public final void testRgb() {
        verifyRgb(0, 0, 0);
        verifyRgb(255, 0, 0);
        verifyRgb(255, 255, 0);
        verifyRgb(255, 255, 255);
        verifyRgb(0, 0, 255);
        verifyRgb(1, 1, 1);

        verifyRgb(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        verifyRgb(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public final void testRgba() {
        verifyRgb(0, 0, 0, 0);
        verifyRgb(255, 0, 0, 255);
        verifyRgb(255, 255, 0, Integer.MAX_VALUE);
        verifyRgb(255, 255, 255, Integer.MIN_VALUE);
        verifyRgb(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 4);
        verifyRgb(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 15);
    }

    @Test
    public final void testRgbaIntInt() {

        verifyRgbaIntInt(0x00000000, 0, 0);
        verifyRgbaIntInt(0xffff0000, 0xff0000, 255);
        verifyRgbaIntInt(0xffffff00, 0xffff00, Integer.MAX_VALUE);
        verifyRgbaIntInt(0x00ffffff, 0xffffff, Integer.MIN_VALUE);
        verifyRgbaIntInt(0x04000000, 0x000000, 4);
        verifyRgbaIntInt(0x0fffffff, 0xffffff, 15);
    }


    @Test
    public final void testGrayInt() {
        verifyGray(0);
        verifyGray(1);
        verifyGray(255);
        verifyGray(256);
        verifyGray(Integer.MAX_VALUE);
        verifyGray(-1);
        verifyGray(Integer.MIN_VALUE);
    }

    @Test
    public final void testGrayIntInt() {
        verifyGray(0, 0);
        verifyGray(0, 5);
        verifyGray(0, Integer.MIN_VALUE);
        verifyGray(0, Integer.MAX_VALUE);

        verifyGray(Integer.MIN_VALUE, 0);
        verifyGray(Integer.MIN_VALUE, 5);
        verifyGray(Integer.MIN_VALUE, Integer.MIN_VALUE);
        verifyGray(Integer.MIN_VALUE, Integer.MAX_VALUE);

        verifyGray(1, 0);
        verifyGray(1, 5);
        verifyGray(1, Integer.MIN_VALUE);
        verifyGray(1, Integer.MAX_VALUE);

        verifyGray(255, 0);
        verifyGray(255, 5);
        verifyGray(255, Integer.MIN_VALUE);
        verifyGray(255, Integer.MAX_VALUE);

        verifyGray(Integer.MAX_VALUE, 0);
        verifyGray(Integer.MAX_VALUE, 5);
        verifyGray(Integer.MAX_VALUE, Integer.MIN_VALUE);
        verifyGray(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private void verifyRgbaIntInt(int expected, int rgb, int a) {
        assertEquals(expected, ColorUtils.rgba(rgb, a));
    }

    private void verifyRgb(int r, int g, int b) {
        verifyRgb(r, g, b, 255);
    }

    private void verifyRgb(int r, int g, int b, int alpha) {
        final String expected = rgbaToHexString(r, g, b, alpha);

        assertEquals(expected, toStandardString(ColorUtils.rgba(r, g, b, alpha)));
    }

    private void verifyGray(int gray) {
        verifyGray(gray, 255);
    }

    private void verifyGray(int gray, int alpha) {
        final String expected = grayToHexString(gray, alpha);
        assertEquals(expected, toStandardString(ColorUtils.gray(gray, alpha)));
    }

    private String grayToHexString(int gray, int a){
        return channelToHex(a)
        + channelToHex(gray)
        + channelToHex(gray)
        + channelToHex(gray);
    }

    private String toStandardString(int color){
        return String.format("%08x", color);
    }

    private String rgbaToHexString(int r, int g, int b, int a){

        return channelToHex(a)
        + channelToHex(r)
        + channelToHex(g)
        + channelToHex(b);
    }

    private String channelToHex(int channel){
        if(channel > 255) {
            channel = 255;
        }
        if(channel < 0) {
            channel = 0;
        }
        return String.format("%02x", channel);
    }

    @Test
    public final void testRed() {
        for (int red = 0; red < 256; red++) {
            final int rgb = ColorUtils.rgba(red, random.nextInt(255), random.nextInt(255), random.nextInt(255));
            assertEquals(red, ColorUtils.red(rgb));
        }
    }

    @Test
    public final void testGreen() {
        for (int green = 0; green < 256; green++) {
            final int rgb = ColorUtils.rgba(random.nextInt(255), green, random.nextInt(255), random.nextInt(255));
            assertEquals(green, ColorUtils.green(rgb));
        }
    }

    @Test
    public final void testBlue() {
        for (int blue = 0; blue < 256; blue++) {
            final int rgb = ColorUtils.rgba(random.nextInt(255), random.nextInt(255), blue, random.nextInt(255));
            assertEquals(blue, ColorUtils.blue(rgb));
        }
    }

    @Test
    public final void testAlpha() {
        for (int alpha = 0; alpha < 256; alpha++) {
            final int rgb = ColorUtils.rgba(random.nextInt(255), random.nextInt(255), random.nextInt(255), alpha);
            assertEquals(alpha, ColorUtils.alpha(rgb));
        }
    }

    @Test
    public final void testClipBetween0To255() {
        verifyClipBetween(0);
        verifyClipBetween(255);
        verifyClipBetween(256);
        verifyClipBetween(-1);
        verifyClipBetween(Integer.MIN_VALUE);
        verifyClipBetween(Integer.MAX_VALUE);
        verifyClipBetween(30);
    }

    private void verifyClipBetween(int valueToClip) {
        int expected = valueToClip;
        if(expected < 0) {
            expected = 0;
        }
        if(expected > 255) {
            expected = 255;
        }
        assertEquals(expected, ColorUtils.clipBetween0and255(valueToClip));
    }


    //HSB

    @Test
    public final void testAwtHSB(){
        final float hue = 1;
        float saturation = 1;
        final float brightness = 1;

        int expected = 0xffff0000;
        int actual = Color.HSBtoRGB(hue, saturation, brightness);
        assertEquals(expected, actual);

        saturation = 0;
        expected = 0xffffffff;
        actual = Color.HSBtoRGB(hue, saturation, brightness);
        assertEquals(expected, actual);

    }

}
