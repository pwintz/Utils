package paul.wintz.utils;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A class (essentially a singleton) to display messages on screen.
 */
public final class Toast {

    private static Toaster toaster;

    public interface Toaster {
        void show(String message);
    }

    public static void setToaster(@Nonnull Toaster toaster){
        checkState(Toast.toaster == null, "toaster was already set!");
        Toast.toaster = checkNotNull(toaster);
    }

    public static void show(String message){
        toaster.show(message);
    }

    private Toast() {
        // Don't allow instantiation.
    }

}
