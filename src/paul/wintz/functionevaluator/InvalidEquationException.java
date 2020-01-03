package paul.wintz.functionevaluator;

import com.google.common.base.MoreObjects;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import paul.wintz.utils.logging.Lg;

import java.util.EmptyStackException;

@SuppressWarnings("WeakerAccess")
public class InvalidEquationException extends Exception {
    private static final String TAG = Lg.makeTAG(InvalidEquationException.class);

    private final String expression;
    private final Reason reason;
    private final String explanation;

    public enum Reason {
        EMPTY_EXPRESSION,
        UNKNOWN_SYMBOL,
        BAD_NUMBER_FORMAT,
        INVALID_NUMBER_OF_OPERANDS,
        EXTRA_OPENING_PARENTHESIS,
        EXTRA_CLOSING_PARENTHESIS,
        INVALID_NUMBER_OF_FUNCTION_ARGUMENTS,
        MISPLACED_COMMA,
        UNKNOWN_EXCEPTION
    }

    private InvalidEquationException(String expression, Reason reason, String explanation) {
        this.expression = "\"" + expression + "\"";
        this.reason = reason;
        this.explanation = explanation;
    }

    @Override
    public String getMessage() {
        return MoreObjects.toStringHelper(InvalidEquationException.class)
                .add("expression", expression)
                .add("reasons", reason)
                .add("explanation", explanation)
                .toString();
    }

    public String getExplanation() {
        return explanation;
    }

    static InvalidEquationException construct(Exception e, String expressionString) {
        Reason reason = getReason(e);
        if(reason == Reason.UNKNOWN_EXCEPTION) {
            Lg.w(TAG, "The equation '%s' caused an unrecognized exception: %s for", expressionString, e);
        }
        return new InvalidEquationException(expressionString, reason, e.getMessage());
    }

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
        if (e instanceof IllegalArgumentException) {
            String s = e.getMessage();
            if ("Expression can not be empty".equals(s)) {
                return Reason.EMPTY_EXPRESSION;
            }
            if ("Operator is unknown for token.".equals(s)) {
                return Reason.UNKNOWN_SYMBOL;
            }
            if (s.startsWith("Unable to parse char ")){
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
            if("Misplaced function separator ',' or mismatched parentheses".equals(s)){
                return Reason.MISPLACED_COMMA;
            }
        }
        return Reason.UNKNOWN_EXCEPTION;
    }

    public Reason getReason() {
        return reason;
    }
}
