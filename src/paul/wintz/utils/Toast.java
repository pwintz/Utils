package paul.wintz.utils;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A class (essentially a singleton) to display messages on screen.
 */
public final class Toast {
    private static final String TAG = Lg.makeTAG(Toast.class);

    private static Toaster toaster;

    public interface Toaster {
        void show(String message);
    }

    public static void setToaster(@Nonnull Toaster toaster){
        checkState(Toast.toaster == null, "toaster was already set!");
        Toast.toaster = checkNotNull(toaster);
    }

    public static void show(String message){
        if(toaster == null){
            Lg.i(TAG, "Toaster not set. Cannot display message: %s", message);
            return;
        }
        toaster.show(message);
    }

    private Toast() {
        // Don't allow instantiation.
    }

}
