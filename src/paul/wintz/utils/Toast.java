package paul.wintz.utils;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nonnull;

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
