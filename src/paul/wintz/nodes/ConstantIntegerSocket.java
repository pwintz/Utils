package paul.wintz.nodes;

public final class ConstantIntegerSocket extends Socket<Integer> {
    private int value;
    
    public ConstantIntegerSocket() {
        this(0);
    }
    
    public ConstantIntegerSocket(int value) {
        super(Integer.class);
        setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public Integer getOutput() {
        return value;
    }

}