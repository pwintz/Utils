package paul.wintz.uioptiontypes.values;

import javax.annotation.Nullable;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.File;

class ValueOptionToJsonConverter {

    public static void setValueFromJson(String name, OptionItem<?> optionItem, @Nullable JsonObject json) {
        if(json == null) {
            optionItem.setValueToDefault();
            return;
        }
        if(Integer.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getOption is guaranteed to match the valueType
            ValueOption<Integer> option = (ValueOption<Integer>) optionItem.getOption();

            int value = json.getInt(name, (Integer) optionItem.defaultValue);
            option.setValue(value);
        } else if(Float.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getOption is guaranteed to match the valueType
            ValueOption<Float> option = (ValueOption<Float>) optionItem.getOption();

            JsonNumber jsonNumber = json.getJsonNumber(name);
            float value;
            if(jsonNumber != null){
                value = (float) jsonNumber.doubleValue();
            } else {
                value = (Float) optionItem.defaultValue;
            }

            option.setValue(value);
        } else if(Boolean.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getOption is guaranteed to match the valueType
            ValueOption<Boolean> option = (ValueOption<Boolean>) optionItem.getOption();
            boolean value = json.getBoolean(name, (Boolean) optionItem.defaultValue);
            option.setValue(value);
        } else if(String.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getOption is guaranteed to match the valueType
            ValueOption<String> option = (ValueOption<String>) optionItem.getOption();
            String value = json.getString(name, (String) optionItem.defaultValue);
            option.setValue(value);
        } else if(File.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getOption is guaranteed to match the valueType
            ValueOption<File> option = (ValueOption<File>) optionItem.getOption();
            String value = json.getString(name, optionItem.defaultValue.toString());
            option.setValue(new File(value));
        } else {
            throw new RuntimeException("Unrecognized data type:" + optionItem.valueType);
        }
    }

    public static void putValueInJson(String name, OptionItem<?> optionItem, JsonObjectBuilder jsonObjectBuilder){

        if(Integer.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getValue is guaranteed to match the valueType
            ValueOption<Integer> option = (ValueOption<Integer>) optionItem.getOption();
            Integer value = option.getValue();
            jsonObjectBuilder.add(name, value);
        } else if(Float.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getValue is guaranteed to match the valueType
            ValueOption<Float> option = (ValueOption<Float>) optionItem.getOption();
            Float value = option.getValue();
            jsonObjectBuilder.add(name, value);
        } else if(Boolean.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getValue is guaranteed to match the valueType
            ValueOption<Boolean> option = (ValueOption<Boolean>) optionItem.getOption();
            boolean value = option.getValue();
            jsonObjectBuilder.add(name, value);
        } else if(String.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getValue is guaranteed to match the valueType
            ValueOption<String> option = (ValueOption<String>) optionItem.getOption();
            String value = option.getValue();
            jsonObjectBuilder.add(name, value);
        } else if(File.class == optionItem.valueType) {
            @SuppressWarnings("unchecked") // The type returned by getValue is guaranteed to match the valueType
            ValueOption<File> option = (ValueOption<File>) optionItem.getOption();
            File value = option.getValue();
            jsonObjectBuilder.add(name, value.toString());
        } else {
            throw new RuntimeException("Unrecognized data type:" + optionItem.valueType);
        }
    }
}
