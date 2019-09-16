package paul.wintz.uioptiontypes.values;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Nullable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked") // Ignore the use of raw type for the listener so we don't need to duplicate it for every type.
@RunWith(MockitoJUnitRunner.class)
public class ValueOptionsBundleTest {

    FakeJsonIO jsonIO;
    @Spy ValueOption.ValueChangeCallback listener;

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(listener);
    }

    @Test
    public void saveEmpty() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(0);
    }

    @Test
    public void loadFloatFromIO() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder()
                .put("temperature", 101.7f)
                .build();
        FloatOption floatOption = FloatOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("temperature", floatOption, 98.7f); // default is not used

        Mockito.verify(listener).callback(101.7f);
    }

    @Test
    public void loadFloatFromDefault() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        FloatOption floatOption = FloatOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("Air speed", floatOption, 1232.3f);

        Mockito.verify(listener).callback(1232.3f);
    }

    @Test
    public void saveFloat() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        FloatOption floatOption = FloatOption.builder().build();
        valueOptionsBundle.connect("velocity", floatOption, -22.3f);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("velocity", -22.3f);
    }

    @Test
    public void saveModifiedFloat() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        FloatOption floatOption = FloatOption.builder().build();
        valueOptionsBundle.connect("penguins per sq mile", floatOption, 0f);

        floatOption.setValue(500.1f);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("penguins per sq mile", 500.1f); // (They marched)
    }

    ///////////////
    /// STRINGS ///
    ///////////////
    @Test
    public void loadStringFromIO() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder()
                .put("name", "Batman")
                .build();
        StringOption stringOption = StringOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("name", stringOption, "Bruce Wayne"); // default is not used

        Mockito.verify(listener).callback("Batman");
    }

    @Test
    public void loadStringFromDefault() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        StringOption stringOption = StringOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("password", stringOption, "P@$$w0rd");

        Mockito.verify(listener).callback("P@$$w0rd");
    }

    @Test
    public void saveString() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        StringOption stringOption = StringOption.builder().build();
        valueOptionsBundle.connect("last meal", stringOption, "worms");

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("last meal", "worms");
    }

    @Test
    public void saveModifiedString() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        StringOption stringOption = StringOption.builder().build();
        valueOptionsBundle.connect("name", stringOption, "Val Jean");

        stringOption.setValue("Monsieur Madeleine");

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("name", "Monsieur Madeleine");
    }

    //////////////
    // Integers //
    //////////////
    @Test
    public void loadIntegerFromIO() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder()
                .put("id number", 123)
                .build();
        IntegerOption intOption = IntegerOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("id number", intOption, 0); // default is not used

        Mockito.verify(listener).callback(123);
    }

    @Test
    public void loadIntegerFromDefault() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        IntegerOption intOption = IntegerOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("id number", intOption, 1432);

        Mockito.verify(listener).callback(1432);
    }

    @Test
    public void saveInteger() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        IntegerOption intOption = IntegerOption.builder().build();
        valueOptionsBundle.connect("id number", intOption, 123);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("id number", 123);
    }

    @Test
    public void saveModifiedInteger() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        IntegerOption intOption = IntegerOption.builder().build();
        valueOptionsBundle.connect("id number", intOption, 123);

        intOption.setValue(987);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("id number", 987);
    }

    ////////////////
    /// Booleans ///
    ////////////////
    @Test
    public void loadBooleanFromIO() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder()
                .put("am I cool?", false)
                .build();
        BooleanOption booleanOption = BooleanOption.builder()
                .addViewValueChangeCallback(listener)
                .initial(true) // this is necessary otherwise the "change" is ignored.
                .build();
        valueOptionsBundle.connect("am I cool?", booleanOption, true); // default is not used

        Mockito.verify(listener).callback(false);
    }

    @Test
    public void loadBooleanFromDefault() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        BooleanOption booleanOption = BooleanOption.builder()
                .addViewValueChangeCallback(listener)
                .build();
        valueOptionsBundle.connect("am I smart?", booleanOption, true);

        Mockito.verify(listener).callback(true);
    }

    @Test
    public void saveBoolean() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        BooleanOption booleanOption = BooleanOption.builder().build();
        valueOptionsBundle.connect("am I awkward?", booleanOption, true);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("am I awkward?", true);
    }

    @Test
    public void saveModifiedBoolean() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().build();
        BooleanOption booleanOption = BooleanOption.builder().build();
        valueOptionsBundle.connect("am I old?", booleanOption, false);

        booleanOption.setValue(true);

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(1);
        jsonIO.assertSaved("am I old?", true);
    }

    ///////////
    // Other //
    ///////////
    @Test
    public void defaultsAreUsedIfNoJsonIsAvailable() {
        ValueOptionsBundle valueOptionsBundle = bundleBuilder().buildWithMissingJson();

        IntegerOption intOption = IntegerOption.builder().build();
        BooleanOption booleanOption = BooleanOption.builder().build();
        FloatOption floatOption = FloatOption.builder().build();
        StringOption stringOption = StringOption.builder().build();

        valueOptionsBundle.connect("an int", intOption, 256);
        valueOptionsBundle.connect("a boolean", booleanOption, true);
        valueOptionsBundle.connect("a float", floatOption, 3.14f);
        valueOptionsBundle.connect("a string", stringOption, "Paul Wintz");

        valueOptionsBundle.save();

        jsonIO.assertSavedSize(4);
        jsonIO.assertSaved("an int", 256);
        jsonIO.assertSaved("a boolean", true);
        jsonIO.assertSaved("a float", 3.14f);
        jsonIO.assertSaved("a string", "Paul Wintz");
    }

    ValueOptionsBundleBuilder bundleBuilder() {
        return new ValueOptionsBundleBuilder();
    }

    class ValueOptionsBundleBuilder {
        Map<String, Object> jsonMap = new HashMap<>();

        ValueOptionsBundleBuilder put(String key, Float value) {
            jsonMap.put(key, (double) value);
            return this;
        }

        ValueOptionsBundleBuilder put(String key, Object value) {
            jsonMap.put(key, value);
            return this;
        }

        ValueOptionsBundle build() {
            jsonIO = new FakeJsonIO(jsonMap);
            return new ValueOptionsBundle(jsonIO);
        }

        ValueOptionsBundle buildWithMissingJson() {
            jsonIO = new FakeJsonIO();
            return new ValueOptionsBundle(jsonIO);
        }
    }

    static class FakeJsonIO implements ValueOptionsBundle.JsonIO {
        private final JsonObjectBuilder jsonObjectBuilder;
        JsonObject savedJson;

        FakeJsonIO() {
            jsonObjectBuilder = null;
        }

        FakeJsonIO(Map<String, Object> jsonMap) {
            jsonObjectBuilder = Json.createObjectBuilder(jsonMap);
        }

        @Override
        @Nullable
        public JsonObject load() {
            if(jsonObjectBuilder == null) {
                return null;
            }
            return jsonObjectBuilder.build();
        }

        @Override
        public void save(JsonObject jsonObject) {
            savedJson = jsonObject;
        }

        void assertSavedSize(int expectedSize){
            assertThat(savedJson.keySet().size(), is(equalTo(expectedSize)));
        }

        public void assertSaved(String key, Integer expectedValue) {
            assertThat(savedJson.getInt(key), is(equalTo(expectedValue)));
        }

        public void assertSaved(String key, Boolean expectedValue) {
            assertThat(savedJson.getBoolean(key), is(equalTo(expectedValue)));
        }

        public void assertSaved(String key, String expectedValue) {
            assertThat(savedJson.getString(key), is(equalTo(expectedValue)));
        }

        public void assertSaved(String key, Float expectedValue) {
            assertThat(savedJson.getJsonNumber(key).doubleValue(), is(closeTo(expectedValue, 1e-8)));
        }
    }
}