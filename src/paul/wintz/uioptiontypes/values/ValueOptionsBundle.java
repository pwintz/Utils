package paul.wintz.uioptiontypes.values;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nullable;
import javax.json.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValueOptionsBundle {
    public static final String TAG = Lg.makeTAG(ValueOptionsBundle.class);

    private final JsonIO jsonIO;
    private Map<String, OptionItem<?>> optionItems = new HashMap<>();
    private Map<String, ValueOptionsBundle> subBundles = new HashMap<>();
    @Nullable private JsonObject jsonObject;

    public ValueOptionsBundle(String jsonFilename) {
        this(new JsonFileIO(jsonFilename));
    }

    public ValueOptionsBundle(JsonIO jsonIO) {
        this.jsonIO = jsonIO;
        jsonObject = jsonIO.load();
    }

    // Create a Json object out of all the options stored in optionItems
    public JsonObject getJsonObject(){
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for(Map.Entry<String, OptionItem<?>> optionItem : optionItems.entrySet()) {
            ValueOptionToJsonConverter.putValueInJson(optionItem.getKey(), optionItem.getValue(), jsonObjectBuilder);
        }
        return jsonObjectBuilder.build();
    }

    public String getJsonString(){
        return getJsonObject().toString();
    }

    public void save() {
        jsonIO.save(getJsonObject());
    }

    public void fromString(String jsonString) {

        try {
            JsonReader reader = Json.createReader(new StringReader(jsonString));
            JsonObject jsonObject = reader.readObject();

            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            for(Map.Entry<String, OptionItem<?>> optionItem : optionItems.entrySet()) {
                ValueOptionToJsonConverter.setValueFromJson(optionItem.getKey(), optionItem.getValue(), jsonObject);
            }
        } catch (JsonException e) {
            Lg.w(TAG, "Loading Json failed.", e);
        }
}

    public ValueOptionsBundle connectFile(String name, ValueOption<File> option, String defaultValue) {
        return connect(name, new OptionItem<>(option, File.class, new File(defaultValue)));
    }

    public ValueOptionsBundle connect(String name, ValueOption<Float> option, Float defaultValue){
        return connect(name, new OptionItem<>(option, Float.class, defaultValue));
    }

    public ValueOptionsBundle connect(String name, ValueOption<Integer> option, Integer defaultValue){
        return connect(name, new OptionItem<>(option, Integer.class, defaultValue));
    }

    public ValueOptionsBundle connect(String name, ValueOption<Boolean> option, Boolean defaultValue){
        return connect(name, new OptionItem<>(option, Boolean.class, defaultValue));
    }

    public ValueOptionsBundle connect(String name, ValueOption<String> option, String defaultValue){
        return connect(name, new OptionItem<>(option, String.class, defaultValue));
    }

    public ValueOptionsBundle add(String name, ValueOptionsBundle subBundle){
        subBundles.put(checkNotNull(name), subBundle);
        return this;
    }

    private ValueOptionsBundle connect(String name, OptionItem<?> optionItem) {
        optionItems.put(checkNotNull(name), optionItem);
        ValueOptionToJsonConverter.setValueFromJson(name, optionItem, jsonObject);
        return this;
    }

    interface JsonIO {
        @Nullable JsonObject load();
        void save(JsonObject jsonObject);
    }

    static class JsonFileIO implements JsonIO {
        private final String filename;

        JsonFileIO(String filename){
            this.filename = filename;
        }

        @Override
        @Nullable
        public JsonObject load() {
            try {
                JsonReader reader = Json.createReader(new BufferedReader(new FileReader(filename)));
                return reader.readObject();
            } catch (JsonException e) {
                Lg.w(TAG, "Loading Json failed.", e);
            } catch (FileNotFoundException e) {
                Lg.w(TAG, "Json file not found at %s. Using default values.", e.getMessage());
            }
            return null;
        }

        @Override
        public void save(JsonObject jsonObject){
            try (PrintWriter out = new PrintWriter(filename)) {
                JsonWriter writer = Json.createWriter(out);
                writer.writeObject(jsonObject);
            } catch (FileNotFoundException e) {
                Lg.e(TAG, "Could not save Json", e);
            }
        }
    }

}
