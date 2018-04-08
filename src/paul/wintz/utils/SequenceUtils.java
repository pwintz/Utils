package paul.wintz.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

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

	public static void assertLengthsMatch(Object... arrays){
		checkArgument(doLengthsMatch(arrays), "The length of the arrays do not match");
	}

	public static int[] toIntArray(String[] strings) {
		final int[] ints = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			ints[i] = Integer.valueOf(strings[i].trim());
		}
		return ints;
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
