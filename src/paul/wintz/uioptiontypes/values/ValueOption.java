package paul.wintz.uioptiontypes.values;

import com.google.common.collect.ImmutableList;
import paul.wintz.uioptiontypes.OptionItem;
import paul.wintz.utils.logging.Lg;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ValueOption<T> implements OptionItem {
    private static String TAG = Lg.makeTAG(ValueOption.class);

    public interface ValueChangeCallback<T> {
        void callback(T value);
    }

    public interface ValidityEvaluator<T> {
        boolean isValid(T value);
    }

    public interface StateValidator {
        boolean isValid();
    }

    public final T initial;
    public final ValueChangeCallback<T> viewValueChangeCallback;
    public final ValueChangeCallback<T> modelValueChangeCallback;
    private final ImmutableList<ValidityEvaluator<T>> validityEvaluator;
    private final ImmutableList<StateValidator> stateValidators;

    public boolean emitViewValueChanged(T newValue) {
        boolean changeAllowed = isStateValid() && isValidValue(newValue);
        Lg.v(TAG, "emitViewValueChange(%s), isChangeAllowed? %b", newValue, changeAllowed);
        if(changeAllowed) {
            viewValueChangeCallback.callback(newValue);
        }
        return changeAllowed;
    }

    public final boolean isValidValue(T value) {
        return validityEvaluator.stream().allMatch(evaluator -> evaluator.isValid(value));
    }

    private boolean isStateValid() {
        return stateValidators.stream().allMatch(StateValidator::isValid);
    }

    protected ValueOption(Builder<T, ?> builder) {
        initial = checkNotNull(builder.initial);
        viewValueChangeCallback = checkNotNull(builder.viewValueChangeCallback);
        modelValueChangeCallback = checkNotNull(builder.modelValueChangeCallback);
        validityEvaluator = ImmutableList.copyOf(builder.validityEvaluators);
        stateValidators = ImmutableList.copyOf(builder.stateValidators);
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public static class Builder<T, B extends Builder> {

        protected T initial;
        private final ValueChangeCallback<T> nullValueChangeCallback = (pass) -> {};
        protected ValueChangeCallback<T> viewValueChangeCallback = nullValueChangeCallback;
        protected ValueChangeCallback<T> modelValueChangeCallback = nullValueChangeCallback;
        protected List<ValidityEvaluator<T>> validityEvaluators = new ArrayList<>();
        protected List<StateValidator> stateValidators = new ArrayList<>();

        public B initial(T initial) {
            this.initial = initial;
            return (B) this;
        }

        public final B viewValueChangeCallback(ValueChangeCallback<T> viewValueChangeCallback) {
            checkState(this.viewValueChangeCallback.equals(nullValueChangeCallback), "viewValueChangeCallback was set twice");
            this.viewValueChangeCallback = checkNotNull(viewValueChangeCallback);
            return (B) this;
        }

        public B modelValueChangeCallback(ValueChangeCallback<T> modelValueChangeCallback) {
            checkState(this.modelValueChangeCallback.equals(nullValueChangeCallback), "modelValueChangeCallback was set twice");
            this.modelValueChangeCallback = checkNotNull(modelValueChangeCallback);
            return (B) this;
        }

        public final B addValidityEvaluator(BooleanOption.ValidityEvaluator<T> validityEvaluator) {
            this.validityEvaluators.add(checkNotNull(validityEvaluator));
            return (B) this;
        }

        public B addStateValidator(StateValidator validator) {
            stateValidators.add(validator);
            return (B) this;
        }

        protected void checkValues() {
            for(ValidityEvaluator<T> ve : validityEvaluators) {
                checkState(ve.isValid(initial), "initial value %s is invalid", initial);
            }
        }

        protected Builder() {
            // Prevent instantiation
        }

    }
}
