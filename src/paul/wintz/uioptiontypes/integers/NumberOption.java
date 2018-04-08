package paul.wintz.uioptiontypes.integers;

import static com.google.common.base.Preconditions.checkNotNull;

import paul.wintz.uioptiontypes.UserInputOption;

public abstract class NumberOption<T extends Number> extends UserInputOption<T> {

    protected final T min;
    protected final T max;

    public NumberOption(String description, T min, T max) {
        super(description);
        this.min = checkNotNull(min);
        this.max = checkNotNull(max);
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

}