package paul.wintz.mvp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import paul.wintz.typefactory.ClassNotSupportedException;
import paul.wintz.typefactory.TypeFactory;
import paul.wintz.uioptiontypes.values.ListOption;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PresenterFactoryPresenterTest {

    PresenterFactoryPresenter factoryPresenter;
    @Spy PresenterFactoryPresenter.ValueChangedListener<RequiredSupertype> presenterChangeListener;
    MockView presenterSelectionView = new MockView();

    @Before
    public void setup() {
        factoryPresenter = PresenterFactoryPresenter.builder(RequiredSupertype.class)
                .setPresenterSelectionView(presenterSelectionView)
                .setPresenterChangedListener(presenterChangeListener)
                .addPresenter(ValidPresenter.class, ValidPresenter.View.class, ValidPresenter::new)
                .addPresenter(AnotherValidPresenter.class, AnotherValidPresenter.View.class, AnotherValidPresenter::new)
                .build();
    }

    @Test (expected = ClassNotSupportedException.class)
    public void builderWillNotAddPresenterIfViewFactoryCannotCreateView() {
        PresenterFactoryPresenter.builder(Object.class)
                .setPresenterSelectionView(presenterSelectionView)
                .addPresenter(
                        PresenterNotInViewFactory.class, PresenterNotInViewFactory.View.class,
                        PresenterNotInViewFactory::new)
                .build();
    }

    @Test (expected = IllegalArgumentException.class)
    public void cannotAddPresenterIfDoesNotExtendRequiredSupertype() {
        PresenterFactoryPresenter.builder(RequiredSupertype.class)
                .setPresenterSelectionView(presenterSelectionView)
                .addPresenter(
                        PresenterWithoutRequiredSupertype.class, PresenterWithoutRequiredSupertype.View.class,
                        PresenterWithoutRequiredSupertype::new)
                .build();
    }

    @Test
    public void whenAPresenterIsSelectedListenerIsNotified() throws Exception {
        presenterSelectionView.presentersListOption.emitViewValueChanged(AnotherValidPresenter.class);

        verify(presenterChangeListener).notify(isA(AnotherValidPresenter.class));
    }

    @Test
    public void noCallbackWhenAnUnrecognizedPresenterIsSelected() throws Exception {
        assertFalse(presenterSelectionView.presentersListOption.emitViewValueChanged(UnrecognizedPresenter.class));
    }

    interface RequiredSupertype {
    }

    static class ValidPresenter implements Presenter<ValidPresenter.View>, RequiredSupertype {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {}

        static class View {}
    }

    static class AnotherValidPresenter implements Presenter<AnotherValidPresenter.View>, RequiredSupertype {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {}

        static class View {}
    }

    static class PresenterWithoutRequiredSupertype implements Presenter<PresenterWithoutRequiredSupertype.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {}

        static class View {}
    }

    static class PresenterNotInViewFactory implements Presenter<PresenterNotInViewFactory.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {}

        interface View {}
    }

    static class UnrecognizedPresenter implements Presenter<UnrecognizedPresenter.View> {

        @Override
        public Class<View> getViewClass() {
            return View.class;
        }

        @Override
        public void setView(View view) {}

        interface View {}
    }

    private static class MockView implements PresenterFactoryPresenter.PresenterSelectionView {
        private ListOption<Class<? extends Presenter<?>>> presentersListOption;

        @Override
        public TypeFactory getViewFactory() {
            return TypeFactory.builder()
                    .putType(ValidPresenter.View.class, ValidPresenter.View::new)
                    .putType(AnotherValidPresenter.View.class, AnotherValidPresenter.View::new)
                    .putType(PresenterWithoutRequiredSupertype.View.class, PresenterWithoutRequiredSupertype.View::new)
                    .build();
        }

        @Override
        public void setPresentersOption(ListOption<Class<? extends Presenter<?>>> presenterChoices) {
            this.presentersListOption = presenterChoices;
        }

    }
}