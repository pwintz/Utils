package paul.wintz.functionevaluator;

import net.objecthunter.exp4j.function.Function;
import paul.wintz.utils.Utils;

import static java.lang.Math.*;

@SuppressWarnings("WeakerAccess")
public class ExtraFunctions {

    public static final Function NORMALIZED_COSINE = new Function("ncos") {
        @Override
        public double apply(double... args) {
            return 0.5 * Math.cos(2 * Math.PI * args[0]) + 0.5;
        }
    };

    public static final Function NORMALIZED_SINE = new Function("nsin") {
        @Override
        public double apply(double... args) {
            return 0.5 * Math.sin(2 * Math.PI * args[0]) + 0.5;
        }
    };

    public static final Function ROUND = new Function("round") {
        @Override
        public double apply(double... args) {
            if(Double.isNaN(args[0])) {
                return Double.NaN;
            }
            return round(args[0]);
        }
    };

    public static final Function FLOOR = new Function("floor") {
        @Override
        public double apply(double... args) {
            if(Double.isNaN(args[0])) {
                return Double.NaN;
            }
            return floor(args[0]);
        }
    };

    public static final Function CEIL = new Function("ceil") {
        @Override
        public double apply(double... args) {
            if(Double.isNaN(args[0])) {
                return Double.NaN;
            }
            return ceil(args[0]);
        }
    };

    public static final Function MIN = new Function("min", 2) {
        @Override
        public double apply(double... args) {
            return min(args[0], args[1]);
        }
    };

    public static final Function MAX = new Function("max", 2) {
        @Override
        public double apply(double... args) {
            return max(args[0], args[1]);
        }
    };

    /**
     * A function that that increases in value 'steps' times in [0, 1). If there is one
     * step, it occurs at 0.5. If two, then they are at 1/3, 2/3.
     * The value in [0.0, <first step>) is zero, and from [<last step, 1.0) is 1.0.
     * The increase of value at each step equals (1.0 / steps).
     */
    public static final Function STEPS = new Function("steps", 2) {
        @Override
        public double apply(double... args) {
            double x = args[0];
            double steps = Utils.checkPositive(args[1]);

            double stepNumber = Math.floor((steps + 1) * x);
            return stepNumber / steps;
        }
    };

    /**
     * A function that oscillates between 0 and 1. The number of periods between 0.0 and 1.0 is determined
     * by the second argument, which we will call 'frequency'. If frequency=1.0, then the value will be zero in [0.0, 0.5) and
     * 1.0 in [0.5, 1.0).
     **/
    public static final Function SQUARE_WAVE = new Function("square") {
        @Override
        public double apply(double... args) {
            double x = args[0];
            double frequency = 1.0; //args[1];

            // If frequency is negative, we invert the wave. This is effectively the same as
            // reversing the x-axis. The
            if(frequency < 0.0) {
                x = -x;
                frequency = -frequency;
            }

            // The modulus operator (%) does not play well with negative numbers, for our
            // uses, so if the number is negative, we add a value to it into [0.0, 1.0).
            if(x < 0){
                x -= Math.floor(x);
            }

            double period = 1.0 / frequency;
            if(x % period < period / 2.0) {
                return 0.0;
            } else {
                return 1.0;
            }
        }
    };

}
