package paul.wintz.typefactory;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TypeFactoryTest {

    interface BaseInterfaceA {}
    class SubClassA implements BaseInterfaceA {}

    interface BaseInterfaceB  {}
    class SubClassB implements BaseInterfaceB {}

    class ClassC {}

    interface UnrecognizedInterface  {}

    TypeFactory typeFactory = TypeFactory.builder()
            .putType(BaseInterfaceA.class, SubClassA::new)
            .putType(BaseInterfaceB.class, SubClassB::new)
            .putType(ClassC.class, ClassC::new)
            .build();

    @Test (expected = ClassNotSupportedException.class)
    public void makeThrowsClassNotFoundIfClassIsUnrecognized() throws Exception {
        typeFactory.make(UnrecognizedInterface.class);
    }

    @Test
    public void makeReturnsAnInstanceOfSubClassIfBaseInterfaceIsRecognized() throws Exception {
        assertThat(typeFactory.make(BaseInterfaceA.class), is(instanceOf(SubClassA.class)));
        assertThat(typeFactory.make(BaseInterfaceB.class), is(instanceOf(SubClassB.class)));
        assertThat(typeFactory.make(ClassC.class), is(instanceOf(ClassC.class)));
    }

    @Test
    public void makeReturnsUniqueInstancesIfCalledTwice() throws Exception {
        Assert.assertNotSame(typeFactory.make(BaseInterfaceA.class), typeFactory.make(BaseInterfaceA.class));
    }

    @Test
    public void getBaseInterfacesReturnsAvailableTypes() {
        //noinspection unchecked
        assertThat(typeFactory.getBaseTypes(), Matchers.contains(BaseInterfaceA.class, BaseInterfaceB.class, ClassC.class));
    }

    @Test
    public void canMakeReturnsFalseForUnrecognizedClass() {
        assertFalse(typeFactory.canMake(UnrecognizedInterface.class));
    }

    @Test
    public void canMakeReturnsFalseForSubclass() {
        assertFalse(typeFactory.canMake(SubClassA.class));
    }

    @Test
    public void canMakeReturnsTrueFoRecognizedClass() {
        assertTrue(typeFactory.canMake(BaseInterfaceA.class));
    }

    @Test
    public void getInstantiatorReturnsInstantiator() throws Exception {
        Instantiator<? extends BaseInterfaceA> instantiator = typeFactory.getInstantiator(BaseInterfaceA.class);
        assertThat(instantiator.instance(), is(instanceOf(BaseInterfaceA.class)));
    }

    @Test
    public void builderOnlyAllowsInstantiatorsToCreateSubClassesOfKey() {
        // This will compile:
        TypeFactory.builder().putType(Object.class, SubClassA::new);

        // this will not:
        // TypeFactory.builder().putType(SubClassA.class, Object::new);
    }

}