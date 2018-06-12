package paul.wintz.uioptiontypes;

import static java.util.Arrays.asList;

import paul.wintz.utils.ObservableList;

public class OptionGroup extends ObservableList<OptionItem> implements OptionItem {

    public OptionGroup(OptionItem... opts) {
        super(asList(opts));
    }

    public void addAll(OptionItem... items) {
        super.addAll(asList(items));
    }

}
