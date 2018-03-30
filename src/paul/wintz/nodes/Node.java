package paul.wintz.nodes;

import static com.google.common.base.Preconditions.*;

import java.util.*;

public abstract class Node<T> implements ISocket<T> {
	
	private final Map<String, Plug<?>> plugs = new LinkedHashMap<>();
	private final Class<T> outputType;

	public Node(Class<T> outputType) {
		this.outputType = checkNotNull(outputType);
	}
	
	protected void addPlug(String name, Plug<?> plug) {
		checkArgument(!plugs.containsKey(name));
		checkArgument(!plugs.containsValue(plug));
		plugs.put(checkNotNull(name), checkNotNull(plug));
	}

	@Override
	public Class<T> getOutputType() {
		return outputType;
	}

	public Set<String> getPlugNames() {
		return plugs.keySet();
	}

	public Optional<IPlug<?>> getPlug(String plugName) {
		return Optional.ofNullable(plugs.get(plugName));
	}
	
	public boolean attemptPlugin(String plugName, ISocket<?> socket) {
		checkNotNull(socket);
		Optional<IPlug<?>> plug = getPlug(plugName);
		return plug.isPresent() && plug.get().plugin(socket);
	}
	
	@Override
	public String toString() {
		return "Node{plugs=" + plugs + ", outputType=" + outputType + "}";
	}
	
}