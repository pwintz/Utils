package paul.wintz.utils;

import java.io.*;
import java.nio.file.*;

import paul.wintz.utils.logging.Lg;

public final class FileUtils {

    private static final String TAG = Lg.makeTAG(FileUtils.class);

    public static String[] getFileNamesInPath(String folderPath) {
        return new File(folderPath).list();
    }

    public static void addLineToFile(String fileName, CharSequence newLine) {

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND)){
            writer.append(newLine);
        } catch (IOException e) {
            Lg.e(TAG, "Failed to append line", e);
        }
    }

    private FileUtils() {
        // Do not instantiate.
    }

}
