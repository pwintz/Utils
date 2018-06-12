package paul.wintz.uioptiontypes.values;

import java.io.File;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileOption extends ValueOption<File> {

    private Path directory;

    private FileOption(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<File, Builder> {

        private Path directory;

        private Builder() {
            initial(new File(""));
        }

        public FileOption build() {
            return new FileOption(this);
        }

        public Builder path(Path path) {
            this.directory = checkNotNull(directory);
            return this;
        }

    }

}
