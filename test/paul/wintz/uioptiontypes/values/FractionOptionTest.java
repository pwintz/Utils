package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import paul.wintz.math.Fraction;

import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class FractionOptionTest {

    FractionOption.Builder builder = FractionOption.builder();
    @Mock ValueOption.ValueChangeCallback<Fraction> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback);
    }

    //TODO: test FractionOption
    @Test
    public void defaultValues() {
        builder.initial(new Fraction(1, 2, false)).build();
//        assertThat(builder.initial, is(equalTo(Fraction.)));
//        assertThat(builder.increment, is(equalTo(1)));
//        assertThat(builder.min, is(equalTo(Fraction.MIN_VALUE)));
//        assertThat(builder.max, is(equalTo(Fraction.MAX_VALUE)));
    }
//
//    @Test(expected = IllegalStateException.class)
//    public void throwsIfMinIsIllegal() {
//        builder.addValidityEvaluator(value -> value != -17).min(-17).build();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void throwsIfMaxIsIllegal() {
//        builder.addValidityEvaluator(value -> value != 4).max(4).build();
//    }
//
//    @Test
//    public void callbackCalledWhenViewChangesValue() {
//        FractionOption option = builder.build();
//
//        option.emitViewValueChanged(5);
//
//        verify(changeCallback).callback(5);
//    }
//
//    @Test
//    public void doesNotCallbackWhenViewChangesToValueLessThanMin() {
//        FractionOption option = builder.min(-2).build();
//
//        assertFalse(option.emitViewValueChanged(-3));
//
//        verify(changeCallback, never()).callback(any());
//    }
//
//    @Test
//    public void doesNotCallbackWhenViewChangesToValueMoreThanMax() {
//        FractionOption option = builder.max(4).build();
//
//        assertFalse(option.emitViewValueChanged(5));
//
//        verify(changeCallback, never()).callback(any());
//    }
//
//    @Test
//    public void prohibitZeroDoesNotAllowZero() {
//        FractionOption option = builder.prohibitZero().initial(1).build();
//
//        assertFalse(option.emitViewValueChanged(0));
//
//        verify(changeCallback, never()).callback(any());
//    }
//
//    @Test
//    public void prohibitNegativeDoesNotAllowNegative() {
//        FractionOption option = builder.prohibitNegative().build();
//
//        assertFalse(option.emitViewValueChanged(-1));
//
//        verify(changeCallback, never()).callback(any());
//    }
//
//    @Test
//    public void prohibitNonPositiveDoesNotAllowZero() {
//        FractionOption option = builder.prohibitNonPositive().initial(1).build();
//
//        assertFalse(option.emitViewValueChanged(0));
//
//        verify(changeCallback, never()).callback(any());
//    }
//
//    @Test
//    public void prohibitNonPositiveAllowsPositiveValues() {
//        FractionOption option = builder.prohibitNonPositive().initial(1).build();
//
//        assertTrue(option.emitViewValueChanged(2));
//
//        verify(changeCallback).callback(2);
//    }
//
//    @Test
//    public void rangeSetsMinAndMax() {
//        FractionOption option = builder.range(1900, 2099).initial(1993).build();
//        assertThat(option.getMin(), is(equalTo(1900)));
//        assertThat(option.getMax(), is(equalTo(2099)));
//    }
//
//    @Test
//    public void incrementAmountSetAndGet() {
//        FractionOption option = builder.increment(10).build();
//        assertThat(option.getIncrement(), is(equalTo(10)));
//    }
//
//    @Test
//    public void getNumberOfValuesInRange() {
//        FractionOption option = builder.range(0, 9).build();
//
//        assertThat(option.getNumberOfValuesInRange(), is(equalTo(10L)));
//    }
//
//    @Test
//    public void getNumberOfValuesInRangeForCountLargerThanMaxFraction() {
//        FractionOption option = builder.build();
//
//        assertThat(option.getNumberOfValuesInRange(),
//                is(equalTo(((long) Fraction.MAX_VALUE) - (long) Fraction.MIN_VALUE + 1L)));
//    }

}