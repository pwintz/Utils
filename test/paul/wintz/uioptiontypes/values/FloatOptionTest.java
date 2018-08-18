package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class FloatOptionTest {

    FloatOption.Builder builder = FloatOption.builder();

    @Mock ValueOption.ValueChangeCallback<Float> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.viewValueChangeCallback(changeCallback);
    }

    @Test
    public void defaultValues() {
        assertThat(builder.initial, is(equalTo(0f)));
        assertThat(builder.increment, is(equalTo(Float.NaN)));
        assertThat(builder.min, is(equalTo(Float.NEGATIVE_INFINITY)));
        assertThat(builder.max, is(equalTo(Float.POSITIVE_INFINITY)));
    }

    @Test
    public void canCreateBuilderWithInitialValue() {
        assertThat(FloatOption.builder(3.4f).initial, is(equalTo(3.4f)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfMinIsIllegal() {
        builder.addValidityEvaluator(value -> value != -3.14f).min(-3.14f).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfMaxIsIllegal() {
        builder.addValidityEvaluator(value -> value != 2.17f).max(2.17f).build();
    }

    @Test
    public void callbackCalledWhenViewChangesValue() {
        FloatOption option = builder.build();

        option.emitViewValueChanged(1.618f);

        Mockito.verify(changeCallback).callback(1.618f);
    }

    @Test
    public void doesNotCallbackWhenViewChangesToValueLessThanMin() {
        FloatOption option = builder.min(-2f).build();

        assertFalse(option.emitViewValueChanged(-2.1f));

        Mockito.verify(changeCallback, never()).callback(any());
    }

    @Test
    public void doesNotCallbackWhenViewChangesToValueMoreThanMax() {
        FloatOption option = builder.max(4f).build();

        assertFalse(option.emitViewValueChanged(4.1f));

        Mockito.verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitNonPositiveDoesNotAllowZero() {
        FloatOption option = builder.prohibitNonPositive().initial(0.1f).build();

        assertFalse(option.emitViewValueChanged(0f));

        Mockito.verify(changeCallback, never()).callback(any());
    }

    @Test
    public void prohibitNonPositiveAllowsTinyValues() {
        FloatOption option = builder.prohibitNonPositive().initial(0.1f).build();

        assertTrue(option.emitViewValueChanged(Float.MIN_NORMAL));

        Mockito.verify(changeCallback).callback(Float.MIN_NORMAL);
    }

    @Test
    public void rangeSetsMinAndMax() {
        FloatOption option = builder.range(1900f, 2099f).initial(1993f).build();
        assertThat(option.getMin(), is(equalTo(1900f)));
        assertThat(option.getMax(), is(equalTo(2099f)));
    }

    @Test
    public void incrementSetAndGet() {
        FloatOption option = builder.increment(10.0f).build();
        assertThat(option.getIncrement(), is(equalTo(10.f)));
    }
}