package paul.wintz.mvp;

import paul.wintz.typefactory.FixedSuperTypeFactory;
import paul.wintz.typefactory.Instantiator;
import paul.wintz.typefactory.TypeFactory;
import paul.wintz.uioptiontypes.values.ListOption;
import paul.wintz.utils.logging.Lg;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

// A presenter that a supports selecting between a list of presenters.
public class PresenterFactoryPresenter<T> {
    private static final String TAG = Lg.makeTAG(PresenterFactoryPresenter.class);

    // PresenterSelectionView is an interface that takes a list of presenters then,
    // when a presenters is selected, a callback is triggered.
    public interface PresenterSelectionView {
        TypeFactory getViewFactory();
        void setPresentersOption(ListOption<Class<? extends Presenter<?>>> presenterChoices);
    }

    private PresenterFactoryPresenter(Builder<T> builder) {
        FixedSuperTypeFactory<Presenter<?>> presenterFactory = builder.presenterFactoryBuilder.build();
        TypeFactory viewFactory = builder.viewFactory;
        PresenterSelectionView presenterSelectionView = builder.presenterSelectionView;
        onPresenterChangeListener = builder.onPresenterChangeListener;

        presenterSelectionView.setPresentersOption(ListOption.<Class<? extends Presenter<?>>>builder()
                .addAll(presenterFactory.getBaseTypes())
                .displayNameMapper(Class::getSimpleName)
                .addViewValueChangeCallback(basePresenterType -> {
                    Presenter<?> presenter = presenterFactory.make(basePresenterType);
                    presenter.createAndSetView(viewFactory);

                    //noinspection unchecked - We check that every added Presenter class extends T.
                    emitPresenterChanged((T) presenter);
                    Lg.i(TAG, "Presenter changed to " + basePresenterType.getSimpleName());
                })
                .build());
    }

    public interface ValueChangedListener<T> {
        void notify(T newPresenter);
    }

    private final ValueChangedListener<T> onPresenterChangeListener;

    private void emitPresenterChanged(T newPresenter) {
        onPresenterChangeListener.notify(newPresenter);
    }

    public static <T> Builder<T> builder(Class<T> requiredSupertype) {
        return new Builder<>(requiredSupertype);
    }

    public static class Builder<T> {
        private final Class<T> requiredSupertype;
        private PresenterSelectionView presenterSelectionView;
        private TypeFactory viewFactory;
        private FixedSuperTypeFactory.Builder<Presenter<?>> presenterFactoryBuilder = FixedSuperTypeFactory.builder();
        private ValueChangedListener<T> onPresenterChangeListener = (newPresenter) -> {};

        private Builder(Class<T> requiredSupertype) {
            this.requiredSupertype = requiredSupertype;
        }

        public Builder<T> setPresenterSelectionView(PresenterSelectionView presenterSelectionView){
            this.presenterSelectionView = checkNotNull(presenterSelectionView, "presenterSelectionView was null");
            viewFactory = checkNotNull(presenterSelectionView.getViewFactory(),"viewFactory was null");
            return this;
        }

        public <P extends Presenter<V>, V> Builder<T> addPresenter(
                Class<P> presenterClass, Class<V> viewClass, Instantiator<P> presenterInstantiator) {
            checkNotNull(viewFactory, "setPresenterSelectionView() must be called before addPresenter()");
            viewFactory.checkClassIsSupported(viewClass);

            checkArgument(requiredSupertype.isAssignableFrom(presenterClass), "Presenter class must extend %s", requiredSupertype);

            presenterFactoryBuilder.putType(presenterClass, () -> {
                P presenter = presenterInstantiator.instance();
                presenter.setView(viewFactory.make(viewClass));
                return presenter;
            });

            return this;
        }

        public Builder<T> setPresenterChangedListener(ValueChangedListener<T> onPresenterChangeListener) {
            this.onPresenterChangeListener = checkNotNull(onPresenterChangeListener);
            return this;
        }

        public PresenterFactoryPresenter<T> build(){
            checkNotNull(presenterSelectionView);
            checkNotNull(viewFactory);
            return new PresenterFactoryPresenter<>(this);
        }

    }


}
