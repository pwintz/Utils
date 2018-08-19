package paul.wintz.utils;

import java.util.Set;

@Deprecated
public interface AbstractFactory<T, P> {
    Set<P> getKeys();
    T make(P name);
}
