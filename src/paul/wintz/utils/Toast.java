package paul.wintz.utils;

import paul.wintz.stringids.StringId;
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
        /**
         * @param message a string message to display to screen
         */
        void show(String message);

        /**
         * @param messageId an ID that corresponds to a message string.
         */
        void show(StringId messageId);

        /**
         * @param messageFormat a format string.
         * @param args a mixture of StringIds and other objects (including boxed primitives)
         */
        void show(String messageFormat, Object... args);

        /**
         * @param messageFormatId an ID that corresponds to a format string.
         * @param args a mixture of StringIds and other objects (including boxed primitives)
         */
        void show(StringId messageFormatId, Object... args);
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

    public static void show(String message, Object... args){
        if(toaster == null){
            Lg.i(TAG, "Toaster not set. Cannot display message: %s", message);
            return;
        }
        toaster.show(message, args);
    }

    public static void show(StringId messageId){
        if(toaster == null){
            Lg.i(TAG, "Toaster not set. Cannot display messageId: %s", messageId);
            return;
        }
        toaster.show(messageId);
    }

    public static void show(StringId messageId, Object... args){
        if(toaster == null){
            Lg.i(TAG, "Toaster not set. Cannot display messageId: %s with args: %s", messageId, args);
            return;
        }
        toaster.show(messageId, args);
    }

    private Toast() {
        // Don't allow instantiation.
    }

}
