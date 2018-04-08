package paul.wintz.nodes;

import java.util.*;

public class NodeGraph {
    private final Set<Node<?>> nodes = new HashSet<>();
    
    public void addNode(Node<?> node) {
        nodes.add(node);
    }
    
    public void removeNode(Node<?> node) {
        nodes.remove(node);
    }
    
    public void clearNodes() {
        nodes.clear();
    }
    
    public void checkForCycles() {
        //TODO check for cycles 
    }
    
}
