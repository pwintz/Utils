package paul.wintz.uioptiontypes;

import static com.google.common.base.Preconditions.*;

import java.util.*;

public abstract class UserInputOption<T> implements OptionItem {

    private final List<OnValueChangedListener<T>> onValueChangedListeners = new ArrayList<>();
    private ValidityEvaluator<T> validityEvaluator = value -> true;

    private T currentValue;

    public interface OnValueChangedListener<T> {
        void onValueChanged(T newValue);
    }

    public interface ValidityEvaluator<T> {
        boolean isValid(T value);
    }

    public UserInputOption(String description) {
        this.description = description;
    }

    protected String description;

    @Override
    public final String getDescription() {
        return description;
    }
    
    public void addOnValueChangedListener(OnValueChangedListener<T> callback) {
        onValueChangedListeners.add(callback);
    }

    public void addOnValueChangedListenerAndCall(OnValueChangedListener<T> callback) {
        addOnValueChangedListener(callback);
        callback.onValueChanged(getValue());
    }

    private final void doCallbacks() {
        for (final OnValueChangedListener<T> callback : onValueChangedListeners) {
            callback.onValueChanged(getValue());
        }
    }

    public void setValidityEvaluator(ValidityEvaluator<T> validityEvaluator) {
        this.validityEvaluator = checkNotNull(validityEvaluator);
    }

    public void setValue(T newValue) {
        checkArgument(isValidValue(newValue), "The value %s was rejected by %s", newValue, validityEvaluator);

        currentValue = newValue;
        doCallbacks();
    }

    public T getValue(){
        return currentValue;
    }

    public boolean isValidValue(T value) {
        return validityEvaluator.isValid(value);
    }

    @Override
    public String toString() {
        String text = getDescription();
        if (getValue() != null) {
            text += ": " + getValue();
        }
        return text;
    }
}