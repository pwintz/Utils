package paul.wintz.typefactory;

@FunctionalInterface
public interface Instantiator<T> {
    T instance();
}
