package paul.wintz.uioptiontypes.values;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A container for storing options in ValueOptionsBundle.
 * @param <T> the type of object chosen in the option.
 */
class OptionItem<T> {
    private ValueOption<T> option = null; // is null until connected to an option.
    final Class<T> valueType;
    final T defaultValue;

    OptionItem(ValueOption<T> option, Class<T> valueType, T defaultValue) {
        this.option = checkNotNull(option);
        this.valueType = checkNotNull(valueType);
        this.defaultValue = defaultValue;
    }

    void setValueToDefault() {
        option.setValue(defaultValue);
    }

    public ValueOption<T> getOption() {
        return option;
    }

    public void setOption(ValueOption<T> option) {
        this.option = option;
    }
}
