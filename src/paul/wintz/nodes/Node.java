package paul.wintz.nodes;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;

import com.google.common.collect.Multimap;

public abstract class Node<T> implements ISocket<T> {
	
	private final Map<String, Plug<?>> plugs = new LinkedHashMap<>();
	private final Class<T> outputType;

	public Node(Class<T> clazz) {
		this.outputType = clazz;
	}
	
	protected final void addPlug(String name, Plug<?> plug) {
		plugs.put(checkNotNull(name), checkNotNull(plug));
	}

	@Override
	public Class<T> getOutputType() {
		return outputType;
	}

	public Set<String> getPlugNames() {
		return plugs.keySet();
	}

	public IPlug<?> getPlug(String plugName) {
		return plugs.get(plugName);
	}
	
	@Override
	public String toString() {
		return "Node{plugs=" + plugs + ", outputType=" + outputType + "}";
	}
	
}