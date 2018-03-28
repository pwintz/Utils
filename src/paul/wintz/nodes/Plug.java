package paul.wintz.nodes;

public class Plug<T> implements IPlug<T> {
	private final Class<T> type;
	private ISocket<? extends T> pluggedSocket;
	private final T defaultValue;
	
	public Plug(Class<T> type) {
		this(type, null);
	}
	
	public Plug(Class<T> type, T defaultValue) {
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Class<T> getInputType() {
		return type;
	}
	
	@Override
	@SuppressWarnings("unchecked") // isAssignableFrom() checks that the socket is compatible.
	public boolean plugin(ISocket<?> socket) {
		if(getInputType().isAssignableFrom(socket.getOutputType())) {
			this.pluggedSocket = (ISocket<? extends T>) socket;
			return true;
		} 
		
		return false;
	}
	
	@Override
	public void unplug() {
		this.pluggedSocket = null;
	}
	
	@Override
	public boolean isPlugged() {
		return pluggedSocket != null;
	}
	
	@Override
	public T getInput() {
		if(isPlugged())
			return pluggedSocket.getOutput();
		
		return defaultValue;
	}

	@Override
	public String toString() {
		return "Plug{type=" + type + ", pluggedSocket=" + pluggedSocket + "}";
	}

}