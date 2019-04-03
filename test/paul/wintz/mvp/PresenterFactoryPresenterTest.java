package paul.wintz.mvp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import paul.wintz.typefactory.ClassNotSupportedException;
import paul.wintz.typefactory.TypeFactory;
import paul.wintz.uioptiontypes.values.ListOption;

import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PresenterFactoryPresenterTest {

    PresenterFactoryPresenter factoryPresenter;
    @Mock PresenterFactoryPresenter.ValueChangedListener presenterChangeListener;
    FakePresenterSelectionView presenterSelectionView = new FakePresenterSelectionView();

    @Before
    public void setup() {
        factoryPresenter = PresenterFactoryPresenter.builder()
                .setPresenterSelectionView(presenterSelectionView)
                .setPresenterChangedListener(presenterChangeListener)
                .addPresenter(PresenterA.class, PresenterA.View.class, PresenterA::new)
                .build();
    }

    @Test (expected = RuntimeException.class)
    public void builderWillNotAddPresenterIfViewFactoryCannotCreateView() {
        PresenterFactoryPresenter.builder()
                .setPresenterSelectionView(presenterSelectionView)
                .addPresenter(
                        PresenterNotInViewFactory.class, PresenterNotInViewFactory.View.class,
                        PresenterNotInViewFactory::new)
                .build();
    }

    @Test
    public void whenAPresenterIsSelectedListenerIsNotified() throws Exception {
        presenterSelectionView.emitPresenterSelected(PresenterA.class);

        verify(presenterChangeListener).notify(isNull(Presenter.class), isA(PresenterA.class));
    }

    @Test (expected = ClassNotSupportedException.class)
    public void noCallbackWhenAnUnrecognizedPresenterIsSelected() throws Exception {
        presenterSelectionView.emitPresenterSelected(UnrecognizedPresenter.class);
    }

    static class PresenterA implements Presenter<PresenterA.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {
        }

        static class View {
        }
    }

    static class PresenterNotInViewFactory implements Presenter<PresenterNotInViewFactory.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {
        }

        static class View {}
    }

    static class UnrecognizedPresenter implements Presenter<UnrecognizedPresenter.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {
        }

        static class View {
        }
    }

    private static class FakePresenterSelectionView implements PresenterFactoryPresenter.PresenterSelectionView {

        private ListOption<Class<? extends Presenter<?>>> presenterChoices;
        TypeFactory viewFactory = TypeFactory.builder()
                .putType(PresenterA.View.class, PresenterA.View::new)
                .build();

        @Override
        public TypeFactory getViewFactory() {
            return viewFactory;
        }

        @Override
        public void setPresentersOption(ListOption<Class<? extends Presenter<?>>> presenterChoices) {
            this.presenterChoices = presenterChoices;
        }

        public void emitPresenterSelected(Class<? extends Presenter<?>> presenterClass) {
            presenterChoices.emitViewValueChanged(presenterClass);
        }
    }
}