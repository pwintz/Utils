package paul.wintz.utils;

import java.util.Set;

public abstract class AbstractFactory<T> {

    protected abstract TypeMap<T> getTypeMap();

    public final Set<String> getNames() {
        return getTypeMap().keySet();
    }

    public final T make(String name) {
        return getTypeMap().instantiate(name, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{names: " + getNames() + "}";
    }

}
