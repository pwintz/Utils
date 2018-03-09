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
	
}
