package paul.wintz.spirotechnics.tests;

import org.junit.Test;
import paul.wintz.utils.SequenceUtils;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestSequenceUtils_IndexOfLastTrueElement {

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfListIsEmpty() {
        SequenceUtils.indexOfLastTrueElement(Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void throwsIfListIsNull() {
        SequenceUtils.indexOfLastTrueElement(null);
    }
    
    @Test
    public void returnsMinusOneIfOnlyElementIsFalse() {
        assertThat(SequenceUtils.indexOfLastTrueElement(singletonList(false)), is(equalTo(-1)));
    }
    
    @Test
    public void returns1IfOnlyElementIsTrue() {
        assertThat(SequenceUtils.indexOfLastTrueElement(singletonList(true)), is(equalTo(0)));
    }
    
    @Test
    public void returnsLastOfSeveralTrueFollowedByFalses() {
        assertThat(SequenceUtils.indexOfLastTrueElement(asList(false, true, true, true, false, false)), is(equalTo(3)));
    }

    @Test
    public void returnsLastIndexIfItsTrue() {
        assertThat(SequenceUtils.indexOfLastTrueElement(asList(false, false, true)), is(equalTo(2)));
    }
    
    @Test
    public void returnsFirstIndexIfItsTrue() {
        assertThat(SequenceUtils.indexOfLastTrueElement(asList(true, false, false)), is(equalTo(0)));
    }
    
    @Test
    public void returnsMinusOneAllFalse() {
        assertThat(SequenceUtils.indexOfLastTrueElement(asList(false, false, false, false)), is(equalTo(-1)));
    }
    
}
