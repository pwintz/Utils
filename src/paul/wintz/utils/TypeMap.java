package paul.wintz.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.swing.UIManager.put;
import static paul.wintz.utils.logging.Lg.makeTAG;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;

@SuppressWarnings("serial")
@Deprecated
public class TypeMap<T> {
    private static final String TAG = makeTAG(TypeMap.class);
    private final HashMap<String, Class<? extends T>> nameToTypeMap = new LinkedHashMap<>();

    @SafeVarargs
    public TypeMap(Class<? extends T>... types){

        for(final Class<? extends T> type : types){
            add(type);
        }
    }

    public void add(Class<? extends T> type) {
        nameToTypeMap.put(type.getSimpleName(), type);
    }

    public Set<String> getTypeNames() {
        return nameToTypeMap.keySet();
    }

    public T instantiateStatic(String className) {
        return instantiate(className, null);
    }

    @SuppressWarnings("unchecked")
    public T instantiate(String name, Object outerObject) {
        try {

            final Class<? extends T> class1 = checkNotNull(nameToTypeMap.get(name));

            if(isStatic(class1))
                return class1.newInstance();
            else if(outerObject != null){
                //for non-static classes, we have to pass the outerObject to the constructor
                final Constructor<?> constructor = class1.getDeclaredConstructors()[0];
                return (T) constructor.newInstance(outerObject);
            } else
                throw new NullPointerException(name + " is not static but outerObject was null");

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Lg.w(TAG, "Failed to instantiate: " + name, e);
            return null;
        }
    }

    private static boolean isStatic(Class<?> aClass) {
        return  aClass.getEnclosingClass() == null || Modifier.isStatic(aClass.getModifiers());
    }

}
