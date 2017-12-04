package paul.wintz.uioptiontypes;

import java.util.*;

public class OptionGroup extends OptionItem implements Iterable<OptionItem> {
	private static final String TAG = OptionGroup.class.getSimpleName();

	protected final List<OptionItem> options = new ArrayList<>();
	private final List<OnListChangeListener> changeListeners = new ArrayList<>();

	private static final int NO_CHANGE = -1;
	int firstIndexChanged = NO_CHANGE;

	public interface OnListChangeListener {
		void onListChange();
	}

	public OptionGroup(OptionItem... opts) {
		this("", opts);
		description = this.getClass().getSimpleName();
	}

	public OptionGroup(String description, OptionItem... opts) {
		super(description);

		addOptions(opts);
	}

	public final void clearOptions(){
		options.clear();
	}

	public final void addOptions(final OptionItem... opts) {
		addOptions(Arrays.asList(opts));
	}

	public final void addOptions(final List<? extends OptionItem> opts) {
		for (final OptionItem opt : opts) {
			options.add(opt);
		}
	}

	public final void replace(final int index, final OptionItem option){
		options.set(index, option);
	}

	public final OptionItem get(final int index){
		return options.get(index);
	}

	public int size() {
		return options.size();
	}

	@Override
	public final Iterator<OptionItem> iterator() {
		return options.iterator();
	}

	public final void addListChangeListener(final OnListChangeListener changeListener){
		changeListeners.add(changeListener);
	}

	public final void notifyOnChangeListeners(){

		for(final OnListChangeListener l : changeListeners){
			l.onListChange();
		}

	}

	@Override
	public String toString() {
		return String.format("%s (%d items)", description, options.size());
	}
}
