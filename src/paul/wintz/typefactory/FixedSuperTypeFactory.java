package paul.wintz.typefactory;

import com.google.common.collect.ImmutableMap;

import java.util.Set;

/**
 * Creates instances that are a subclass of given types. The available supertypes can
 * be accessed by calling getBaseTypes().
 */
public class FixedSuperTypeFactory<T> {

    private final ImmutableMap<Class<? extends T>, Instantiator<? extends T>> typeToInstantiatorMap;

    public <Q extends T> Q make(Class<Q> baseType) {
        checkClassIsSupported(baseType);
        return baseType.cast(typeToInstantiatorMap.get(baseType).instance());
    }

    public Set<Class<? extends T>> getBaseTypes() {
        return typeToInstantiatorMap.keySet();
    }

    public boolean canMake(Class<? extends T> viewClass) {
        return typeToInstantiatorMap.containsKey(viewClass);
    }

    private void checkClassIsSupported(Class<? extends T> baseType) {
        ClassNotSupportedException.checkClassIsSupported(baseType, typeToInstantiatorMap.keySet());
    }

    private FixedSuperTypeFactory(Builder<T> builder){
        typeToInstantiatorMap = builder.mapBuilder.build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    public Instantiator<? extends T> getInstantiator(Class<? extends T> baseType) {
        checkClassIsSupported(baseType);
        return typeToInstantiatorMap.get(baseType);
    }

    public static class Builder<T> {

        private ImmutableMap.Builder<Class<? extends T>, Instantiator<? extends T>> mapBuilder = new ImmutableMap.Builder<>();

        /**
         * @param instantiator A callback that creates an instance of a class that extends S.
         * @param <S> the base type. The instance created by the Instantiator must extend S,
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
