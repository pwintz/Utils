package paul.wintz.nodes;

public abstract class Socket<T> implements ISocket<T> {

	private final Class<T> outputType;

	public Socket(Class<T> clazz) {
		this.outputType = clazz;
	}
	
	@Override
	public Class<T> getOutputType() {
		return outputType;
	}
}
