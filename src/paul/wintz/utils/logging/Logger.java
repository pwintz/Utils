package paul.wintz.utils.logging;

public interface Logger {
	void logVerbose(String TAG, String message);
	void logVerbose(String TAG, String message, Throwable e);
	
	void logDebug(String TAG, String message);
	void logDebug(String TAG, String message, Throwable e);
	
	void logInfo(String TAG, String message);
	void logInfo(String TAG, String message, Throwable e);
	
	void logWarning(String TAG, String message);
	void logWarning(String TAG, String message, Throwable e);
	
	void logError(String TAG, String message);
	void logError(String TAG, String message, Throwable e);
}