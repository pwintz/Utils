package paul.wintz.utils;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.lang.Math.round;

public class SequenceUtils {

    public static int indexOfLastTrueElement(List<Boolean> booleanList) {
        checkArgument(!booleanList.isEmpty());
        for (int i = booleanList.size() - 1; i >= 0; i--) {
            if (booleanList.get(i))
                return i;
        }
        return -1;
    }

    public static boolean doLengthsMatch(Object... arrays){
        final int length = SequenceUtils.length(arrays[0]);

        for(final Object a : arrays){
            if(length != SequenceUtils.length(a)) return false;
        }

        return true;
    }

    public static double[] toDoubleArray(String[] strings) {
        final double[] doubles = new double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            doubles[i] = Double.valueOf(strings[i].trim());
        }
        return doubles;
    }

    public static int[] toIntArray(String[] strings) {
        try {
            final int[] ints = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                ints[i] = Integer.valueOf(strings[i].trim());
            }
            return ints;
        } catch (NumberFormatException e) {
            double[] doubles = toDoubleArray(strings);
            int[] ints = new int[strings.length];
            for (int i = 0; i < doubles.length; i++) {
                double d = doubles[i];
                if (abs(d - round(d)) > 0.0001) {
                    NumberFormatException numberFormatException = new NumberFormatException("Number wasn't a integer:" + d);
                    numberFormatException.initCause(e);
                    throw numberFormatException;
                }
                ints[i] = (int) round(d);
            }
            return ints;
        }

    }

    public static boolean[] toBooleanArray(String[] strings) {
        final boolean[] booleans = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            booleans[i] = Boolean.valueOf(strings[i].trim());
        }
        return booleans;
    }

    private static int length(Object array){
        return java.lang.reflect.Array.getLength(array);
    }
}
