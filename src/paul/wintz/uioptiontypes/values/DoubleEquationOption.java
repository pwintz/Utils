package paul.wintz.uioptiontypes.values;

import paul.wintz.functionevaluator.FunctionEvaluator;
import paul.wintz.functionevaluator.InvalidEquationException;
import paul.wintz.uioptiontypes.ValuesSuppliers;
import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class DoubleEquationOption extends ValueOption<EquationDoubleSupplierPair> {
    private static final String TAG = Lg.makeTAG(DoubleEquationOption.class);

    // To create a new functionEvaluator, call setEquation() then build(), which will return a new functionEvaluator
    // with the given function equation and a standard set of variables that are specified in setValuesSupplier().
    private final FunctionEvaluator.Builder functionEvaluatorBuilder;

    @SuppressWarnings("WeakerAccess")
    protected DoubleEquationOption(Builder builder) {
        super(builder);
        this.functionEvaluatorBuilder = builder.functionEvaluatorBuilder;
        emitEquationChanged(builder.initialEquation); //
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean emitEquationChanged(String newEquation){
        try {
            FunctionEvaluator functionEvaluator = functionEvaluatorBuilder.setEquation(newEquation).build();
            return super.emitViewValueChanged(new EquationDoubleSupplierPair(newEquation, functionEvaluator::evaluate));
        } catch (InvalidEquationException e) {
            Lg.w(TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean emitViewValueChanged(@Nonnull EquationDoubleSupplierPair newValue) {
        throw new UnsupportedOperationException("Use emitEquationChanged");
    }

    public static class Builder extends ValueOption.Builder<EquationDoubleSupplierPair, DoubleEquationOption.Builder> {
        private String initialEquation = "";
        private final FunctionEvaluator.Builder functionEvaluatorBuilder = FunctionEvaluator.builder().setEquation("1.0");

        public DoubleEquationOption build() {
            try {
                FunctionEvaluator functionEvaluator = functionEvaluatorBuilder.setEquation(initialEquation).build();
                initial = new EquationDoubleSupplierPair(initialEquation, functionEvaluator::evaluate);
            } catch (InvalidEquationException e) {
                Lg.e(TAG, "Initial equation value is invalid", e);
                throw new RuntimeException(e);
            }
            return new DoubleEquationOption(this);
        }

        protected Builder() {}

        public Builder initial(String initialEquation) {
            this.initialEquation = checkNotNull(initialEquation);
            return this;
        }

        public Builder setValuesSupplier(ValuesSuppliers valuesSupplier) {
            for (Map.Entry<String, DoubleSupplier> entry : valuesSupplier) {
                functionEvaluatorBuilder.addVariable(entry.getKey(), entry.getValue());
            }
            return this;
        }

        @Override
        public Builder callbackOnInitialization(boolean callbackOnInitialization) {
            throw new UnsupportedOperationException("Callback on initialization is done by default");
        }
    }

}
