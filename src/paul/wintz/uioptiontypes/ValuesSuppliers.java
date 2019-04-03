package paul.wintz.uioptiontypes;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValuesSuppliers implements Iterable<Map.Entry<String, DoubleSupplier>> {
    private static final String TAG = Lg.makeTAG(ValuesSuppliers.class);

    private final Map<String, DoubleSupplier> doubleSuppliers = new HashMap<>();

    public void putDoubleSupplier(String name, DoubleSupplier supplier){
        doubleSuppliers.put(checkNotNull(name), checkNotNull(supplier));
    }

    public Set<String> getDoubleSupplierNames(){
        return doubleSuppliers.keySet();
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
