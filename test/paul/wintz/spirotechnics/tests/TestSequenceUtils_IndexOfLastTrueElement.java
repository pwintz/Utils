package paul.wintz.spirotechnics.tests;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import paul.wintz.utils.SequenceUtils;

public class TestSequenceUtils_IndexOfLastTrueElement {

	@Test(expected = IllegalArgumentException.class)
	public void throwsIfListIsEmpty() {
		SequenceUtils.indexOfLastTrueElement(asList());
	}

	@Test(expected = NullPointerException.class)
	public void throwsIfListIsNull() {
		SequenceUtils.indexOfLastTrueElement(null);
	}
	
	@Test
	public void returnsMinusOneIfOnlyElementIsFalse() {
		assertThat(SequenceUtils.indexOfLastTrueElement(asList(false)), is(equalTo(-1)));
	}
	
	@Test
	public void returns1IfOnlyElementIsTrue() {
		assertThat(SequenceUtils.indexOfLastTrueElement(asList(true)), is(equalTo(0)));
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
