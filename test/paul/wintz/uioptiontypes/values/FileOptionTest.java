package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FileOptionTest {
    FileOption.Builder builder = FileOption.builder();

    @Mock ValueOption.ValueChangeCallback<File> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback)
                .extensionFilter("All files","*");
    }

    @Test
    public void initialValueIsFalseByDefault() {
        assertThat(builder.initial, is(equalTo(new File(""))));
    }

    @Test
    public void callbackCalledWhenViewChangesValue() {
        ValueOption<File> option = builder.build();

        option.emitViewValueChanged(new File("a/file/path"));

        Mockito.verify(changeCallback).callback(new File("a/file/path"));
    }
}