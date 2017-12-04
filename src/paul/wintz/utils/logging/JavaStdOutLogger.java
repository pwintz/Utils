package paul.wintz.utils.logging;

import java.io.PrintStream;

public final class JavaStdOutLogger implements Logger {

	private static final PrintStream stdOut = System.out;
	private static final PrintStream stdErr = System.err;

	private static PrintStream lastOut = stdOut;

	private static synchronized void printStd(String level, String TAG, String message){
		flushIfSwitchingStreams(stdOut);

		final String toPrint = format(level, TAG, message);
		stdOut.println(toPrint);
	}

	private static synchronized void printErr(String level, String TAG, String message){
		flushIfSwitchingStreams(stdErr);

		final String toPrint = format(level, TAG, message);
		stdErr.println(toPrint);
	}

	private static synchronized void flushIfSwitchingStreams(PrintStream stream) {
		if(stream != lastOut) {
			lastOut.flush();
			lastOut = stream;
		}
	}

	private static String format(String level, String TAG, String message){
		return String.format("%s/%s: %s", level, TAG, message);
	}

	@Override
	public void logVerbose(String TAG, String message) {
		printStd("V", TAG, message);
	}

	@Override
	public void logVerbose(String TAG, String message, Throwable e) {
		printStd("V", TAG, message);
		e.printStackTrace(stdOut);
	}

	@Override
	public void logDebug(String TAG, String message) {
		printStd("D", TAG, message);
	}

	@Override
	public void logDebug(String TAG, String message, Throwable e) {
		printStd("D", TAG, message);
		e.printStackTrace(stdOut);
	}

	@Override
	public void logInfo(String TAG, String message) {
		printStd("I", TAG, message);
	}

	@Override
	public void logInfo(String TAG, String message, Throwable e) {
		printStd("I", TAG, message);
		e.printStackTrace(stdOut);
	}

	@Override
	public void logWarning(String TAG, String message) {
		printErr("W", TAG, message);
	}

	@Override
	public void logWarning(String TAG, String message, Throwable e) {
		printErr("W", TAG, message);
		e.printStackTrace();
	}

	@Override
	public void logError(String TAG, String message) {
		printErr("E", TAG, message);
	}

	@Override
	public void logError(String TAG, String message, Throwable e) {
		printErr("E", TAG, message);
		e.printStackTrace();
	}

}