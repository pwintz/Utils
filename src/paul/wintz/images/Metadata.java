package paul.wintz.images;

import com.sun.imageio.plugins.gif.GIFImageMetadata;
import org.w3c.dom.Node;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class Metadata {

    public static void main(String[] args) throws IOException {
//        processFile("animation2.gif");


        final File file = new File("still.png");
        File out = new File("output.png");
        HashMap<String, String> map = new HashMap<>();

        map.put("spyrotechnic json", "{key: value, key2:value2}");

        PNGMetadataUtils.addMetadata(file, out, map);
        PNGMetadataUtils.readMetadata(out, map.keySet());

        Set<String> map2 = new HashSet<>();
        map2.add("Hello");
        PNGMetadataUtils.readMetadata(out, map2);

    }

    private static void print(Object s){
        System.out.println(s);
    }

    private static String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    private static void processFile(String fileName) {

        File file = new File(fileName);
        String extension = getExtension(file.toString());

        Metadata metadataPrinter = new Metadata();

        System.out.println("\n=====\n");
        System.out.println(fileName);
        try {
            // Retrieve image.
            BufferedImage bi = ImageIO.read(file);
//            File outputfile = new File("data/out.gif");
//            ImageIO.write(bi, "gif", outputfile);

            ImageWriter writer = ImageIO.getImageWritersBySuffix(extension).next();

            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            String metadata_format_name = metadata.getNativeMetadataFormatName();

            // Display metadata
            Node asTree = metadata.getAsTree(metadata_format_name);
            MetadataUtils.displayMetadata(asTree);

//            // Create PNG metadata to add to existing metadata
//            GIFStreamMetadata gifStreamMetadata = new GIFStreamMetadata();
//            gifStreamMetadata.backgroundColorIndex = -1;
//            Node gifStreamMetadataAsTree = gifStreamMetadata.getAsTree(metadata_format_name);
//
//            // Merge with existing metadata
//            metadata.mergeTree(metadata_format_name, gifStreamMetadataAsTree);

            // Create PNG metadata to add to existing metadata
            GIFImageMetadata gifMetadata = new GIFImageMetadata();
            gifMetadata.transparentColorIndex = -1;
            Node gifMetadataAsTree = gifMetadata.getAsTree(metadata_format_name);

            // Merge with existing metadata
            metadata.mergeTree(metadata_format_name, gifMetadataAsTree);

            MetadataUtils.displayMetadata(metadata.getAsTree(metadata_format_name));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
            writer.setOutput(stream);
            writer.write(metadata, new IIOImage(bi, null, metadata), writeParam);

            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Metadata metadata = new Metadata();
//        metadata.readAndDisplayMetadata(fileName.toString());
    }

    void readAndDisplayMetadata( String fileName ) {

        System.out.println("\n=====\n");
        System.out.println(fileName);
        try {

            File file = new File( fileName );
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext()) {

                // pick the first available ImageReader
                ImageReader reader = readers.next();

                // attach source to the reader
                reader.setInput(iis, true);

                // read metadata of first image
                IIOMetadata metadata = reader.getImageMetadata(0);

                String[] names = metadata.getMetadataFormatNames();
                for(String name : names) {
                    System.out.println("Format name: " + name);
                    MetadataUtils.displayMetadata(metadata.getAsTree(name));
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}


