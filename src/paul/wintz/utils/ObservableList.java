package paul.wintz.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

import java.util.*;

public class ObservableList<T> implements Iterable<T> {

	private final List<T> items = new ArrayList<>();
	private final Set<Observer> observers = new HashSet<>();

	public interface Observer {
		void onListChange();
	}

	public ObservableList() {
	}
	
	public ObservableList(T opts) {
		addAll(singletonList(opts));
	}
	
	public ObservableList(List<T> opts) {
		addAll(opts);
	}
	
	public final void clear(){
		if(items.isEmpty()) return;
		
		items.clear();
		notifyOnChangeListeners();
	}
	
	public void add(T item) {
		items.add(item);
		notifyOnChangeListeners();
	}

	public final void addAll(final List<? extends T> opts) {
		for (final T opt : opts) {
			items.add(checkNotNull(opt));
		}
		notifyOnChangeListeners();
	}

	public final void set(final int index, final T item){
		items.set(index, checkNotNull(item));
		notifyOnChangeListeners();
	}
	
	public final void remove(final int index) {
		items.remove(index);
		notifyOnChangeListeners();
	}
	
	public final void remove(T item) {
		if(items.remove(item)) {
			notifyOnChangeListeners();
		}
	}

	public final T get(final int index){
		return items.get(index);
	}

	public int size() {
		return items.size();
	}

	@Override
	public final Iterator<T> iterator() {
		return items.iterator();
	}

	public final void addListChangeListener(final Observer changeListener){
		observers.add(checkNotNull(changeListener));
	}

	public final void notifyOnChangeListeners(){
		for(final Observer l : observers){
			l.onListChange();
		}
	}

	@Override
	public String toString() {
		return String.format("ListeningList{(%d items), (%d listeners)", items.size(), observers.size());
	}
	
}
