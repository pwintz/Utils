package paul.wintz.uioptiontypes;

import static java.util.Arrays.asList;

import paul.wintz.utils.ObservableList;

public class OptionGroup extends ObservableList<OptionItem> implements OptionItem {

	private final String description;
		
	public OptionGroup(OptionItem... opts) {
		super(asList(opts));
		this.description = this.getClass().getSimpleName();
	}
	
	public OptionGroup(String description, OptionItem... opts) {
		super(asList(opts));
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public void addAll(OptionItem... items) {
		super.addAll(asList(items));
	}

}
