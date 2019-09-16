package paul.wintz.functionevaluator;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static paul.wintz.utils.RegexUtils.WORD_BOUNDARY;

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

    /**
     * @return the value of the function, if possible, otherwise NaN.
     */
    public double evaluate() {
        try {
            return expression.evaluate();
        } catch (ArithmeticException e){
            return Double.NaN; // If there happens to be an error, we'll just return NaN.
        }
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

        public Builder addVariable(Map.Entry<String, DoubleSupplier> variableEntry){
            variables.put(variableEntry.getKey(), variableEntry.getValue());
            return this;
        }

        public boolean hasVariable(String name){
            return variables.containsKey(name);
        }

        public ImmutableSet<String> variableNames(){
            return ImmutableSet.copyOf(variables.keySet());
        }

        public void removeVariable(String name){
            variables.remove(name);
        }

        public FunctionEvaluator build() throws InvalidEquationException {
            try {
                expressionString = insertMissingParenthesesAfterVariables(expressionString);
                ExpressionBuilder builder = new ExpressionBuilder(expressionString)
                        .variables(parameters.keySet());

                // The variables are passed as functions that return its current value.
                for(String variableName : this.variables.keySet()){
                    final DoubleSupplier variableSupplier = checkNotNull(variables.get(variableName), "The variable named '%s' was null", variableName);
                    builder.function(new Function(variableName, 0) {
                        @Override
                        public double apply(double... doubles) {
                            return variableSupplier.getAsDouble();
                        }
                    });
                }

                builder.function(ExtraFunctions.NORMALIZED_SINE)
                        .function(ExtraFunctions.NORMALIZED_COSINE)
                        .function(ExtraFunctions.ROUND)
                        .function(ExtraFunctions.STEPS)
                        .function(ExtraFunctions.SQUARE_WAVE);

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

        private String insertMissingParenthesesAfterVariables(String expressionString) {
            final String NO_PARENTHESES_PAIR = "(?!\\(\\))";
            final String REGEX_TEMPLATE = WORD_BOUNDARY + "%s" + WORD_BOUNDARY + NO_PARENTHESES_PAIR;
            for (String varName : variables.keySet()) {
                String regex = String.format(REGEX_TEMPLATE, varName);
                expressionString = expressionString.replaceAll(regex, varName + "()");
            }
            return expressionString;
        }
    }

}
