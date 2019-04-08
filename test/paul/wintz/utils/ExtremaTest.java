package paul.wintz.utils;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ExtremaTest {

    @Test
    public void updateOnceSetsMaxAndMinToValue() {
        Extrema extrema = new Extrema();

        extrema.update(3.14);

        assertThat(extrema.getMin(), is(equalTo(3.14)));
        assertThat(extrema.getMax(), is(equalTo(3.14)));
    }

    @Test
    public void updateTwiceSetsMaxAndMinToRespectiveValues() {
        Extrema extrema = new Extrema();

        extrema.update(-12.5);
        extrema.update(23.3);

        assertThat(extrema.getMin(), is(equalTo(-12.5)));
        assertThat(extrema.getMax(), is(equalTo(23.3)));
    }

    @Test
    public void clearResetsMaxAndMin() {
        Extrema extrema = new Extrema();

        extrema.update(1.2345);
        extrema.clear();

        assertThat(extrema.getMin(), is(equalTo(Double.MAX_VALUE)));
        assertThat(extrema.getMax(), is(equalTo(-Double.MAX_VALUE)));
    }

}