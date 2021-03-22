package paul.wintz.stringids;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class StringIdMapTest {

    @Test (expected = NullPointerException.class)
    public void throwsIfRequiredIdsNotGivenToBuilder() {
        StringIdMap.builder().build();
    }

    @Test (expected = IllegalStateException.class)
    public void throwsIfMissingStringsForRequiredIds() {
        StringIdMap.builder()
                .setRequiredIds(NumbersStringIds.values())
                .build();
    }

    @Test
    public void stringMapReturnsSpecifiedStrings() {
        StringIdMap stringIdMap = StringIdMap.builder()
                .setRequiredIds(NumbersStringIds.values())
                .putStringId(NumbersStringIds.ONE, "one")
                .putStringId(NumbersStringIds.TWO, "two")
                .putStringId(NumbersStringIds.THREE, "three")
                .build();

        assertThat(stringIdMap.get(NumbersStringIds.ONE), is(equalTo("one")));
        assertThat(stringIdMap.get(NumbersStringIds.TWO), is(equalTo("two")));
        assertThat(stringIdMap.get(NumbersStringIds.THREE), is(equalTo("three")));
    }

    @Test
    public void stringMapReturnsEmptyStringIfKeyIsNull() {
        StringIdMap stringIdMap = StringIdMap.builder()
                .setRequiredIds(EmptyStringIds.values())
                .build();

        assertThat(stringIdMap.get(null), is(equalTo("")));
    }

    private enum NumbersStringIds implements StringId {
        ONE, TWO, THREE
    }
    private enum EmptyStringIds implements StringId {}

}