package paul.wintz.utils;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
@RunWith(MockitoJUnitRunner.class)
public class ObservableListTest {

	ObservableList<String> observableList = new ObservableList<>();

	@Mock ObservableList.OnListChangeListener changeListener;
	
	String item0 = "dummy item 0";
	String item1 = "dummy item 1";
	
	// CONSTRUCTORS
	@Test
	public void containsNoItemsIfNonePassedToConstructor() {
		ObservableList<String> anOptionGroup = new ObservableList<>();
		
		assertThat(anOptionGroup.size(), is(equalTo(0)));
	}
	
	@Test
	public void containsItemPassedToConstructor() {
		ObservableList<String> anOptionGroup = new ObservableList<String>(item0);
		
		assertThat(anOptionGroup.get(0), is(equalTo(item0)));
	}
	
	@Test
	public void containsItemsPassedToConstructor() {
		ObservableList<String> anOptionGroup = new ObservableList<String>(Arrays.asList(item0, item1));
		
		assertThat(anOptionGroup.get(0), is(equalTo(item0)));
		assertThat(anOptionGroup.get(1), is(equalTo(item1)));
	}
	
	// ADD
	
	@Test (expected = NullPointerException.class)
	public void cannotAddNullOptions() {
		observableList.addAll(null);
	}
	
	@Test
	public void canAddMultipleItems() {
		observableList.addAll(asList(item0, item1));
		
		assertThat(observableList.get(0), is(equalTo(item0)));
		assertThat(observableList.get(1), is(equalTo(item1)));
		assertThat(observableList.size(), is(equalTo(2)));
	}
	
	@Test
	public void listenerNotifiedOfItemAdded() {
		observableList.addListChangeListener(changeListener);
		
		observableList.add(item0);
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotifiedOnceIfMultipleItemsAddedTogether() {
		observableList.addListChangeListener(changeListener);
		
		observableList.addAll(asList(item0, item1));
		
		verify(changeListener, Mockito.atMost(1)).onListChange();
	}
	
	@Test
	public void listenerNotifiedTwiceIfAddedSeparately() {
		observableList.addListChangeListener(changeListener);
		
		observableList.add(item0);
		observableList.add(item1);
		
		verify(changeListener, Mockito.times(2)).onListChange();
	}	
	
	// REPLACE
	
	@Test
	public void canReplaceItem() {
		observableList.add(item0);
		
		observableList.set(0, item1);
		
		assertThat(observableList.get(0), is(equalTo(item1)));
		assertThat(observableList.size(), is(equalTo(1)));
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void cannotReplaceAbsentItem() {
		observableList.add(item0);
		
		observableList.set(4, item1);
	}
	
	@Test (expected = NullPointerException.class)
	public void cannotReplaceWithNullItem() {
		observableList.add(item0);
		
		observableList.set(0, null);
	}
	
	@Test
	public void listenerNotifiedOnReplace() {
		observableList.add(item0);
		observableList.addListChangeListener(changeListener);
				
		observableList.set(0, item1);
		
		verify(changeListener).onListChange();
	}
	
	// REMOVE INDEX
	
	@Test
	public void canRemoveIndex() {
		observableList.add(item0);
		
		observableList.remove(0);
		
		assertThat(observableList.size(), is(equalTo(0)));
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void cannotRemoveAbsentIndex() {		
		observableList.remove(4);
	}
	
	@Test
	public void listenerNotifiedOnRemoveIndex() {
		observableList.add(item0);
		observableList.addListChangeListener(changeListener);
				
		observableList.remove(0);
		
		verify(changeListener).onListChange();
	}
	
	// REMOVE OBJECT
	
	@Test
	public void canRemoveItem() {
		observableList.add(item0);
		
		observableList.remove(item0);
		
		assertThat(observableList.size(), is(equalTo(0)));
	}
	
	@Test 
	public void nothingHappensWhenRemovingAbsentItem() {		
		observableList.remove(item0);
	}
	
	@Test
	public void listenerNotifiedOnRemoveItem() {
		observableList.add(item0);
		observableList.addListChangeListener(changeListener);
				
		observableList.remove(item0);
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotNotifiedOnRemoveItemIfItemIsAbsent() {
		observableList.add(item0);
		observableList.addListChangeListener(changeListener);
				
		observableList.remove(item1);
		
		verify(changeListener, never()).onListChange();
	}
	
	// CLEAR
	
	@Test
	public void canClearItems() {
		observableList.addAll(asList(item0, item1));
		
		observableList.clear();
		
		assertThat(observableList.size(), is(equalTo(0)));
	}
	
	@Test
	public void listenerNotifiedOnClearIfListContainsItems() {
		observableList.add(item0);
		observableList.addListChangeListener(changeListener);
		
		observableList.clear();
		
		verify(changeListener).onListChange();
	}
	
	@Test
	public void listenerNotNotifiedOnClearIfListIsEmpty() {
		observableList.addListChangeListener(changeListener);
		
		observableList.clear();
		
		verify(changeListener, Mockito.never()).onListChange();
	}
	
	// ITERATOR
	
	@Test
	public void iteratorListsAllItems() {
		observableList.addAll(asList(item0, item1));
		
		Iterator<?> iterator = observableList.iterator();
		assertThat(iterator.next(), is(equalTo(item0)));
		assertThat(iterator.next(), is(equalTo(item1)));
		assertThat(iterator.hasNext(), is(equalTo(false)));
		
	}
	
	// LISTENERS
	
	@Test (expected = NullPointerException.class)
	public void cannotAddNullListener() {
		observableList.addListChangeListener(null);
	}
	
	@Test
	public void readdingListenerDoesNothing() {
		observableList.addListChangeListener(changeListener);
		observableList.addListChangeListener(changeListener);
		
		observableList.add(item0);
		
		verify(changeListener).onListChange();
	}
	
	// TO STRING
	
	@Test
	public void toStringDoesNotCrash() {
		observableList.toString();
	}
	
		
}
