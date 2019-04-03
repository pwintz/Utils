package paul.wintz.functionevaluator;

import com.google.common.base.MoreObjects;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import paul.wintz.utils.logging.Lg;

import javax.annotation.Nullable;
import java.util.EmptyStackException;

public class InvalidEquationException extends Exception {

    private static final String TAG = Lg.makeTAG(InvalidEquationException.class);
    private final String expression;
    private final Reason reason;
    private final String message;

    public enum Reason {
        UNKNOWN_SYMBOL,
        BAD_NUMBER_FORMAT,
        EMPTY_EXPRESSION,
        INVALID_NUMBER_OF_OPERANDS,
        EXTRA_OPENING_PARENTHESIS,
        EXTRA_CLOSING_PARENTHESIS,
        ARITHMETIC_ERROR,
        INVALID_NUMBER_OF_FUNCTION_ARGUMENTS;
    }

    private InvalidEquationException(String expression, Reason reason, String message) {
        this.expression = "\"" + expression + "\"";
        this.reason = reason;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return MoreObjects.toStringHelper(InvalidEquationException.class)
                .add("expression", expression)
                .add("reasons", reason)
                .add("message", message)
                .toString();
    }

    static InvalidEquationException construct(Exception e, String expressionString) {
        return new InvalidEquationException(expressionString, getReason(e), e.getMessage());
    }

    @Nullable
    private static Reason getReason(Exception e) {
        if(e instanceof UnknownFunctionOrVariableException) {
            return Reason.UNKNOWN_SYMBOL;
        }
        if (e instanceof NumberFormatException) {
            return Reason.BAD_NUMBER_FORMAT;
        }
        if (e instanceof EmptyStackException){
            return Reason.EXTRA_CLOSING_PARENTHESIS;
        }
        if (e instanceof ArithmeticException) {
            return Reason.ARITHMETIC_ERROR;
        }
        if (e instanceof IllegalArgumentException) {
            String s = e.getMessage();
            if ("Expression can not be empty".equals(s)) {
                return Reason.EMPTY_EXPRESSION;
            }
            if ("Operator is unknown for token.".equals(s)) {
                return Reason.UNKNOWN_SYMBOL;
            }
            if (s.equals("Mismatched parentheses detected. Please check the expression")) {
                return Reason.EXTRA_OPENING_PARENTHESIS;
            }
            if (s.contains("Invalid number of operands available for")) {
                return Reason.INVALID_NUMBER_OF_OPERANDS;
            }
            if ("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.".equals(s)) {
                return Reason.INVALID_NUMBER_OF_FUNCTION_ARGUMENTS;
            }
        }

        Lg.w(TAG, "Unrecognized exception", e);
        return null;
    }
}
