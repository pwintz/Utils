package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static paul.wintz.utils.color.ColorUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class ColorOptionTest {

    ColorOption.Builder builder = ColorOption.builder();

    @Mock ValueOption.ValueChangeCallback<Integer> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback);
    }

    @Test
    public void initialValueIsBlackByDefault() {
        ColorOption option = builder.build();

        assertThat(option.getValue(), is(equalTo(BLACK)));
    }


    @Test
    public void initialValueSetByColor() {
        ColorOption option = builder
                .initial(0x11336699)
                .build();

        assertThat(option.getValue(), is(equalTo(0x11336699)));
    }

    @Test
    public void initialValueSetByChannel() {
        ColorOption option = builder
                .red(0x12).green(0x34).blue(0x56).alpha(0x78)
                .build();

        assertThat(option.getValue(), is(equalTo(0x78123456)));
    }

    @Test
    public void initialValueSetByGrayValue() {
        ColorOption option = builder
                .gray(0x99)
                .build();

        assertThat(option.getValue(), is(equalTo(0xFF999999)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfAChannelIsNegative() {
        builder.red(-1).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfAChannelIsLargerThan255() {
        builder.alpha(256).build();
    }

    @Test
    public void callbackCalledWhenViewChangesColorValue() {
        ValueOption<Integer> option = builder.build();

        option.emitViewValueChanged(YELLOW);

        Mockito.verify(changeCallback).callback(YELLOW);
    }

    @Test
    public void callbackCalledWhenViewChangesChannelValues() {
        ColorOption option = builder.build();

        option.emitViewValueChanged(0x11, 0x22, 0x33, 0x44);

        Mockito.verify(changeCallback).callback(0x44112233);
    }

}