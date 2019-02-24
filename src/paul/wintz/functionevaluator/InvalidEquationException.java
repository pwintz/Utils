package paul.wintz.functionevaluator;

import com.google.common.base.MoreObjects;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import paul.wintz.utils.logging.Lg;

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
        ARITHMETIC_ERROR;
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
        Reason reason = null;
        if(e instanceof UnknownFunctionOrVariableException) {
            reason = Reason.UNKNOWN_SYMBOL;
        } else if (e instanceof NumberFormatException) {
            reason = Reason.BAD_NUMBER_FORMAT;
        } else if (e instanceof EmptyStackException){
            reason = Reason.EXTRA_CLOSING_PARENTHESIS;
        } else if (e instanceof ArithmeticException) {
            reason = Reason.ARITHMETIC_ERROR;
        } else if (e instanceof IllegalArgumentException) {
            String s = e.getMessage();
            if ("Expression can not be empty".equals(s)) {
                reason = Reason.EMPTY_EXPRESSION;
            } else if ("Operator is unknown for token.".equals(s)) {
                reason = Reason.UNKNOWN_SYMBOL;
            } else if (s.equals("Mismatched parentheses detected. Please check the expression")) {
                reason = Reason.EXTRA_OPENING_PARENTHESIS;
            } else if (s.contains("Invalid number of operands available for")) {
                reason = Reason.INVALID_NUMBER_OF_OPERANDS;
            } else {
                Lg.w(TAG, "Unrecognized exception" + e);
            }
        } else {
            Lg.w(TAG, "Unrecognized exception" + e);
        }
        return new InvalidEquationException(expressionString, reason, e.getMessage());
    }
}
