package paul.wintz.typefactory;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;

public class ClassNotSupportedException extends RuntimeException {
    private <T> ClassNotSupportedException(Class<? extends T> unsupportedClass, Collection<Class<? extends T>> supportedClasses){
        super("The type '" + unsupportedClass + "' is not in the collection of supported classes: " + supportedClasses);
    }

    static <T> void checkClassIsSupported(Class<? extends T> classToCheck, Collection<Class<? extends T>> supportedClasses) {
        if(!supportedClasses.contains(classToCheck)) {
            throw new ClassNotSupportedException(classToCheck, supportedClasses);
        }
    }
}
