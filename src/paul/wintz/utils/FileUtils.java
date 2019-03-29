package paul.wintz.utils;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FileUtils {

    private static final String TAG = Lg.makeTAG(FileUtils.class);

    @Nonnull public static String[] getFileNamesInPath(String folderPath) {
        String[] list = new File(folderPath ).list();
        if (list == null){
            Lg.w(TAG, "File list was empty. Bad path? " + folderPath);
            return new String[0];
        }
        return list;
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
