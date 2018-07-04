package paul.wintz.typefactory;

class ClassNotSupported extends RuntimeException {
    ClassNotSupported(Class<?> unsupportedClass){
        super("The type '" + unsupportedClass + "' is not supported by this factory");
    }
}
