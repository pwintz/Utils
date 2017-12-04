package paul.wintz.utils.logging;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nonnull;

public final class Lg {

	private static Logger logger;

	public static void setLogger(@Nonnull Logger logger){
		checkState(Lg.logger == null, "logger was already set!");
		Lg.logger = checkNotNull(logger);
	}

	public static void v(String tag, String message){
		logger.logVerbose(tag, message);
	}

	public static void v(String tag, String message, Throwable t){
		logger.logVerbose(tag, message, t);
	}

	public static void d(String tag, String message){
		logger.logDebug(tag, message);
	}

	public static void d(String tag, String message, Throwable t){
		logger.logDebug(tag, message, t);
	}

	public static void i(String tag, String message){
		logger.logInfo(tag, message);
	}

	public static void i(String tag, String message, Throwable t){
		logger.logInfo(tag, message, t);
	}

	public static void w(String tag, String message){
		logger.logWarning(tag, message);
	}

	public static void w(String tag, String message, Throwable t){
		logger.logWarning(tag, message, t);
	}

	public static void e(String tag, String message){
		logger.logError(tag, message);
	}

	public static void e(String tag, String message, Throwable t){
		logger.logError(tag, message, t);
	}

	public static String makeTAG(Class<?> classs){
		return classs.getSimpleName();
	}

	public static void setupDefaultLogger(){
		if(logger == null) {
			setLogger(new JavaStdOutLogger());
		}
	}

	private Lg() {
		// Don't allow instantiation.
	}

}
