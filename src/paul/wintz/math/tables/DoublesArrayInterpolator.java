package paul.wintz.math.tables;

public class DoublesArrayInterpolator {

    public enum EdgeBehavior {
        WRAP,
        EXTEND_CONSTANT,
        EXTEND_LINEAR,
        //      REFLECT,
        ERROR
    }

    private final int length;
    private final double xStart;
    private final double xEnd;
    private final double period;

    private final double[] yArray;

    private EdgeBehavior edgeBehavior = EdgeBehavior.ERROR;

    public DoublesArrayInterpolator(final double xStart, final double xEnd, final double[] yArray){

        this.yArray = yArray;
        this.xStart = xStart;
        this.xEnd = xEnd;
        period = xEnd - xStart;
        length = yArray.length;

    }

    public double interpolate(double x){

        // The indices of the two elements that are the closest above and
        // below the given length.
        final int before = getIndexBefore(x);
        final int after = before + 1;

        final double distFromBefore = x - getX(before);

        final double yBefore = getValueAtIndex(before);
        final double yAfter = getValueAtIndex(after);

        final double slope = (yAfter - yBefore) / getDeltaTime();

        return yBefore + distFromBefore * slope;

    }

    int getIndexBefore(double x){

        return (int) Math.floor(length * (x - xStart) / period);

    }

    double getX(int index){
        return xStart + index * getDeltaTime();
    }

    double getDeltaTime(){
        return (xEnd - xStart) / length;
    }


    double getValueAtIndex(int index){

        if(0 <= index && index < length) return yArray[index];
        else if(index < 0){
            switch(edgeBehavior){
            case ERROR:
                throw new IndexOutOfBoundsException("The index " + index + " is too small.");

            case EXTEND_CONSTANT:
                return yArray[0];

            case EXTEND_LINEAR:
                final double slope = yArray[1] - yArray[0];
                return yArray[0] + slope * index;

            case WRAP:
                return yArray[wrapIndex(index)];

            default:
                throw new RuntimeException("Not implemented: " + edgeBehavior);
            }
        } else if(index >= length){

            final int last = yArray.length - 1;
            switch(edgeBehavior){
            case ERROR:
                throw new IndexOutOfBoundsException("The index " + index + " is too large.");

            case EXTEND_CONSTANT:
                return yArray[last];

            case EXTEND_LINEAR:
                final double slope = yArray[last] - yArray[last - 1];
                return yArray[last] + slope * (index - last);

            case WRAP:
                return yArray[wrapIndex(index)];

            default:
                throw new RuntimeException("Not implemented: " + edgeBehavior);
            }
        }

        throw new RuntimeException();
    }

    private int wrapIndex(int index){
        return positiveModulus(index, length);
    }

    static int positiveModulus(int a, int b) {
        if(b < 0) throw new IllegalArgumentException("b must be positive");

        return Math.floorMod(a, b);
    }


    public final EdgeBehavior getEdgeBehavior() {
        return edgeBehavior;
    }


    public final void setEdgeBehavior(EdgeBehavior edgeBehavior) {
        this.edgeBehavior = edgeBehavior;
    }

}
