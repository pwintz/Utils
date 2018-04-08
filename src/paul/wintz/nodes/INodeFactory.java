package paul.wintz.nodes;

import java.util.List;

public interface INodeFactory {
    Node<?> createNode(String name);
    List<String> getNames();
}
