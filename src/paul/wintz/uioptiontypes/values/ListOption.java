package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Arrays.asList;

public class ListOption<T> extends ValueOption<T> {

    private final ImmutableList<T> list;

    private ListOption(Builder<T> builder) {
        super(builder);
        this.list = ImmutableList.copyOf(builder.items);
    }

    public int getSize() {
        return list.size();
    }

    private int getIndex() {
        return list.indexOf(getValue());
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> extends ValueOption.Builder<T, Builder<T>> {

        private List<T> items = new ArrayList<>();

        public Builder<T> add(T item) {
            this.items.add(item);
            return this;
        }

        public Builder<T> addAll(Collection<? extends T> items) {
            this.items.addAll(items);
            return this;
        }

        public Builder<T> fromEnumeration(Class<T> enumeration) {
            return addAll(asList(enumeration.getEnumConstants()));
        }

        public ListOption<T> build() {
            checkState(!items.isEmpty(), "List is empty");
            if(initial == null) {
                initial = items.get(0);
            } else {
                checkState(items.contains(initial));
            }
            addValidityEvaluator(value -> items.contains(value));
            return new ListOption<>(this);
        }

        private Builder() {
            // Prevent instantiation
        }

    }

    @Nonnull
    public ImmutableList<T> getList() {
        return list;
    }

    @Override
    public String toString(){
        return String.format("ListOption. Selected: %s (%d of %d)", getValue(), getIndex() + 1, getSize());
    }
}
