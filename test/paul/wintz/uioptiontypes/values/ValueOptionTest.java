package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValueOptionTest {

    private final StringOptionStub.Builder builder = StringOptionStub.builder();

    @Mock ValueOption.ValueChangeCallback<String> changeCallback;
    @Mock ValueOption.ValueChangeCallback<String> changeCallback2;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback)
                .initial("");
    }

    @Test
    public void viewChangeTriggersCallback() {
        ValueOption<String> stringOption = builder.build();

        stringOption.emitViewValueChanged("Value");

        verify(changeCallback).callback("Value");
    }

    @Test
    public void duplicateViewChangeTriggersOnlyOneCallback() {
        ValueOption<String> stringOption = builder.build();

        stringOption.emitViewValueChanged("Value");
        stringOption.emitViewValueChanged("Value");

        verify(changeCallback, times(1)).callback("Value");
    }

    public void duplicateValidViewChangesBothReturnTrue() {
        ValueOption<String> stringOption = builder.build();

        assertTrue(stringOption.emitViewValueChanged("NewValue"));
        assertTrue(stringOption.emitViewValueChanged("NewValue"));
    }

    public void emitViewChangeReturnsFalseIfValueIsInvalid() {
        ValueOption<String> stringOption = builder
                .addValidityEvaluator(value -> false)
                .build();

        assertFalse(stringOption.emitViewValueChanged("Not Valid"));
    }

    public void emitViewChangeReturnsFalseIfStateIsInvalid() {
        ValueOption<String> stringOption = builder
                .addStateValidator(() -> false)
                .build();

        assertFalse(stringOption.emitViewValueChanged("Not Valid"));
    }

    @Test(expected = NullPointerException.class)
    public void buildersThrowsIfNoInitialValue() {
        StringOptionStub.builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfIllegalInitialValue() {
        builder.initial("notEmpty")
                .addValidityEvaluator(String::isEmpty) // will evaluate as false
                .build();
    }

    @Test
    public void checksInitialValueAgainstMultipleValidators() {
        builder.initial("MyValue")
                .addValidityEvaluator(s -> s.equals("MyValue"))
                .addValidityEvaluator(s -> s.length() == 7)
                .build();
    }

    @Test
    public void viewValueChangeRejectedIfValueIsInvalid() {
        ValueOption<String> stringOption = builder
                .addValidityEvaluator(String::isEmpty) // will evaluate as false
                .build();

        stringOption.emitViewValueChanged("Not empty");

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void viewValueChangePassedIfValueIsValid() {
        ValueOption<String> stringOption = builder
                .initial("Expel") // must satisfy value validators
                .addValidityEvaluator(s -> !s.isEmpty())
                .addValidityEvaluator(value -> value.startsWith("Exp"))
                .build();

        stringOption.emitViewValueChanged("Expected");

        verify(changeCallback).callback("Expected");
    }

    @Test
    public void viewValueChangeRejectedIfStateIsInvalid() {
        ValueOption<String> stringOption = builder
                .addStateValidator(() -> false)
                .build();

        stringOption.emitViewValueChanged("Anything");

        verify(changeCallback, never()).callback(any());
        assertThat(stringOption.getValue(), is(not(equalTo("Anything"))));
    }

    @Test
    public void viewValueChangePassedIfStateIsValid() {
        ValueOption<String> stringOption = builder
                .addStateValidator(() -> true)
                .addStateValidator(() -> true)
                .build();

        stringOption.emitViewValueChanged("Anything");

        verify(changeCallback).callback("Anything");
        assertThat(stringOption.getValue(), is(equalTo("Anything")));
    }

    @Test
    public void isValueValidReturnsFalseIfAnyValidatorFails() {
        ValueOption<String> stringOption = builder
                .addValidityEvaluator(s -> true)
                .addValidityEvaluator(String::isEmpty) // will evaluate as false
                .addValidityEvaluator(s -> true)
                .build();

        assertFalse(stringOption.isValueValid("Not Empty"));
    }

    @Test
    public void isValueValidReturnsFalseIfAllValidatorsPass() {
        ValueOption<String> stringOption = builder
                .addValidityEvaluator(s -> true)
                .addValidityEvaluator(s -> true)
                .build();

        assertTrue(stringOption.isValueValid("Anything"));
    }

    @Test
    public void isStateValidReturnsFalseIfAnyValidatorFails() {
        ValueOption<String> stringOption = builder
                .addStateValidator(() -> true)
                .addStateValidator(() -> false)
                .addStateValidator(() -> true)
                .build();

        assertFalse(stringOption.isStateValid());
    }

    @Test
    public void isStateValidReturnsFalseIfAllValidatorsPass() {
        ValueOption<String> stringOption = builder
                .addStateValidator(() -> true)
                .addStateValidator(() -> true)
                .build();

        assertTrue(stringOption.isStateValid());
    }

    @Test
    public void noCallbackOnInitializationByDefault() {
        builder.build();

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void callbackOnInitializationEnabled() {
        builder.callbackOnInitialization(true)
                .initial("Initial value")
                .build();

        verify(changeCallback).callback("Initial value");
    }

    @Test
    public void addViewValueChangeCallbackBeforeBuilding() {
        ValueOption<String> option = builder
                .addViewValueChangeCallback(changeCallback2)
                .build();

        option.emitViewValueChanged("New value");

        verify(changeCallback).callback("New value");
        verify(changeCallback2).callback("New value");
    }

    @Test
    public void addViewValueChangeCallbackAfterBuilding() {
        ValueOption<String> option = builder.build();
        option.addViewValueChangeCallback(changeCallback2);

        option.emitViewValueChanged("New value");

        verify(changeCallback).callback("New value");
        verify(changeCallback2).callback("New value");
    }

    @Test
    public void reusingBuilderDiscardsPreviousCallbacks() {
        StringOptionStub.Builder builder = StringOptionStub.builder()
                .addViewValueChangeCallback(changeCallback)
                .callbackOnInitialization(true)
                .addStateValidator(() -> true)
                .addValidityEvaluator(value -> true)
                .initial("Initial value");

        builder.build();

        assertEquals("Initial value", builder.initial);
        assertTrue(builder.isCallbackOnInitialization());
        assertEquals(1, builder.stateValidatorsCount());
        assertEquals(1, builder.valueValidatorsCount());

        assertEquals(0, builder.viewValueChangeCallbackCount());
    }
}