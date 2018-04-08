package paul.wintz.nodes;

public interface IPlug<T> {

    Class<T> getInputType();

    boolean plugin(ISocket<?> socket);
    void unplug();
    boolean isPlugged();

    T getInput();

}