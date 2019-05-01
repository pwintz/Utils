package paul.wintz.functionevaluator;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static paul.wintz.functionevaluator.InvalidEquationException.Reason.*;

public class InvalidEquationExceptionTest {

    @Test
    public void throwsIfNoExpressionGiven() throws Exception {
        try{
            FunctionEvaluator.builder().build();
            fail();
        } catch (InvalidEquationException e){
            assertThat(e.getReason(), is(equalTo(EMPTY_EXPRESSION)));
        }
    }

    @Test
    public void throwsIfEmptyEquation() throws Exception {
        checkExpressionThrowsWithReason("", EMPTY_EXPRESSION);
        checkExpressionThrowsWithReason(" ", EMPTY_EXPRESSION);
    }

    @Test
    public void throwsIfUnknownSymbol() throws Exception {
        checkExpressionThrowsWithReason("mystery", UNKNOWN_SYMBOL);
        checkExpressionThrowsWithReason("?", UNKNOWN_SYMBOL);
        checkExpressionThrowsWithReason("!", UNKNOWN_SYMBOL);
        checkExpressionThrowsWithReason("&", UNKNOWN_SYMBOL);
    }

    @Test
    public void throwsIfBadNumberFormat() throws Exception {
        checkExpressionThrowsWithReason("1.2.3", BAD_NUMBER_FORMAT);
    }

    @Test
    public void throwsIfInvalidNumberOfOperands() throws Exception {
        checkExpressionThrowsWithReason("+", INVALID_NUMBER_OF_OPERANDS);
        checkExpressionThrowsWithReason("1-", INVALID_NUMBER_OF_OPERANDS);
        checkExpressionThrowsWithReason("*4", INVALID_NUMBER_OF_OPERANDS);
        checkExpressionThrowsWithReason("/7.3", INVALID_NUMBER_OF_OPERANDS);
        checkExpressionThrowsWithReason("7.3^", INVALID_NUMBER_OF_OPERANDS);

        // These are OK
        FunctionEvaluator.builder().setEquation("-1").build();
        FunctionEvaluator.builder().setEquation("+2").build();
    }

    @Test
    public void throwsIfExtraParenthesis() throws Exception {
        checkExpressionThrowsWithReason("(1+3", EXTRA_OPENING_PARENTHESIS);
        checkExpressionThrowsWithReason("1+3)", EXTRA_CLOSING_PARENTHESIS);
    }

    @Test
    public void throwsIfInvalidNumberOfFunctionArguments() throws Exception {
        checkExpressionThrowsWithReason("cos(1, 2)", INVALID_NUMBER_OF_FUNCTION_ARGUMENTS);
    }

    @Test
    public void throwsIfMisplacedComma() throws Exception {
        checkExpressionThrowsWithReason("1,23", MISPLACED_COMMA);
    }

    @Test
    public void smokeTestGetMessage() throws Exception {
        try{
            FunctionEvaluator.builder().setEquation("+").build();
            fail("Expected to catch an exception");
        } catch (InvalidEquationException e){
            e.getMessage();
        }
    }

    private void checkExpressionThrowsWithReason(String equation, InvalidEquationException.Reason reason){
        checkExpressionThrowsWithReason(FunctionEvaluator.builder(), equation, reason);
    }

    private void checkExpressionThrowsWithReason(FunctionEvaluator.Builder builder, String equation, InvalidEquationException.Reason reason){
        try{
            builder.setEquation(equation).build();
            fail("build() was expected to throw an error but did not.");
        } catch (InvalidEquationException e){
            assertThat(e.getReason(), is(equalTo(reason)));
        }
    }

}