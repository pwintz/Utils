package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IntegerOptionTest {

    IntegerOption.Builder builder = IntegerOption.builder();
    @Mock ValueOption.ValueChangeCallback<Integer> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback);
    }

    @Test
    public void defaultValues() {
        assertThat(builder.initial, is(equalTo(0)));
        assertThat(builder.increment, is(equalTo(1)));
        assertThat(builder.min, is(equalTo(Integer.MIN_VALUE)));
        assertThat(builder.max, is(equalTo(Integer.MAX_VALUE)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfMinIsIllegal() {
        builder.addValidityEvaluator(value -> value != -17).min(-17).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfMaxIsIllegal() {
        builder.addValidityEvaluator(value -> value != 4).max(4).build();
    }

    @Test
    public void callbackCalledWhenViewChangesValue() {
        IntegerOption option = builder.build();

        option.emitViewValueChanged(5);

        verify(changeCallback).callback(5);
    }

    @Test
    public void doesNotCallbackWhenViewChangesToValueLessThanMin() {
        IntegerOption option = builder.min(-2).build();

        assertFalse(option.emitViewValueChanged(-3));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void doesNotCallbackWhenViewChangesToValueMoreThanMax() {
        IntegerOption option = builder.max(4).build();

        assertFalse(option.emitViewValueChanged(5));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitZeroDoesNotAllowZero() {
        IntegerOption option = builder.prohibitZero().initial(1).build();

        assertFalse(option.emitViewValueChanged(0));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitNegativeDoesNotAllowNegative() {
        IntegerOption option = builder.prohibitNegative().build();

        assertFalse(option.emitViewValueChanged(-1));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitNonPositiveDoesNotAllowZero() {
        IntegerOption option = builder.prohibitNonPositive().initial(1).build();

        assertFalse(option.emitViewValueChanged(0));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitNonPositiveAllowsPositiveValues() {
        IntegerOption option = builder.prohibitNonPositive().initial(1).build();

        assertTrue(option.emitViewValueChanged(2));

        verify(changeCallback).callback(2);
    }

    @Test
    public void rangeSetsMinAndMax() {
        IntegerOption option = builder.range(1900, 2099).initial(1993).build();
        assertThat(option.getMin(), is(equalTo(1900)));
        assertThat(option.getMax(), is(equalTo(2099)));
    }

    @Test
    public void settingMinMultipleTimesUsesLargest() {
        builder.min(-100);
        assertThat(builder.getMin(), is(equalTo(-100)));
        builder.min(-1); // Should update minimum because it provides a new lower bound.
        assertThat(builder.getMin(), is(equalTo(-1)));
        builder.min(-10); // Should be ignored because it is below the allowed minimum
        assertThat(builder.getMin(), is(equalTo(-1)));
    }

    @Test
    public void settingMaxMultipleTimesUsesSmallest() {
        builder.max(100);
        assertThat(builder.getMax(), is(equalTo(100)));
        builder.max(1); // Should update maximum because it provides a new upper bound.
        assertThat(builder.getMax(), is(equalTo(1)));
        builder.max(10); // Be ignored because it is above the allowed minimum
        assertThat(builder.getMax(), is(equalTo(1)));
    }

    @Test
    public void incrementAmountSetAndGet() {
        IntegerOption option = builder.increment(10).build();
        assertThat(option.getIncrement(), is(equalTo(10)));
    }

    @Test
    public void getNumberOfValuesInRange() {
        IntegerOption option = builder.range(0, 9).build();

        assertThat(option.getNumberOfValuesInRange(), is(equalTo(10L)));
    }

    @Test
    public void getNumberOfValuesInRangeForCountLargerThanMaxInteger() {
        IntegerOption option = builder.build();

        assertThat(option.getNumberOfValuesInRange(),
                is(equalTo(((long) Integer.MAX_VALUE) - (long) Integer.MIN_VALUE + 1L)));
    }

    @Test
    public void toStringDoesNotChoke() {
        //noinspection ResultOfMethodCallIgnored
        builder.build().toString();
    }

}