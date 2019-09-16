package paul.wintz.uioptiontypes.values;

import paul.wintz.utils.logging.Lg;

import javax.annotation.Nullable;
import javax.json.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
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

    public void save() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for(Map.Entry<String, OptionItem<?>> optionItem : optionItems.entrySet()) {
            ValueOptionToJsonConverter.putValueInJson(optionItem.getKey(), optionItem.getValue(), jsonObjectBuilder);
        }
        jsonIO.save(jsonObjectBuilder.build());
    }

    private static class OptionItem<T> {
        private ValueOption<T> option = null; // is null until connected to an option.
        private final Class<T> valueType;
        private final T defaultValue;

        private OptionItem(ValueOption<T> option, Class<T> valueType, T defaultValue) {
            this.option = checkNotNull(option);
            this.valueType = checkNotNull(valueType);
            this.defaultValue = defaultValue;
        }

        private void setValueToDefault() {
            option.setValue(defaultValue);
        }
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

    private static class ValueOptionToJsonConverter {

        public static void setValueFromJson(String name, OptionItem<?> optionItem, @Nullable JsonObject json) {
            if(json == null) {
                optionItem.setValueToDefault();
                return;
            }
            if(optionItem.valueType == Integer.class) {
                int value = json.getInt(name, (Integer) optionItem.defaultValue);
                ((ValueOption<Integer>)optionItem.option).setValue(value);
            } else if(optionItem.valueType == Float.class) {
                JsonNumber jsonNumber = json.getJsonNumber(name);
                float value;
                if(jsonNumber != null){
                    value = (float) jsonNumber.doubleValue();
                } else {
                    value = (Float) optionItem.defaultValue;
                }
                ((ValueOption<Float>)optionItem.option).setValue(value);
            } else if(optionItem.valueType == Boolean.class) {
                boolean value = json.getBoolean(name, (Boolean) optionItem.defaultValue);
                ((ValueOption<Boolean>)optionItem.option).setValue(value);
            } else if(optionItem.valueType == String.class) {
                String value = json.getString(name, (String) optionItem.defaultValue);
                ((ValueOption<String>)optionItem.option).setValue(value);
            } else {
                throw new RuntimeException("Unrecognized data type:" + optionItem.valueType);
            }
        }

        public static void putValueInJson(String name, OptionItem<?> optionItem, JsonObjectBuilder jsonObjectBuilder){
            if(optionItem.valueType == Integer.class) {
                Integer value = ((ValueOption<Integer>)optionItem.option).getValue();
                jsonObjectBuilder.add(name, value);
            } else if(optionItem.valueType == Float.class) {
                Float value = ((ValueOption<Float>)optionItem.option).getValue();
                jsonObjectBuilder.add(name, value);
            } else if(optionItem.valueType == Boolean.class) {
                boolean value = ((ValueOption<Boolean>)optionItem.option).getValue();
                jsonObjectBuilder.add(name, value);
            } else if(optionItem.valueType == String.class) {
                String value = ((ValueOption<String>)optionItem.option).getValue();
                jsonObjectBuilder.add(name, value);
            } else {
                throw new RuntimeException("Unrecognized data type:" + optionItem.valueType);
            }
        }
    }

}
