package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableList;
import paul.wintz.utils.logging.Lg;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ValueOption<T> {
    @SuppressWarnings("unused") private static final String TAG = Lg.makeTAG(ValueOption.class);

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
    private final Set<ValueChangeCallback<T>> viewValueChangeCallback = new HashSet<>();
    private final ImmutableList<ValueValidator<T>> valueValueValidator;
    private final ImmutableList<StateValidator> stateValidators;

    protected ValueOption(Builder<T, ?> builder) {
        builder.checkValue(builder.initial, "initial");
        viewValueChangeCallback.addAll(builder.viewValueChangeCallback);
        builder.viewValueChangeCallback.clear();
        valueValueValidator = ImmutableList.copyOf(builder.valueValidators);
        stateValidators = ImmutableList.copyOf(builder.stateValidators);
        if(builder.callbackOnInitialization){
            emitViewValueChanged(builder.initial);
        }
        this.value = checkNotNull(builder.initial);
    }

    /**
     * Notify the callback that the value of this option changed in the view.
     * @param newValue the value that the view was changed to.
     * @return true if the change is legal, false otherwise. If the newValue equals the old value,
     * and the change is otherwise legal, then true is returned.
     */
    public boolean emitViewValueChanged(@Nonnull T newValue) {
        boolean changeAllowed = isStateValid() && isValueValid(newValue);
        if (changeAllowed && !newValue.equals(getValue())) {
            this.value = newValue;
            for(ValueChangeCallback<T> callback : viewValueChangeCallback){
                callback.callback(newValue);
            }
        }
        return changeAllowed;
    }

    public void addViewValueChangeCallback(ValueChangeCallback<T> viewValueChangeCallback) {
        this.viewValueChangeCallback.add(checkNotNull(viewValueChangeCallback));
    }

    public T getValue() {
        return value;
    }

    final boolean isValueValid(T value) {
        return valueValueValidator.stream().allMatch(evaluator -> evaluator.isValid(value));
    }

    boolean isStateValid() {
        return stateValidators.stream().allMatch(StateValidator::isValid);
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    protected static class Builder<T, B extends Builder> {

        protected T initial;
        private boolean callbackOnInitialization = false;
        private final List<ValueValidator<T>> valueValidators = new ArrayList<>();
        private final List<StateValidator> stateValidators = new ArrayList<>();
        private final Set<ValueChangeCallback<T>> viewValueChangeCallback = new HashSet<>();

        public B initial(T initial) {
            this.initial = checkNotNull(initial);
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
                checkArgument(ve.isValid(value), "%s value %s is invalid for %s", name, initial, this);
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
