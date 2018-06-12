package paul.wintz.spirotechnics.tests;

import org.junit.Test;
import paul.wintz.utils.ClassUtils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ClassUtilsTest {

    @Test
    public void testClassHierarchyToString() {
        String actual = ClassUtils.classHierarchyToString(Float.class);
        assertThat(actual, is(equalTo("Object<Number<Float")));
    }

    private static class NestedClass {
        private static class DoublyNestedClass {}
    }

    @Test
    public void testClassHierarchyToStringWithNestedClass() {
        String actual = ClassUtils.classHierarchyToString(NestedClass.class);
        assertThat(actual, is(equalTo("Object<ClassUtilsTest#NestedClass")));
    }

    @Test
    public void testClassHierarchyToStringWithDoublyNestedClass() {
        String actual = ClassUtils.classHierarchyToString(NestedClass.DoublyNestedClass.class);
        assertThat(actual, is(equalTo("Object<ClassUtilsTest#NestedClass#DoublyNestedClass")));
    }

    @Test
    public void testClassHierarchyToStringWithAnonymousClass() {
        Object anon = new Object() {};
        String actual = ClassUtils.classHierarchyToString(anon.getClass());
        assertThat(actual, is(equalTo("Object<ClassUtilsTest#<?>")));
    }

    @Test
    public void testClassHierarchyToStringWithDoubleAnonymousClass() throws Exception {
        Object outerAnon = new Object() {
            public Object innerAnon = new Object() {};
        };
        Class<?> innerAnonClass = outerAnon.getClass().getField("innerAnon").get(outerAnon).getClass();
        String actual = ClassUtils.classHierarchyToString(innerAnonClass);
        assertThat(actual, is(equalTo("Object<ClassUtilsTest#<?>#<?>")));
    }

}
