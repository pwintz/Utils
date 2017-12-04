package paul.wintz.utils;

import java.io.*;

import paul.wintz.utils.logging.Lg;

public final class FileUtils {

	private static final String TAG = Lg.makeTAG(FileUtils.class);

	public static String[] getFileNamesInPath(String folderPath) {
		return new File(folderPath).list();
	}

	public static void addLineToFile(String fileName, String newLine) {
		FileWriter out;
		try {
			// ADD EXTENSION TO FILENAME
			if (!fileName.contains(".txt")) {
				fileName += ".txt";
			}
			// ADD LINE BREAK
			if (!newLine.endsWith(System.lineSeparator())) {
				newLine += System.lineSeparator();
			}

			// SAVE ARRAYLIST TO THE FILE
			out = new FileWriter(fileName, true);
			out.append(newLine);
			out.close();
			Lg.v(TAG, newLine + " appended to " + fileName);
		} catch (final IOException e) {
			Lg.e(TAG, "Failed to append line", e);
		}
	}

	private FileUtils() {
		// Do not instantiate.
	}

}
