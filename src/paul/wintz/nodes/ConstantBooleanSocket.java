package paul.wintz.nodes;

public final class ConstantBooleanSocket extends Socket<Boolean> {
    private boolean value;
    
    public ConstantBooleanSocket() {
        this(false);
    }
    
    public ConstantBooleanSocket(boolean value) {
        super(Boolean.class);
        setValue(value);
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getOutput() {
        return value;
    }

}