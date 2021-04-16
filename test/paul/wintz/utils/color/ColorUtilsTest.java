package paul.wintz.utils.color;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ColorUtilsTest {

    @Test
    public void pixelsToString() {
        int[] pixels = {0xFF0000, 0x123456, 0x00000000};

        assertThat(ColorUtils.pixelsToString(pixels, 1), is(equalTo("[#00FF0000, ...]")));
        assertThat(ColorUtils.pixelsToString(pixels), is(equalTo("[#00FF0000, #00123456, #00000000]")));
        assertThat(ColorUtils.pixelsToString(pixels, 10), is(equalTo("[#00FF0000, #00123456, #00000000]")));
    }
}