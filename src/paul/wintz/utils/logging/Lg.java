package paul.wintz.utils.logging;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Lg {

    private static Logger logger;

    static {
        setupDefaultLogger();
    }

    public static void setLogger(@Nonnull Logger logger){
        Lg.logger = checkNotNull(logger);
    }

    public static void v(String tag, String message){
        logger.logVerbose(tag, message);
    }

    public static void v(String tag, String format, Object... args){
        logger.logVerbose(tag, String.format(format, args));
    }

    public static void v(String tag, String message, Throwable t){
        logger.logVerbose(tag, message, t);
    }

    public static void d(String tag, String message){
        logger.logDebug(tag, message);
    }

    public static void d(String tag, String format, Object... args){
        logger.logDebug(tag, String.format(format, args));
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

    public static void i(String tag, String format, Object... args){
        logger.logInfo(tag, String.format(format, args));
    }

    public static void w(String tag, String message){
        logger.logWarning(tag, message);
    }

    public static void w(String tag, String format, Object... args){
        logger.logWarning(tag, String.format(format, args));
    }

    public static void w(String tag, String message, Throwable t){
        logger.logWarning(tag, message, t);
    }

    public static void e(String tag, String message){
        logger.logError(tag, message);
    }

    public static void e(String tag, String format, Object... args){
        logger.logError(tag, String.format(format, args));
    }

    public static void e(String tag, Throwable error){
        logger.logError(tag, "", error);
    }

    public static void e(String tag, String message, Throwable t){
        logger.logError(tag, message, t);
    }

    public static String makeTAG(Class<?> clazz){
        return clazz.getSimpleName();
    }

    private static synchronized void setupDefaultLogger(){
        if(logger == null) {
            setLogger(new JavaStdOutLogger());
        }
    }

    private Lg() {
        // Don't allow instantiation.
    }

    private static String tempTAG() {
        return "(TEMP)" + new Exception().getStackTrace()[2].getClassName();
    };

    public static void temp(String message){
        logger.logDebug(tempTAG(), message);
    }

    public static void temp(String format, Object... args){
        logger.logDebug(tempTAG(), String.format(format, args));
    }

    public static void temp(Throwable t){
        logger.logDebug(tempTAG(), "", t);
    }

    public static void temp(String message, Throwable t){
        logger.logDebug(tempTAG(), message, t);
    }

}

