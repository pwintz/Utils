package paul.wintz.mvp;

import paul.wintz.typefactory.TypeFactory;

// Each presenter needs its own unique view.
public interface Presenter<V> {
    Class<V> getViewClass();
    default void setView(V view) {};
    default void createAndSetView(TypeFactory viewFactory) {
        setView(viewFactory.make(getViewClass()));
    }

    default void enable(){}
    default void disable(){}

}
