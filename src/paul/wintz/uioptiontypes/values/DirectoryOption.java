package paul.wintz.uioptiontypes.values;

import java.io.File;

public class DirectoryOption extends ValueOption<File> {

    private DirectoryOption(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ValueOption.Builder<File, Builder> {
        private Builder() {
            initial(new File(""));
        }

        public DirectoryOption build() {
            return new DirectoryOption(this);
        }
    }

}
