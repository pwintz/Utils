package paul.wintz.nodes;

public interface ISocket<T> {
	T getOutput();
	Class<T> getOutputType();
}