package paul.wintz.nodes;

class IntegerPlug extends Plug<Integer> {

    public IntegerPlug() {
        this(0);  //default value is 0
    }
    
    public IntegerPlug(int defaultValue) {
        super(Integer.class, defaultValue);
    }   
    
}