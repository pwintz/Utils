package paul.wintz.nodes;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;

@SuppressWarnings("ALL")
public class NodesGraphController {

	private INodeFactory nodeFactory;
	private Set<Plug<?>> endPlugs = new LinkedHashSet<>();
	
	public NodesGraphController(INodeFactory nodeFactory) {
		this.nodeFactory = checkNotNull(nodeFactory);
	}
	
	public void addEndPlug(Plug<?> endPlug) {
		endPlugs.add(endPlug);
	}
	
	public void addNode(String name){}
		
}
