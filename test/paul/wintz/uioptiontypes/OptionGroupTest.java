package paul.wintz.uioptiontypes;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class OptionGroupTest {

	OptionGroup optionGroup = new OptionGroup("A Description");

	@Mock
	OptionGroup.OnListChangeListener changeListener;
	
	UserInputOption<?> dummyOption0 = new UserInputOption<String>("dummy0") {};
	UserInputOption<?> dummyOption1 = new UserInputOption<String>("dummy1") {};
	
	// CONSTRUCTORS
	
	@Test
	public void setsDescriptionToClassNameIfNoDescriptionGiven() {
		OptionGroup anOptionGroup = new OptionGroup();
		
		assertThat(anOptionGroup.getDescription(), is(Matchers.equalTo("OptionGroup")));
	}
	
	@Test
	public void setsDescriptionToArgument() {
		OptionGroup anOptionGroup = new OptionGroup("A Description");
		
		assertThat(anOptionGroup.getDescription(), is(equalTo("A Description")));
	}

	@Test
	public void containsNoItemsIfNonePassedToConstructor() {
		OptionGroup anOptionGroup = new OptionGroup();
		
		assertThat(anOptionGroup.size(), is(equalTo(0)));
	}
	
	@Test
	public void containsItemsPassedToConstructor() {
		OptionGroup anOptionGroup = new OptionGroup(dummyOption0);
		
		assertThat(anOptionGroup.get(0), is(equalTo(dummyOption0)));
	}
	
	// ADD
	
	@Test (expected = NullPointerException.class)
	public void cannotAddNullOptions() {
		optionGroup.addOptions((OptionItem) null);
	}
	
	@Test
	public void canAddMultipleItems() {
		optionGroup.addOptions(dummyOption0, dummyOption1);
		
		assertThat(optionGroup.get(0), is(equalTo(dummyOption0)));
		assertThat(optionGroup.get(1), is(equalTo(dummyOption1)));
		assertThat(optionGroup.size(), is(equalTo(2)));
	}
	
	@Test
	public void listenerNotifiedOfItemAdded() {
		optionGroup.addListChangeListener(changeListener);
		
		optionGroup.addOptions(dummyOption0);
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotifiedOnceIfMultipleItemsAddedTogether() {
		optionGroup.addListChangeListener(changeListener);
		
		optionGroup.addOptions(dummyOption0, dummyOption1);
		
		verify(changeListener, Mockito.atMost(1)).onListChange();
	}
	
	@Test
	public void listenerNotifiedTwiceIfAddedSeparately() {
		optionGroup.addListChangeListener(changeListener);
		
		optionGroup.addOptions(dummyOption0, dummyOption1);
		
		verify(changeListener, Mockito.times(1)).onListChange();
	}	
	
	// REPLACE
	
	@Test
	public void canReplaceItem() {
		optionGroup.addOptions(dummyOption0);
		
		optionGroup.replace(0, dummyOption1);
		
		assertThat(optionGroup.get(0), is(equalTo(dummyOption1)));
		assertThat(optionGroup.size(), is(equalTo(1)));
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void cannotReplaceAbsentItem() {
		optionGroup.addOptions(dummyOption0);
		
		optionGroup.replace(4, dummyOption1);
	}
	
	@Test (expected = NullPointerException.class)
	public void cannotReplaceWithNullItem() {
		optionGroup.addOptions(dummyOption0);
		
		optionGroup.replace(0, null);
	}
	
	@Test
	public void listenerNotifiedOnReplace() {
		optionGroup.addOptions(dummyOption0);
		optionGroup.addListChangeListener(changeListener);
				
		optionGroup.replace(0, dummyOption1);
		
		verify(changeListener).onListChange();
	}
	
	// REMOVE INDEX
	
	@Test
	public void canRemoveIndex() {
		optionGroup.addOptions(dummyOption0);
		
		optionGroup.remove(0);
		
		assertThat(optionGroup.size(), is(equalTo(0)));
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void cannotRemoveAbsentIndex() {		
		optionGroup.remove(4);
	}
	
	@Test
	public void listenerNotifiedOnRemoveIndex() {
		optionGroup.addOptions(dummyOption0);
		optionGroup.addListChangeListener(changeListener);
				
		optionGroup.remove(0);
		
		verify(changeListener).onListChange();
	}
	
	// REMOVE OBJECT
	
	@Test
	public void canRemoveItem() {
		optionGroup.addOptions(dummyOption0);
		
		optionGroup.remove(dummyOption0);
		
		assertThat(optionGroup.size(), is(equalTo(0)));
	}
	
	@Test 
	public void nothingHappensWhenRemovingAbsentItem() {		
		optionGroup.remove(dummyOption0);
	}
	
	@Test
	public void listenerNotifiedOnRemoveItem() {
		optionGroup.addOptions(dummyOption0);
		optionGroup.addListChangeListener(changeListener);
				
		optionGroup.remove(dummyOption0);
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotNotifiedOnRemoveItemIfItemIsAbsent() {
		optionGroup.addOptions(dummyOption0);
		optionGroup.addListChangeListener(changeListener);
				
		optionGroup.remove(dummyOption1);
		
		verify(changeListener, never()).onListChange();
	}
	
	// CLEAR
	
	@Test
	public void canClearItems() {
		optionGroup.addOptions(dummyOption0, dummyOption1);
		
		optionGroup.clearOptions();
		
		assertThat(optionGroup.size(), is(equalTo(0)));
	}
	
	@Test
	public void listenerNotifiedOnClearIfListContainsItems() {
		optionGroup.addOptions(dummyOption0);
		optionGroup.addListChangeListener(changeListener);
		
		optionGroup.clearOptions();
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotNotifiedOnClearIfListIsEmpty() {
		optionGroup.addListChangeListener(changeListener);
		
		optionGroup.clearOptions();
		
		verify(changeListener, Mockito.never()).onListChange();
	}
	
	// ITERATOR
	
	@Test
	public void iteratorListsAllItems() {
		optionGroup.addOptions(dummyOption0, dummyOption1);
		
		Iterator<OptionItem> iterator = optionGroup.iterator();
		assertThat(iterator.next(), is(equalTo(dummyOption0)));
		assertThat(iterator.next(), is(equalTo(dummyOption1)));
		assertThat(iterator.hasNext(), is(equalTo(false)));
		
	}
	
	// LISTENERS
	
	@Test (expected = NullPointerException.class)
	public void cannotAddNullListener() {
		optionGroup.addListChangeListener(null);
	}
	
	// TO STRING
	
	@Test
	public void toStringDoesNotCrash() {
		optionGroup.toString();
	}
	
		
}
