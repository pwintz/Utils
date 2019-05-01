package paul.wintz.uioptiontypes;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValuesSuppliers implements Iterable<Map.Entry<String, DoubleSupplier>> {
    private static final String TAG = Lg.makeTAG(ValuesSuppliers.class);

    private final Map<String, DoubleSupplier> doubleSuppliers = new HashMap<>();
    private final Set<Consumer<Set<String>>> suppliersChangeListeners = new HashSet<>();

    public void putDoubleSupplier(String name, DoubleSupplier supplier){
        doubleSuppliers.put(checkNotNull(name), checkNotNull(supplier));
        notifySuppliersChangeListeners();
    }

    public void removeDoubleSupplier(String name){
        doubleSuppliers.remove(name);
        notifySuppliersChangeListeners();
    }

    public void addSuppliersChangeListener(Consumer<Set<String>> listener) {
        suppliersChangeListeners.add(checkNotNull(listener));
    }

    public void removeSuppliersChangeListener(Consumer<Set<String>> listener) {
        suppliersChangeListeners.remove(checkNotNull(listener));
    }

    private void notifySuppliersChangeListeners() {
        suppliersChangeListeners.forEach(setConsumer -> setConsumer.accept(this.getDoubleSupplierNames()));
    }

    public Set<String> getDoubleSupplierNames(){
        return doubleSuppliers.keySet();
    }

    @Nonnull public DoubleSupplier get(String name){
        return doubleSuppliers.get(name);
    }

    @Nonnull public Iterator<Map.Entry<String, DoubleSupplier>> iterator(){
        return doubleSuppliers.entrySet().iterator();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private final Map<String, DoubleSupplier> doubleSuppliers = new HashMap<>();

        private Builder (){}

        public Builder putDoubleSupplier(String name, DoubleSupplier supplier){
            doubleSuppliers.put(checkNotNull(name), checkNotNull(supplier));
            Lg.v(TAG, "Put double Supplier: %s with name '%s'", supplier, name);
            return this;
        }

        public Builder addAlias(String alias, String target) {
            DoubleSupplier doubleSupplier = checkNotNull(doubleSuppliers.get(target), "Alias: %s, target: %s", alias, target);
            doubleSuppliers.put(checkNotNull(alias), doubleSupplier);
            return this;
        }

        public ValuesSuppliers build(){
            return new ValuesSuppliers(this);
        }

    }

    private ValuesSuppliers(Builder builder){
        doubleSuppliers.putAll(builder.doubleSuppliers);
    }

}
