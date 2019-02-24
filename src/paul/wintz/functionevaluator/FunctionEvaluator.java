package paul.wintz.functionevaluator;

import com.google.common.base.MoreObjects;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * Takes a string expression and a set of variables, or variable getters, and returns a value
 */
public class FunctionEvaluator {

    private final @Nonnull Expression expression;
    private @Nonnull String toStringRepresentation = "";

    private FunctionEvaluator(@Nonnull Expression expression){
        this.expression = expression;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double evaluate() {
        return expression.evaluate();
    }

    @Override
    public String toString() {
        return toStringRepresentation;
    }

    public static class Builder {

        private String expressionString;

        // Parameters are values that change occasionally, usually when the user modifies them.
        private Map<String, Double> parameters = new HashMap<>();

        // Variables are values that change frequently, usually as the result of calculations.
        private Map<String, DoubleSupplier> variables = new HashMap<>();

        private Builder() {
            // Empty
        }

        public Builder setEquation(String equation) {
            this.expressionString = equation;
            return this;
        }

        public Builder addParameter(String name, double value){
            parameters.put(name, value);
            return this;
        }

        public Builder addVariable(String name, DoubleSupplier valueSupplier){
            variables.put(name, valueSupplier);
            return this;
        }

        public FunctionEvaluator build() throws InvalidEquationException {
            try {
                ExpressionBuilder builder = new ExpressionBuilder(expressionString)
                        .variables(parameters.keySet());

                // The variables are passed as functions that return its current value.
                for(String variableName : this.variables.keySet()){
                    final DoubleSupplier variableSupplier = variables.get(variableName);
                    builder.function(new Function(variableName, 0) {
                        @Override
                        public double apply(double... doubles) {
                            return variableSupplier.getAsDouble();
                        }
                    });
                }
                Expression expression = builder.build();
                expression.setVariables(parameters);

                FunctionEvaluator functionEvaluator = new FunctionEvaluator(expression);
                functionEvaluator.toStringRepresentation = MoreObjects.toStringHelper(FunctionEvaluator.class)
                        .add("expression", "\"" + expressionString + "\"")
                        .add("parameters", parameters.keySet())
                        .add("variables", variables.keySet())
                        .toString();
                functionEvaluator.evaluate(); // Make sure it works!
                return functionEvaluator;
            } catch (Exception e) {
                throw InvalidEquationException.construct(e, expressionString);
            }
        }
    }

}
