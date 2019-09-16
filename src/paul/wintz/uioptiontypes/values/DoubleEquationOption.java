package paul.wintz.uioptiontypes.values;

import paul.wintz.functionevaluator.FunctionEvaluator;
import paul.wintz.functionevaluator.InvalidEquationException;
import paul.wintz.uioptiontypes.ValuesSuppliers;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.Set;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

@Deprecated // Use EquationOption instead
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
            Toast.show("Invalid equation: %s", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean emitViewValueChanged(@Nonnull EquationDoubleSupplierPair newValue) {
        throw new UnsupportedOperationException("Use updateEquation");
    }

    public static class Builder extends ValueOption.Builder<EquationDoubleSupplierPair, DoubleEquationOption.Builder> {
        private String initialEquation = "";
        private final FunctionEvaluator.Builder functionEvaluatorBuilder = FunctionEvaluator.builder().setEquation("1.0");
        private ValuesSuppliers valuesSupplier;

        public DoubleEquationOption build() {
            try {
                for (Map.Entry<String, DoubleSupplier> entry : valuesSupplier) {
                    functionEvaluatorBuilder.addVariable(entry.getKey(), entry.getValue());
                }
                FunctionEvaluator functionEvaluator = functionEvaluatorBuilder.setEquation(initialEquation).build();
                initial = new EquationDoubleSupplierPair(initialEquation, functionEvaluator::evaluate);
            } catch (InvalidEquationException e) {
                Lg.e(TAG, "Initial equation value is invalid", e);
                throw new RuntimeException(e);
            }

            valuesSupplier.addSuppliersChangeListener((Set<String> updatedSupplierNames) -> {
                for (String variableName : functionEvaluatorBuilder.variableNames()) {
                    if (!updatedSupplierNames.contains(variableName)) {
                        functionEvaluatorBuilder.removeVariable(variableName);
                    }
                }
                for (String supplierName : updatedSupplierNames) {
                    if (!functionEvaluatorBuilder.hasVariable(supplierName)) {
                        functionEvaluatorBuilder.addVariable(supplierName, valuesSupplier.get(supplierName));
                    }
                }
                Lg.v(TAG, "functionEvaluatorBuilder.variableNames(): %s", functionEvaluatorBuilder.variableNames());
            });

            return new DoubleEquationOption(this);
        }

        protected Builder() {}

        public Builder initial(String initialEquation) {
            this.initialEquation = checkNotNull(initialEquation);
            return this;
        }

        public Builder setValuesSupplier(ValuesSuppliers valuesSupplier) {
            this.valuesSupplier = checkNotNull(valuesSupplier);
            return this;
        }

        @Override
        public Builder callbackOnInitialization(boolean callbackOnInitialization) {
            throw new UnsupportedOperationException("Callback on initialization is done by default");
        }
    }

}
