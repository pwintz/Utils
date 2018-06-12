package paul.wintz.utils;

import java.util.Stack;

public class ClassUtils {

    public static String classHierarchyToString(Class<?> clazz) {
        Stack<Class<?>> superClasses = new Stack<>();
        superClasses.add(clazz);
        StringBuilder sb = new StringBuilder();

        Class<?> superClass = clazz.getSuperclass();
        while(superClass != null) {
            superClasses.push(superClass);
            superClass = superClass.getSuperclass();
        }
        while(!superClasses.empty()){
            sb.append(prependEnclosingClasses(superClasses.pop()));
            if(!superClasses.empty()) sb.append("<");
        }

        return sb.toString();
    }

    private static String prependEnclosingClasses(Class<?> clazz) {
        if(clazz.getEnclosingClass() == null) return clazz.getSimpleName();
        return prependEnclosingClasses(clazz.getEnclosingClass()) + "#" + className(clazz);
    }

    private static String className(Class<?> clazz) {
        if(clazz.isAnonymousClass()) return "<?>";
        return clazz.getSimpleName();
    }

}
