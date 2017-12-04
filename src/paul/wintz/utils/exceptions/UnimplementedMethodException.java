package paul.wintz.utils.exceptions;

@SuppressWarnings("serial")
public class UnimplementedMethodException extends RuntimeException {

	public UnimplementedMethodException() {
		super("An unimplemented method was called.");
	}

}
