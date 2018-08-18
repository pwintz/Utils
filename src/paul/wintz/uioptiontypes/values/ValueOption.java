package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableList;
import paul.wintz.utils.logging.Lg;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ValueOption<T> {
    private static final String TAG = Lg.makeTAG(ValueOption.class);

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

    public T value;
    public final ValueChangeCallback<T> viewValueChangeCallback;
    //public final ValueChangeCallback<T> modelValueChangeCallback;
    private final ImmutableList<ValueValidator<T>> valueValueValidator;
    private final ImmutableList<StateValidator> stateValidators;

    public T getValue() {
        return value;
    }

    /**
     * Notify the callback that the value of this option changed in the view.
     * @param newValue the value that the view was changed to.
     * @return true if the change is legal, false otherwise. If the newValue equals the old value,
     * and the change is otherwise legal, then true is returned.
     */
    public boolean emitViewValueChanged(T newValue) {
        boolean changeAllowed = isStateValid() && isValueValid(newValue);
        Lg.v(TAG, "emitViewValueChange(%s), isChangeAllowed? %b", newValue, changeAllowed);
        if (changeAllowed && !newValue.equals(value)) {
            value = newValue;
            viewValueChangeCallback.callback(newValue);
        }
        return changeAllowed;
    }

    public final boolean isValueValid(T value) {
        return valueValueValidator.stream().allMatch(evaluator -> evaluator.isValid(value));
    }

    public boolean isStateValid() {
        return stateValidators.stream().allMatch(StateValidator::isValid);
    }

    protected ValueOption(Builder<T, ?> builder) {
        builder.checkValue(builder.initial, "initial");
        value = checkNotNull(builder.initial);
        viewValueChangeCallback = checkNotNull(builder.viewValueChangeCallback);
        //modelValueChangeCallback = checkNotNull(builder.modelValueChangeCallback);
        valueValueValidator = ImmutableList.copyOf(builder.valueValidators);
        stateValidators = ImmutableList.copyOf(builder.stateValidators);
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    protected static class Builder<T, B extends Builder> {

        protected T initial;
        private final ValueChangeCallback<T> NULL_VALUE_CHANGE_CALLBACK = (pass) -> {};
        private final List<ValueValidator<T>> valueValidators = new ArrayList<>();
        private final List<StateValidator> stateValidators = new ArrayList<>();
        private ValueChangeCallback<T> viewValueChangeCallback = NULL_VALUE_CHANGE_CALLBACK;
        //private ValueChangeCallback<T> modelValueChangeCallback = NULL_VALUE_CHANGE_CALLBACK;

        public B initial(T initial) {
            this.initial = checkNotNull(initial);
            return (B) this;
        }

        public final B viewValueChangeCallback(ValueChangeCallback<T> viewValueChangeCallback) {
            checkState(this.viewValueChangeCallback.equals(NULL_VALUE_CHANGE_CALLBACK), "viewValueChangeCallback was set twice");
            this.viewValueChangeCallback = checkNotNull(viewValueChangeCallback);
            return (B) this;
        }

//        public B modelValueChangeCallback(ValueChangeCallback<T> modelValueChangeCallback) {
//            checkState(this.modelValueChangeCallback.equals(NULL_VALUE_CHANGE_CALLBACK), "modelValueChangeCallback was set twice");
//            this.modelValueChangeCallback = checkNotNull(modelValueChangeCallback);
//            return (B) this;
//        }

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
                checkArgument(ve.isValid(value), "%s value %s is invalid", name, initial);
            }
        }

        protected Builder() {
            // Prevent instantiation
        }

    }

}
