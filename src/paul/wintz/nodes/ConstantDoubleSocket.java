package paul.wintz.nodes;

public final class ConstantDoubleSocket extends Socket<Double> {
    private Double value;
    
    public ConstantDoubleSocket() {
        this(0);
    }
    
    public ConstantDoubleSocket(double value) {
        super(Double.class);
        setValue(value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public Double getOutput() {
        return value;
    }

}