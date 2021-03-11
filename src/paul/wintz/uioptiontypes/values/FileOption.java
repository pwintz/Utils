package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.*;

public class FileOption extends ValueOption<File> {

    public enum Purpose {
        OPEN, SAVE, DIRECTORY
    }

    @Nonnull public final FileOption.Purpose purpose;
    @Nonnull public final String[] extensions;
    @Nonnull public final String extensionsDescription;
    @Nonnull public final Path initialDirectory;

    private FileOption(Builder builder) {
        super(builder);
        this.purpose = builder.purpose;

        extensionsDescription = builder.extensionsDescription;
        ImmutableSet<String> extensions = builder.extensionsBuilder.build();
        this.extensions = new String[extensions.size()];
        extensions.toArray(this.extensions);

        this.initialDirectory = builder.initialDirectory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<File, Builder> {

        private FileOption.Purpose purpose = FileOption.Purpose.OPEN;
        private ImmutableSet.Builder<String> extensionsBuilder = ImmutableSet.builder();
        private String extensionsDescription;
        private Path initialDirectory;

        private Builder() {
            initial(new File(""));
        }

        /**
         * Add an option for a description and list of extensions to make visible when browsing the file system.
         * @param description A description of the types of files with the given extensions.
         * @param extensions One or more file extensions (e.g. ".png", ".exe").
         */
        public Builder extensionFilter(String description, String... extensions) {
            checkArgument(extensions.length > 0, "There must be at least one extension specified.");
            extensionsDescription = description;
            extensionsBuilder.add(extensions);
            return this;
        }

        public Builder isOpen() {
            return purpose(Purpose.OPEN);
        }

        public Builder isSave() {
            return purpose(Purpose.SAVE);
        }

        public Builder purpose(FileOption.Purpose purpose) {
            this.purpose = checkNotNull(purpose);
            return this;
        }

        public Builder initialDirectory(Path directory) {
            this.initialDirectory = directory;
            return this;
        }

        public FileOption build() {
            ImmutableSet<String> extensions = extensionsBuilder.build();
            checkState(extensions.size() > 0, "at least one extension must be specified");
            return new FileOption(this);
        }
    }

}
