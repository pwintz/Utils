package paul.wintz.mvp;

import paul.wintz.typefactory.FixedSuperTypeFactory;
import paul.wintz.typefactory.Instantiator;
import paul.wintz.typefactory.TypeFactory;
import paul.wintz.uioptiontypes.values.ListOption;
import paul.wintz.utils.logging.Lg;

import static com.google.common.base.Preconditions.checkNotNull;

// A presenter that a supports selecting between a list of presenters.
public class PresenterFactoryPresenter {
    private static final String TAG = Lg.makeTAG(PresenterFactoryPresenter.class);

    // PresenterSelectionView is an interface that takes a list of presenters then,
    // when a presenters is selected, a callback is triggered.
    public interface PresenterSelectionView {
        TypeFactory getViewFactory();
        void setPresentersOption(ListOption<Class<? extends Presenter<?>>> presenterChoices);
    }

    private PresenterFactoryPresenter(Builder builder) {
        FixedSuperTypeFactory<Presenter<?>> presenterFactory = builder.presenterFactoryBuilder.build();
        TypeFactory viewFactory = builder.viewFactory;
        PresenterSelectionView presenterSelectionView = builder.presenterSelectionView;
        onPresenterChangeListener = builder.onPresenterChangeListener;

        presenterSelectionView.setPresentersOption(ListOption.<Class<? extends Presenter<?>>>builder()
                .addAll(presenterFactory.getBaseTypes())
                .viewValueChangeCallback(basePresenterType -> {
                    Presenter<?> presenter = presenterFactory.make(basePresenterType);
                    presenter.createAndSetView(viewFactory);
                    emitPresenterChanged(presenter);
                    Lg.i(TAG, "Presenter changed to " + basePresenterType.getSimpleName());
                })
                .build());
    }

    public interface ValueChangedListener {
        void notify(Presenter<?> newPresenter);
    }

    private final ValueChangedListener onPresenterChangeListener;

    private void emitPresenterChanged(Presenter<?> newPresenter) {
        onPresenterChangeListener.notify(newPresenter);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PresenterSelectionView presenterSelectionView;
        private TypeFactory viewFactory;
        private FixedSuperTypeFactory.Builder<Presenter<?>> presenterFactoryBuilder = FixedSuperTypeFactory.builder();
        private ValueChangedListener onPresenterChangeListener = (v) -> {};

        public Builder setPresenterSelectionView(PresenterSelectionView presenterSelectionView){
            this.presenterSelectionView = checkNotNull(presenterSelectionView, "presenterSelectionView was null");
            viewFactory = checkNotNull(presenterSelectionView.getViewFactory(),"viewFactory was null");
            return this;
        }

        public <P extends Presenter<V>, V> Builder addPresenter(
                Class<P> presenterClass, Class<V> viewClass, Instantiator<P> presenterInstantiator) {
            checkNotNull(viewFactory, "setPresenterSelectionView() must be called before addPresenter()");
            viewFactory.checkClassIsSupported(viewClass);

            presenterFactoryBuilder.putType(presenterClass, () -> {
                P presenter = presenterInstantiator.instance();
                presenter.setView(viewFactory.make(viewClass));
                return presenter;
            });

            return this;
        }

        public Builder setPresenterChangedListener(ValueChangedListener onPresenterChangeListener) {
            this.onPresenterChangeListener = checkNotNull(onPresenterChangeListener);
            return this;
        }

        public PresenterFactoryPresenter build(){
            checkNotNull(presenterSelectionView);
            checkNotNull(viewFactory);
            return new PresenterFactoryPresenter(this);
        }

        private Builder() {}

    }


}
