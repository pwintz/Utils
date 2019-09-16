package paul.wintz.uioptiontypes.values;

import com.google.common.base.MoreObjects;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

public class EquationOption extends StringOption {

    private EquationOption(Builder builder) {
        super(builder);
        builder.doubleEquationBuilder
                .setEquationChangeListener(builder.equationChangeListener)
                .setEquationStringOption(this)
                .build();
    }

    public static class Builder extends StringOption.Builder<Builder> {
        private final DoubleEquationOptionWrapper.Builder doubleEquationBuilder;
        private Consumer<DoubleSupplier> equationChangeListener;

        protected Builder(DoubleEquationOptionWrapper.Builder doubleEquationBuilder) {
            this.doubleEquationBuilder = doubleEquationBuilder;
            initial = "1";
            addValidityEvaluator(doubleEquationBuilder::checkIfEquationIsValid);
        }

        public Builder setEquationChangeListener(Consumer<DoubleSupplier> equationChangeListener) {
            this.equationChangeListener = equationChangeListener;
            return this;
        }

        public EquationOption build() {
            return new EquationOption(this);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("initial", initial)
                    .toString();
        }
    }

    public static StringOption.Builder builder() {
        // This method is effectively overridden to prevent users from accidentally using the method from the StringOption class.
        throw new UnsupportedOperationException("Use builder(DoubleEquationOptionWrapper.Builder doubleEquationBuilder) instead");
    }

    public static Builder builder(DoubleEquationOptionWrapper.Builder doubleEquationBuilder) {
        return new Builder(doubleEquationBuilder);
    }
}
