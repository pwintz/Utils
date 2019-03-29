package paul.wintz.uioptiontypes.values;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ListOptionTest {

    ListOption.Builder<String> builder = ListOption.builder();

    @Mock ValueOption.ValueChangeCallback<String> changeCallback;

    @Before
    public void setUp() throws Exception {
        builder.addViewValueChangeCallback(changeCallback);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIfListIsEmpty() {
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIfInitialValueNotInListWhenBuilt() {
        builder.add("An Item").initial("Not in list").build();
    }

    @Test
    public void initialSetToFirstItemByDefault() {
        ListOption<String> option = builder.add("First").build();

        assertThat(option.getValue(), is(equalTo("First")));
    }

    @Test
    public void canSetInitialItem() {
        ListOption<String> option = builder
                .initial("Second")
                .add("First")
                .add("Second")
                .build();

        assertThat(option.getValue(), is(equalTo("Second")));
    }

    @Test
    public void canAddCollection() {
        Collection<String> items = asList("First", "Second", "Third");
        ListOption<String> option = builder
                .addAll(items)
                .build();

        // Warning; this won't work if the collection was a Set
        assertThat(option.getSize(), is(equalTo(items.size())));
    }

    private enum Numbers {
        ONE, TWO, THREE
    }

    @Test
    public void canAddEnumeration() {
        ListOption<Numbers> enumOption = ListOption.<Numbers>builder()
                .fromEnumeration(Numbers.class)
                .build();

        assertThat(enumOption.getValue(), is(equalTo(Numbers.ONE)));
    }

    @Test
    public void acceptsValueInList() {
        ValueOption<String> option = builder.add("First").add("Second").build();

        assertTrue(option.emitViewValueChanged("Second"));

        verify(changeCallback).callback("Second");
    }

    @Test
    public void rejectsValuesNotInList() {
        ValueOption<String> option = builder.add("First").add("Second").build();

        assertFalse(option.emitViewValueChanged("Third"));

        verify(changeCallback, never()).callback(any());
    }

    @Test
    public void iteratorProvidesAllItems() {
        List<String> list = asList("One", "Two", "Three");
        ListOption<String> option = builder.addAll(list).build();

//        assertThat(option.getList(), contains());
//        for(String s : option.getList()) {
//            assertThat(s, is(equalTo(list.get(i))));
//        }
    }

    @Test
    public void toStringDoesNotChoke() {
        //noinspection ResultOfMethodCallIgnored
        builder.add("Anything").build().toString();
    }
}