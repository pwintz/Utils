package paul.wintz.uioptiontypes;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.*;

public class ListOption<T> extends UserInputOption<List<T>> implements Iterable<T> {

	private final ArrayList<T> list = new ArrayList<>();

	private int selectedNdx = 0;

	private Listener<T> listener;

	public interface Listener<T> {
		void onSelect(T t);
	}

	public ListOption(String description, T... items) {
		super(description);
		add(items);
		setSelected(selectedNdx);
	}

	public void add(T... items) {
		add(Arrays.asList(items));
	}

	public void add(Collection<T> items) {
		for (final T item : items) {
			list.add(item);
		}

		setSelected(selectedNdx);
	}


	public void setSelected(T item) {
		setSelected(indexOfWithCheck(item));
	}

	public void setSelected(int n) {
		if(list.isEmpty()) return;
		checkIndex(n);

		selectedNdx = n;

		if(listener != null) {
			listener.onSelect(list.get(n));
		}
	}

	public void setCallback(Listener<T> listener){
		this.listener = listener;
	}

	public int getSize() {
		return list.size();
	}

	public boolean isEmpty(){
		return list.isEmpty();
	}

	public T getSelected(){
		if(list.isEmpty()) return null;
		return list.get(selectedNdx);
	}

	public String getSelectedName(){
		return getSelected().toString();
	}

	private int indexOfWithCheck(T item) {
		final int indexToSelect = list.indexOf(item);

		if (indexToSelect == -1)
			throw new IllegalArgumentException("The item '" + item + "' is not a member of the list");

		return indexToSelect;
	}

	private void checkIndex(int n) {
		checkElementIndex(n, list.size());
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public String toString(){
		return description + " ListOption. Selected: " + getSelected() + " (" + (selectedNdx + 1) + " of " + getSize() + ")";
	}

}
