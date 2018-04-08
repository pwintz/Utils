package paul.wintz.uioptiontypes;

public class BooleanOption extends UserInputOption<Boolean> {

    public BooleanOption(boolean initialValue, String description) {
        super(description);
        setValue(initialValue);
    }

}
