package paul.wintz.uioptiontypes.values;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static java.util.Arrays.asList;

import java.util.*;

public class ListOption<T> extends ValueOption<Integer> implements Iterable<T> {

    private final List<T> list;

    private int selectedNdx = 0;

    private Listener<T> listener;

    public interface Listener<T> {
        void onSelect(T t);
    }

    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    public ListOption(Builder<T> builder) {
        super(builder);
        this.list = builder.items;
        setSelected(selectedNdx);
    }

    public void add(Collection<T> items) {
        list.addAll(items);

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

        checkArgument(indexToSelect != -1, "The item '%s' is not a member of the list", item);

        return indexToSelect;
    }

    private void checkIndex(int n) {
        checkElementIndex(n, list.size());
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public String toString(){
        return " ListOption. Selected: " + getSelected() + " (" + (selectedNdx + 1) + " of " + getSize() + ")";
    }

    public static class Builder<T> extends ValueOption.Builder<Integer, Builder> {

        private List<T> items = new ArrayList<>();
        private T selectedItem;

        private Builder() {
            // Prevent instantiation
            // Set default values
            initial = 0;
        }

        public Builder<T> listItems(List<T> items) {
            this.items.addAll(items);
            return this;
        }

        public Builder<T> fromEnumeration(Class<T> enumeration) {
            listItems(asList(enumeration.getEnumConstants()));
            return this;
        }

        public Builder<T> selectedItem(T item) {
            selectedItem = item;
            return this;
        }

        public ListOption<T> build() {
            return new ListOption<>(this);
        }

    }


}
