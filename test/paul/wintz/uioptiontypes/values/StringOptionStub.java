package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.checkState;

// This is just a stub to get around avoid "Generics Hell" that results from using ValueOption
// directly.
public class StringOptionStub extends ValueOption<String> {

    private StringOptionStub(StringOptionStub.Builder builder){
        super(builder);
    }

    public static StringOptionStub.Builder builder() {
        return new StringOptionStub.Builder();
    }

    public static final class Builder extends ValueOption.Builder<String, StringOptionStub.Builder> {

        private Builder() {
            // Prevent external instantiation and load defaults
        }

        public final ValueOption<String> build() {
            return new StringOptionStub(this);
        }

    }

}
