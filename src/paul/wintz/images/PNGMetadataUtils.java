package paul.wintz.images;

import com.sun.imageio.plugins.png.PNGMetadata;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import paul.wintz.utils.logging.Lg;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Copied from StackOverflow https://stackoverflow.com/a/41282314/6651650
public class PNGMetadataUtils {
    private static final String TAG = Lg.makeTAG(PNGMetadata.class);

    public static void addMetadata(File file, File out, Map<String, String> entries) throws IOException {

        try (ImageInputStream input = ImageIO.createImageInputStream(file);
             ImageOutputStream output = ImageIO.createImageOutputStream(out)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

            if (!readers.hasNext()) {
                throw new IOException("No readers, this operation is unsupported.");
            }

            ImageReader reader = readers.next();

            reader.setInput(input);
            IIOImage image = reader.readAll(0, null);

            for (Map.Entry<String, String> entry : entries.entrySet()) {
                addTextEntry(image.getMetadata(), entry.getKey(), entry.getValue());
            }

            ImageWriter writer = ImageIO.getImageWriter(reader); // TODO: Validate that there are writers

            if(writer == null){
                throw new IOException("No ImageWriter available for reader:" + reader);
            }

            writer.setOutput(output);
            writer.write(image);
        }
    }

    public static Map<String, String> readMetadata(File file, Set<String> keys) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        try (ImageInputStream input = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

            if(!readers.hasNext()){
                throw new IOException("No image readers available for input:" + input);
            }

            ImageReader reader = readers.next();

            reader.setInput(input);

            IIOMetadata imageMetadata = reader.getImageMetadata(0);
            for(String key : keys) {
                String value = getTextEntry(imageMetadata, key);
                map.put(key, value);
                System.out.println("key: " + key + ", value: " + value);
            }

        }
        return map;
    }

    public static String readMetadata(File file, String key) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        try (ImageInputStream input = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new RuntimeException("No image readers available for input:" + input);
            }
            ImageReader reader = readers.next();

            reader.setInput(input);

            IIOMetadata imageMetadata = reader.getImageMetadata(0);
            return getTextEntry(imageMetadata, key);
        }
    }

    static void addTextEntry(final IIOMetadata metadata, final String key, final String value) throws IIOInvalidTreeException {
        // If an entry with the given key already exists, delete it.
        removeTextEntry(metadata, key);

        IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
        textEntry.setAttribute("keyword", key);
        textEntry.setAttribute("value", value);

        IIOMetadataNode text = new IIOMetadataNode("Text");
        text.appendChild(textEntry);

        IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
        root.appendChild(text);

        metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
    }

    static String getTextEntry(final IIOMetadata metadata, final String key) {
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        NodeList entries = root.getElementsByTagName("TextEntry");

        for (int i = 0; i < entries.getLength(); i++) {
            IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
            if (node.getAttribute("keyword").equals(key)) {
                return node.getAttribute("value");
            }
        }

        return null;
    }

    static void removeTextEntry(final IIOMetadata metadata, final String key) {
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        NodeList entries = root.getElementsByTagName("TextEntry");

        for (int i = 0; i < entries.getLength(); i++) {
            IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
            if (node.getAttribute("keyword").equals(key)) {
                Node parentNode = node.getParentNode();
                parentNode.removeChild(node);

                // If the parent node is now empty, we'll delete it from the root.
                if(!parentNode.hasChildNodes()){
                    root.removeChild(parentNode);
                }
            }
        }

        // Update the metadata object to reflect changes to root.
        try {
            metadata.setFromTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
        } catch (IIOInvalidTreeException e) {
            // This shouldn't happen because we are starting with a valid Metadata and are removing a nonrequried node.
            throw new RuntimeException("Failed to set metadata from modified tree", e);
        }
    }
}
