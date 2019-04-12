package paul.wintz.stringids;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class StringIdMap {

    private final ImmutableMap<StringId, String> stringIdMap;

    private StringIdMap(Builder builder) {
        this.stringIdMap = builder.stringIdMapBuilder.build();

        for(StringId id : builder.requiredIDs){
            checkState(stringIdMap.containsKey(id), "stringIdMap is missing key: %s", id);
        }
    }

    public String get(StringId id) {
        return stringIdMap.get(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StringId[] requiredIDs;
        private final ImmutableMap.Builder<StringId, String> stringIdMapBuilder = ImmutableMap.builder();

        public Builder setRequiredIds(@Nonnull StringId[] requiredIDs) {
            checkState(this.requiredIDs == null, "requiredIDs already set");
            this.requiredIDs = checkNotNull(requiredIDs);
            return this;
        }

        public Builder putStringId(StringId stringId, String string) {
            stringIdMapBuilder.put(checkNotNull(stringId), checkNotNull(string));
            return this;
        }

        public StringIdMap build() {
            return new StringIdMap(this);
        }
    }

}
