package paul.wintz.images;

import com.sun.imageio.plugins.png.PNGMetadata;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PNGMetadataUtilsTest {

    PNGMetadata pngMetadata = new PNGMetadata();

    @Test
    public void addTextEntry() throws Exception {
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value");
        String value = PNGMetadataUtils.getTextEntry(pngMetadata, "my_key");

        assertThat(value, is(equalTo("my_value")));
    }

    @Test
    public void addTwoTextEntries() throws Exception {
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key1", "my_value1");
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key2", "my_value2");

        String value1 = PNGMetadataUtils.getTextEntry(pngMetadata, "my_key1");
        String value2 = PNGMetadataUtils.getTextEntry(pngMetadata, "my_key2");

        assertThat(value1, is(equalTo("my_value1")));
        assertThat(value2, is(equalTo("my_value2")));
    }

    @Test
    public void addingSameKeyTwiceOverwritesEntry() throws Exception {
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value1");
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value2");

        String value = PNGMetadataUtils.getTextEntry(pngMetadata, "my_key");

        assertThat(value, is(equalTo("my_value2")));
    }

    @Test
    public void addingSameKeyTwiceDoesNotAffectOtherEntry() throws Exception {
        PNGMetadataUtils.addTextEntry(pngMetadata, "other_key", "other_value");
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value1");
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value2");

        String other_value = PNGMetadataUtils.getTextEntry(pngMetadata, "other_key");

        assertThat(other_value, is(equalTo("other_value")));
    }

    @Test
    public void removeTextEntry() throws Exception {
        PNGMetadataUtils.addTextEntry(pngMetadata, "my_key", "my_value");

        PNGMetadataUtils.removeTextEntry(pngMetadata, "my_key");

        String value = PNGMetadataUtils.getTextEntry(pngMetadata, "my_key");

        assertThat(value, is(equalTo(null)));
    }

}