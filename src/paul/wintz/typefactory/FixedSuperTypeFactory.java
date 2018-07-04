package paul.wintz.typefactory;

import com.google.common.collect.ImmutableMap;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Creates instances that are a subclass of given types. The available supertypes can
 * be accessed by calling getBaseTypes().
 */
public class FixedSuperTypeFactory<T> {

    private ImmutableMap<Class<? extends T>, Instantiator<? extends T>> typeToInstantiatorMap;

    public T make(Class<? extends T> baseType) {
        checkClassIsSupported(baseType);
        return baseType.cast(typeToInstantiatorMap.get(baseType).instance());
    }

    public Set<Class<? extends T>> getBaseTypes() {
        return typeToInstantiatorMap.keySet();
    }

    public boolean canMake(Class<? extends T> viewClass) {
        return typeToInstantiatorMap.containsKey(viewClass);
    }

    public void checkClassIsSupported(Class<? extends T> baseType) {
        if (!canMake(baseType)) {
            throw new ClassNotSupported(baseType);
        }
    }

    private FixedSuperTypeFactory(Builder<T> builder){
        typeToInstantiatorMap = builder.mapBuilder.build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    public static class Builder<T> {

        private ImmutableMap.Builder<Class<? extends T>, Instantiator<? extends T>> mapBuilder = new ImmutableMap.Builder<>();

        /**
         * @param instantiator A callback that creates an instance of a class that extends S.
         * @param <S> the base type. Theinstance by the Instantiator must extend S,
         *           and S must extend T (the type for the TypeFactory).
         */
        public <S extends T> Builder<T> putType(Class<S> baseType, Instantiator<S> instantiator) {
            mapBuilder.put(baseType, instantiator);
            return this;
        }

        public FixedSuperTypeFactory<T> build(){
            return new FixedSuperTypeFactory<>(this);
        }

    }

}
