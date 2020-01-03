package paul.wintz.images;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadata;

public class MetadataUtils {

    public static void displayMetadata(IIOMetadata metadata) {
        Node asTree = metadata.getAsTree(metadata.getNativeMetadataFormatName());
        displayMetadata(asTree);
    }

    public static void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    private static void indent(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("    ");
    }

    private static void displayMetadata(Node node, int level) {
        // print open tag of element
        indent(level);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) {

            // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);

                if(i == 0) {
                    System.out.println();
                }
                indent(level);

                System.out.print("\t");
                System.out.print(attr.getNodeName() +
                        "=\"" + attr.getNodeValue() + "\"");
                if(i < length-1) {
                    System.out.println();
                }
            }
        }

        Node child = node.getFirstChild();
        if (child == null) {
            // no children, so close element and return
            System.out.println("/>");
            return;
        }

        // children, so close current tag
        System.out.println(">");
        while (child != null) {
            // print children recursively
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }

        // print close tag of element
        indent(level);
        System.out.println("</" + node.getNodeName() + ">");
    }
}
