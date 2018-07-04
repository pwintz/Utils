package paul.wintz.typefactory;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

public class FixedSuperTypeFactoryTest {

    interface RootInterface {}

    interface BaseInterfaceA extends RootInterface{}
    class SubClassA implements BaseInterfaceA {}

    interface BaseInterfaceB extends RootInterface {}
    class SubClassB implements BaseInterfaceB {}

    class ClassC implements RootInterface {}

    interface UnrecognizedInterface extends RootInterface {}

    FixedSuperTypeFactory<RootInterface> typeFactory
            = FixedSuperTypeFactory.<RootInterface>builder()
            .putType(BaseInterfaceA.class, SubClassA::new)
            .putType(BaseInterfaceB.class, SubClassB::new)
            .putType(ClassC.class, ClassC::new)
            .build();

    @Test (expected = ClassNotSupported.class)
    public void makeThrowsClassNotFoundIfClassIsUnrecognized() throws Exception {
        typeFactory.make(UnrecognizedInterface.class);
    }

    @Test
    public void makeReturnsAnInstanceOfSubClassIfBaseInterfaceIsRecognized() throws Exception {
        assertThat(typeFactory.make(BaseInterfaceA.class), is(Matchers.instanceOf(SubClassA.class)));
        assertThat(typeFactory.make(BaseInterfaceB.class), is(Matchers.instanceOf(SubClassB.class)));
        assertThat(typeFactory.make(ClassC.class), is(Matchers.instanceOf(ClassC.class)));
    }

    @Test
    public void makeReturnsUniqueInstancesIfCalledTwice() throws Exception {
        assertNotSame(typeFactory.make(BaseInterfaceA.class), typeFactory.make(BaseInterfaceA.class));
    }

    @Test
    public void getBaseInterfacesReturnsAvailableTypes() {
        //noinspection unchecked
        assertThat(typeFactory.getBaseTypes(), Matchers.contains(BaseInterfaceA.class, BaseInterfaceB.class, ClassC.class));
    }

    @Test
    public void builderOnlyAllowsInstantiatorsToCreateSubClassesOfKey() {
        // This will compile:
        FixedSuperTypeFactory.builder().putType(Object.class, SubClassA::new);

        // this will not:
        // TypeFactory.builder().putType(SubClassA.class, Object::new);
    }

}