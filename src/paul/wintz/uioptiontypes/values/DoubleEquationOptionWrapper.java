package paul.wintz.uioptiontypes.values;

import paul.wintz.functionevaluator.FunctionEvaluator;
import paul.wintz.functionevaluator.InvalidEquationException;
import paul.wintz.uioptiontypes.ValuesSuppliers;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.Lg;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class DoubleEquationOptionWrapper {
    private static final String TAG = Lg.makeTAG(DoubleEquationOptionWrapper.class);

    // To create a new functionEvaluator, call setEquation() then build(), which will return a new functionEvaluator
    // with the given function equation and a standard set of variables that are specified in setValuesSupplier().
    private final FunctionEvaluator.Builder functionEvaluatorBuilder;

    private final Consumer<DoubleSupplier> equationChangeListener;

    @SuppressWarnings("WeakerAccess")
    protected DoubleEquationOptionWrapper(Builder builder) {
        this.functionEvaluatorBuilder = checkNotNull(builder.functionEvaluatorBuilder);
        equationChangeListener = checkNotNull(builder.equationChangeListener);

        builder.equationStringOption.addViewValueChangeCallback(this::updateEquation);

        if(!updateEquation(builder.equationStringOption.getValue())){
            throw new RuntimeException("Failed to set equation to initial value");
        }
    }

    public static Builder builder(ValuesSuppliers valuesSupplier) {
        return new Builder(valuesSupplier);
    }

    private boolean updateEquation(String newEquation){
        try {
            FunctionEvaluator functionEvaluator = functionEvaluatorBuilder.setEquation(newEquation).build();
            DoubleSupplier evaluator = functionEvaluator::evaluate;
            equationChangeListener.accept(evaluator);
            return true;
        } catch (InvalidEquationException e) {
            Lg.w(TAG, "Failed to update equation: %s", e.getMessage());
            Toast.show("Invalid equation: %s", e.getMessage());
            return false;
        }
    }

    public static class Builder {
        private final FunctionEvaluator.Builder functionEvaluatorBuilder = FunctionEvaluator.builder().setEquation("1.0");

        private StringOption equationStringOption;
        private final ValuesSuppliers valuesSupplier;

        private Consumer<DoubleSupplier> equationChangeListener;

        public DoubleEquationOptionWrapper build() {
            for (Map.Entry<String, DoubleSupplier> entry : valuesSupplier) {
                functionEvaluatorBuilder.addVariable(entry);
            }

            if(!checkIfEquationIsValid(equationStringOption.getValue())){
                throw new RuntimeException("Initial equation value is invalid: " + equationStringOption.getValue());
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

            return new DoubleEquationOptionWrapper(this);
        }

        public boolean checkIfEquationIsValid(String equation) {
            for (Map.Entry<String, DoubleSupplier> entry : valuesSupplier) {
                functionEvaluatorBuilder.addVariable(entry);
            }
            try {
                functionEvaluatorBuilder.setEquation(equation).build();
                return true;
            } catch (InvalidEquationException e) {
                Lg.w(TAG, "Equation is not valid", e);
                return false;
            }
        }

        protected Builder(ValuesSuppliers valuesSupplier) {
            this.valuesSupplier = valuesSupplier;
        }

        public Builder setEquationStringOption(StringOption equationStringOption) {
            this.equationStringOption = equationStringOption;
            return this;
        }

        public Builder setEquationChangeListener(Consumer<DoubleSupplier> equationChangeListener) {
            this.equationChangeListener = checkNotNull(equationChangeListener);
            return this;
        }

    }

}
