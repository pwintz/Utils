package paul.wintz.nodes;

public interface ISocket<T> {
	abstract T getOutput();
	abstract Class<T> getOutputType();
}