package paul.wintz.functionevaluator;

import org.junit.Test;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.assertEquals;

public class FunctionEvaluatorTest {

    @Test(expected = InvalidEquationException.class)
    public void throwsIfNoExpressionGiven() throws Exception {
        FunctionEvaluator.builder().build();
    }

    @Test
    public void hardcodedConstantReturnsValue()  throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .setEquation("1")
                .build();
        assertEquals(1, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void parameterReturnsValue() throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addParameter("R", 12)
                .setEquation("R")
                .build();
        assertEquals(12, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void variableReturnsValue() throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addVariable("x", () -> 4.5)
                .setEquation("x")
                .build();
        assertEquals(4.5, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void variableReturnsUpdatedValue() throws Exception {
        IncrementerDoubleSupplier variableValueSupplier = new IncrementerDoubleSupplier(7.2);
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addVariable("x", variableValueSupplier)
                .setEquation("x")
                .build();
        assertEquals(7.2, functionEvaluator.evaluate(), 0.0);
        variableValueSupplier.increment();
        assertEquals(8.2, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void complexEquationsAreEvaluated() throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addVariable("x", () -> 1)
                .addVariable("y", () -> 2)
                .addParameter("R", 3.0)
                .setEquation("(x() + y())*R")
                .build();
        assertEquals((1+2)*3, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void parenthesesNotNeededAfterVariableName() throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addVariable("x", () -> 1)
                .setEquation("x + x")
                .build();
        assertEquals(1+1, functionEvaluator.evaluate(), 0.0);
    }

    @Test
    public void toStringTest() throws Exception {
        FunctionEvaluator functionEvaluator = FunctionEvaluator.builder()
                .addParameter("R", 12)
                .addVariable("xyz", () -> 4.3)
                .addVariable("x", () -> 4.3)
                .setEquation("R")
                .build();
        System.out.println(functionEvaluator.toString());
    }

    private static class IncrementerDoubleSupplier implements DoubleSupplier {
        double value;

        private IncrementerDoubleSupplier(double value) {
            this.value = value;
        }

        public void increment() {
            value++;
        }

        @Override
        public double getAsDouble() {
            return value;
        }
    }
}