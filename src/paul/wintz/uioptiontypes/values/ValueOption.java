package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableList;
import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ValueOption<T> {
    @SuppressWarnings("unused") private static final String TAG = Lg.makeTAG(ValueOption.class);
    @Nullable private final String description;

    @FunctionalInterface
    public interface ValueChangeCallback<T> {
        void callback(T value);
    }

    @FunctionalInterface
    public interface ValueValidator<T> {
        boolean isValid(T value);
    }

    @FunctionalInterface
    public interface StateValidator {
        boolean isValid();
    }

    private T value;
    private final Set<ValueChangeCallback<T>> viewValueChangeCallbacks = new HashSet<>();
    private final Set<ValueChangeCallback<T>> modelValueChangeCallbacks = new HashSet<>();
    private final ImmutableList<ValueValidator<T>> valueValueValidator;
    private final ImmutableList<StateValidator> stateValidators;
    private boolean emitIfNewValueEqualsOld;

    protected ValueOption(Builder<T, ?> builder) {
        builder.checkValue(builder.initial, "initial");
        viewValueChangeCallbacks.addAll(builder.viewValueChangeCallback);
        builder.viewValueChangeCallback.clear();
        valueValueValidator = ImmutableList.copyOf(builder.valueValidators);
        stateValidators = ImmutableList.copyOf(builder.stateValidators);
        if(builder.callbackOnInitialization){
            emitViewValueChanged(builder.initial);
        }
        this.value = checkNotNull(builder.initial);
        this.description = builder.description;
        this.emitIfNewValueEqualsOld = builder.emitIfNewValueEqualsOld;
    }

    /**
     * Notify the callback that the value of this option changed in the view.
     * @param newValue the value that the view was changed to.
     * @return true if the change is legal, false otherwise. If the newValue equals the old value,
     * and the change is otherwise legal, then true is returned.
     */
    public boolean emitViewValueChanged(@Nonnull T newValue) {
        if(!isStateValid()) {
            Lg.w(TAG, "Change not emitted -- state not valid.");
            return false;
        }
        if(!isValueValid(newValue)) {
            Lg.w(TAG, "Change not emitted -- new value is not valid: %s.", newValue);
            return false;
        }
        if (!emitIfNewValueEqualsOld && newValue.equals(this.value)) {
            // Lg.w(TAG, "Change not emitted because new value equals current value: \"%s\"", value);
            return false;
        }
        this.value = checkNotNull(newValue);
        for(ValueChangeCallback<T> callbackFromViewChange : viewValueChangeCallbacks){
            callbackFromViewChange.callback(newValue);
        }
        return true;
    }

    public void addViewValueChangeCallback(ValueChangeCallback<T> viewValueChangeCallback) {
        this.viewValueChangeCallbacks.add(checkNotNull(viewValueChangeCallback));
    }

    public void addModelValueChangeCallback(ValueChangeCallback<T> modelValueChangeCallback) {
        this.modelValueChangeCallbacks.add(checkNotNull(modelValueChangeCallback));
    }

    public void updateValueFromModel(T value){
        for (ValueChangeCallback<T> callbackFromModelChange : modelValueChangeCallbacks) {
            callbackFromModelChange.callback(value);
        }
    }

    // Set the value from backend. This is used if both the model and the view need to be updated.
    public void setValue(@Nonnull T value) {
        Lg.v(TAG, "{%s}.setValue(%s)", toString(), value);
        if(!emitViewValueChanged(value)){
            return;
        }
        updateValueFromModel(value);
    }

    public T getValue() {
        return value;
    }

    final boolean isValueValid(T value) {
        return valueValueValidator.stream().allMatch(evaluator -> evaluator.isValid(value));
    }

    final boolean isStateValid() {
        return stateValidators.stream().allMatch(StateValidator::isValid);
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    protected static class Builder<T, B extends Builder> {

        protected T initial;
        private String description = null;
        private boolean callbackOnInitialization = false;
        private boolean emitIfNewValueEqualsOld = false;
        private final List<ValueValidator<T>> valueValidators = new ArrayList<>();
        private final List<StateValidator> stateValidators = new ArrayList<>();
        private final Set<ValueChangeCallback<T>> viewValueChangeCallback = new HashSet<>();

        public B initial(T initial) {
            this.initial = checkNotNull(initial);
            return (B) this;
        }

        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        public B addViewValueChangeCallback(ValueChangeCallback<T> viewValueChangeCallback) {
            this.viewValueChangeCallback.add(checkNotNull(viewValueChangeCallback));
            return (B) this;
        }

        public B callbackOnInitialization(boolean callbackOnInitialization){
            this.callbackOnInitialization = callbackOnInitialization;
            return (B) this;
        }

        public B emitIfNewValueEqualsOld(boolean emitIfNewValueEqualsOld){
            this.emitIfNewValueEqualsOld = emitIfNewValueEqualsOld;
            return (B) this;
        }

        public final B addValidityEvaluator(ValueValidator<T> valueValidator) {
            this.valueValidators.add(checkNotNull(valueValidator));
            return (B) this;
        }

        public final B addStateValidator(StateValidator validator) {
            stateValidators.add(checkNotNull(validator));
            return (B) this;
        }

        protected final void checkValue(T value, String name) {
            for(ValueValidator<T> ve : valueValidators) {
                checkArgument(ve.isValid(value), "'%s' value \"%s\" is invalid for %s", name, initial, this);
            }
        }

        public int viewValueChangeCallbackCount() {
            return viewValueChangeCallback.size();
        }

        public boolean isCallbackOnInitialization() {
            return callbackOnInitialization;
        }

        public int valueValidatorsCount(){
            return valueValidators.size();
        }

        public int stateValidatorsCount(){
            return stateValidators.size();
        }

        protected Builder() {
            // Prevent external instantiation
        }

    }

}
