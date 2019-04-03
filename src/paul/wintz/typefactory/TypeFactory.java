package paul.wintz.typefactory;

import com.google.common.collect.ImmutableMap;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates instances that are a subclass of given types. The available supertypes can
 * be accessed by calling getBaseTypes().
 */
public class TypeFactory {

    private final ImmutableMap<Class<?>, Instantiator<?>> typeToInstantiatorMap;

    @SuppressWarnings("unchecked") // Instantiator is guaranteed to castable by Builder.
    public <T> Instantiator<? extends T> getInstantiator(Class<T> baseType) {
        checkClassIsSupported(baseType);
        return (Instantiator<? extends T>) typeToInstantiatorMap.get(baseType);
    }

    public <T> T make(Class<T> baseType) {
        return getInstantiator(baseType).instance();
    }

    public Set<Class<?>> getBaseTypes() {
        return typeToInstantiatorMap.keySet();
    }

    public boolean canMake(Class<?> viewClass) {
        return typeToInstantiatorMap.containsKey(viewClass);
    }

    public void checkClassIsSupported(Class<?> baseType) {
        ClassNotSupportedException.checkClassIsSupported(baseType, typeToInstantiatorMap.keySet());
    }

    private TypeFactory(Builder builder){
        typeToInstantiatorMap = builder.mapBuilder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImmutableMap.Builder<Class<?>, Instantiator<?>> mapBuilder = new ImmutableMap.Builder<>();

        /**
         * @param instantiator A callback that creates an instance of a class that extends T.
         * @param <T> the base type. The instance created by the Instantiator must extend T.
         */
        public <T> Builder putType(Class<T> baseType, Instantiator<? extends T> instantiator) {
            checkNotNull(baseType, "baseType was null");
            checkNotNull(instantiator, "Instantiator was null");
            mapBuilder.put(baseType, instantiator);
            return this;
        }

        public Builder onVoidSelectedAction(Runnable onVoidSelected){
            putType(Void.class, () -> {
                onVoidSelected.run();
                return null;
            });
            return this;
        }

        public TypeFactory build(){
            return new TypeFactory(this);
        }

    }

}
