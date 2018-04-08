package paul.wintz.utils.exceptions;

public class UnhandledCaseException extends RuntimeException {

    private static final long serialVersionUID = -897809136910152370L;

    public UnhandledCaseException(Object missingCase) {
        super("Programming error: The case '" + missingCase + "' is not handled.");
    }

}
