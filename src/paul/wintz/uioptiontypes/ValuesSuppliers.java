package paul.wintz.uioptiontypes;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleSupplier;

public class ValuesSuppliers implements Iterable<Map.Entry<String, DoubleSupplier>> {

    private Map<String, DoubleSupplier> doubleSuppliers = new HashMap<>();

    public void putDoubleSupplier(String name, DoubleSupplier supplier){
        doubleSuppliers.put(name, supplier);
    }

    public Set<String> getDoubleSupplierNames(){
        return doubleSuppliers.keySet();
    }

    @Nonnull public Iterator<Map.Entry<String, DoubleSupplier>> iterator(){
        return doubleSuppliers.entrySet().iterator();
    }

//    public static Builder builder(){
//        return new Builder();
//    }
//
//    public static class Builder {
//
//        private Builder (){}
//
//        public ValuesSuppliers build(){
//            return new ValuesSuppliers();
//        }
//
//    }
//
//    private ValuesSuppliers(){
//
//    }
//
}
