package paul.wintz.functionevaluator;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static paul.wintz.functionevaluator.ExtraFunctions.*;

public class ExtraFunctionsTest {

    private static final double TOLERANCE = 1e-6;

    @Test
    public void testNormalizedCosine() {
        assertThat(NORMALIZED_COSINE.apply(0),      is(closeTo(1.0d, TOLERANCE)));
        assertThat(NORMALIZED_COSINE.apply(0.25),   is(closeTo(0.5d, TOLERANCE)));
        assertThat(NORMALIZED_COSINE.apply(0.5),    is(closeTo(0.0d, TOLERANCE)));
        assertThat(NORMALIZED_COSINE.apply(0.75),   is(closeTo(0.5d, TOLERANCE)));
        assertThat(NORMALIZED_COSINE.apply(1.0),    is(closeTo(1.0d, TOLERANCE)));

        // Test a couple arguments outside [0,1]
        assertThat(NORMALIZED_COSINE.apply(-10.25), is(closeTo(0.5d, TOLERANCE)));
        assertThat(NORMALIZED_COSINE.apply(7.5),    is(closeTo(0.0d, TOLERANCE)));
    }

    @Test
    public void testNormalizedSine() {
        assertThat(NORMALIZED_SINE.apply(0),    is(closeTo(0.5d, TOLERANCE)));
        assertThat(NORMALIZED_SINE.apply(0.25), is(closeTo(1.0d, TOLERANCE)));
        assertThat(NORMALIZED_SINE.apply(0.5),  is(closeTo(0.5d, TOLERANCE)));
        assertThat(NORMALIZED_SINE.apply(0.75), is(closeTo(0.0d, TOLERANCE)));
        assertThat(NORMALIZED_SINE.apply(1.0),  is(closeTo(0.5d, TOLERANCE)));

        // Test a couple arguments outside [0,1]
        assertThat(NORMALIZED_SINE.apply(-9.25),is(closeTo(0.0d, TOLERANCE)));
        assertThat(NORMALIZED_SINE.apply(7.5),  is(closeTo(0.5d, TOLERANCE)));
    }

    @Test
    public void testRound() {
        assertThat(ROUND.apply(-0.4), is(equalTo(0d)));
        assertThat(ROUND.apply(-0.1), is(equalTo(0d)));
        assertThat(ROUND.apply(0.1),  is(equalTo(0d)));
        assertThat(ROUND.apply(0.4),  is(equalTo(0d)));

        assertThat(ROUND.apply(0.6),  is(equalTo(1d)));
        assertThat(ROUND.apply(0.9),  is(equalTo(1d)));
        assertThat(ROUND.apply(1.1),  is(equalTo(1d)));
        assertThat(ROUND.apply(1.4),  is(equalTo(1d)));

        assertThat(ROUND.apply(-1.4), is(equalTo(-1d)));
        assertThat(ROUND.apply(-1.1), is(equalTo(-1d)));
        assertThat(ROUND.apply(-0.9), is(equalTo(-1d)));
        assertThat(ROUND.apply(-0.6), is(equalTo(-1d)));

        assertTrue(Double.isNaN(ROUND.apply(Double.NaN)));
    }

    @Test
    public void testSteps() {

        double steps = 1.0;
        // zeroth step
        assertThat(STEPS.apply(0.0, steps), is(closeTo(0.0d, TOLERANCE)));
        assertThat(STEPS.apply(0.49, steps), is(closeTo(0.0d, TOLERANCE)));
        // first step
        assertThat(STEPS.apply(0.5, steps), is(closeTo(1.0d, TOLERANCE)));
        assertThat(STEPS.apply(0.99, steps), is(closeTo(1.0d, TOLERANCE)));
        // second step
        assertThat(STEPS.apply(1.0, steps), is(closeTo(2.0d, TOLERANCE)));
        // -1th step
        assertThat(STEPS.apply(-0.1, steps), is(closeTo(-1.0d, TOLERANCE)));

        steps = 2.0;
        // zeroth step
        assertThat(STEPS.apply(0.0, steps), is(closeTo(0.0d, TOLERANCE)));
        assertThat(STEPS.apply(0.32, steps), is(closeTo(0.0d, TOLERANCE)));
        // fist step
        assertThat(STEPS.apply(0.34, steps), is(closeTo(0.5d, TOLERANCE)));
        assertThat(STEPS.apply(0.65, steps), is(closeTo(0.5d, TOLERANCE)));
        // second step
        assertThat(STEPS.apply(0.67, steps), is(closeTo(1.0d, TOLERANCE)));
        assertThat(STEPS.apply(0.99, steps), is(closeTo(1.0d, TOLERANCE)));
        // third step
        assertThat(STEPS.apply(1.0, steps), is(closeTo(1.5d, TOLERANCE)));
        // -1th step
        assertThat(STEPS.apply(-0.1, steps), is(closeTo(-0.5d, TOLERANCE)));
    }

    @Test
    public void testSquareWave() {
        double frequency = 1.0;
        // For a frequency of 1.0, the value should be
        // 0.0 in {..., [-1.0, -0.5), [0.0, 0.5), [1.0, 1.5), ...}
        // and  1.0 in {...[-0.5, 0.0), [0.5, 1.0), [1.5, 2.0), ...}
        assertThat(SQUARE_WAVE.apply(0.0, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(0.5, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(1.0, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(1.5, frequency), is(equalTo(1.0)));

        // the negative direction works too.
        assertThat(SQUARE_WAVE.apply(-0.4, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(-0.6, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(-1.4, frequency), is(equalTo(1.0)));

        frequency = 2.0;
        // For a frequency of 2.0, the value should be
        // 0.0 in ..., [-0.5, 0.25), [0.0, 0.25), [0.5, 0.75), ...
        // and  1.0 in ...[-0.25, 0.0), [0.25, 0.5), [0.75, 2.0), etc.
        assertThat(SQUARE_WAVE.apply(0.0, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(0.25, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(0.5, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(0.75, frequency), is(equalTo(1.0)));

        // the negative direction works too.
        assertThat(SQUARE_WAVE.apply(-0.1, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(-0.26, frequency), is(equalTo(0.0)));

        frequency = -1.0;
        // For a frequency of -1.0, intervals are identical to frequency=1.0, but the
        // values are swapped:
        // 1.0 in {..., (-0.5, 0.25], (0.0, 0.25], (0.5, 0.75], ...}
        // and 0.0 in {..., (-0.25, 0.0], (0.25, 0.5], (0.75, 2.0], ...}
        // Note, however, the rounding at the boundaries is opposite of above
        assertThat(SQUARE_WAVE.apply(0.0, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(0.01, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(0.51, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(1.01, frequency), is(equalTo(1.0)));
        assertThat(SQUARE_WAVE.apply(1.51, frequency), is(equalTo(0.0)));

       // the negative direction works too.
        assertThat(SQUARE_WAVE.apply(-0.1, frequency), is(equalTo(0.0)));
        assertThat(SQUARE_WAVE.apply(-0.51, frequency), is(equalTo(1.0)));
    }
}