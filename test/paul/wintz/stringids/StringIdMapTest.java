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
                .setRequiredIds(MyStringIds.values())
                .build();
    }

    @Test
    public void stringMapReturnsSpecifiedStrings() {
        StringIdMap stringIdMap = StringIdMap.builder()
                .setRequiredIds(MyStringIds.values())
                .putStringId(MyStringIds.ONE, "one")
                .putStringId(MyStringIds.TWO, "two")
                .putStringId(MyStringIds.THREE, "three")
                .build();

        assertThat(stringIdMap.get(MyStringIds.ONE), is(equalTo("one")));
        assertThat(stringIdMap.get(MyStringIds.TWO), is(equalTo("two")));
        assertThat(stringIdMap.get(MyStringIds.THREE), is(equalTo("three")));
    }

    private enum MyStringIds implements StringId {
        ONE, TWO, THREE
    }

}