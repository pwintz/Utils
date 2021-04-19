package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BooleanOptionTest {

    BooleanOption.Builder builder = BooleanOption.builder();

    @Mock ValueOption.ValueChangeCallback<Boolean> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback);
    }

    @Test
    public void initialValueIsFalseByDefault() {
        assertFalse(builder.initial);
    }

    @Test
    public void callbackCalledWhenViewChangesValue() {
        ValueOption<Boolean> option = builder.build();

        option.emitViewValueChanged(true);

        Mockito.verify(changeCallback).callback(true);
    }
}