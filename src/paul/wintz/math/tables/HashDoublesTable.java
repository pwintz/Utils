package paul.wintz.math.tables;

import static com.google.common.base.Preconditions.*;

import java.util.*;

import javax.annotation.Nonnull;

import paul.wintz.math.tables.IndexBoundaryHandler.BoundaryHandling;
import paul.wintz.utils.logging.Lg;

public class HashDoublesTable {
	private static final String TAG = Lg.makeTAG(HashDoublesTable.class);

	private final int steps;
	private final Map<String, double[]> arrays = new HashMap<>();
	private final Set<String> isSorted = new HashSet<>();

	private final IndexBoundaryHandler indexBoundaryHandler = new IndexBoundaryHandler(BoundaryHandling.LOOP);

	public HashDoublesTable(final int steps) {
		this.steps = steps;
		indexBoundaryHandler.setSteps(steps);
	}

	public void addSortedArray(String key, double[] array){
		checkSortedArray(array);
		arrays.put(key, array);
		isSorted.add(key);
	}

	public void addUnsortedArray(@Nonnull String key, double[] array){
		checkArray(array);
		arrays.put(key, array);
	}

	double[] getArray(final String key){
		checkKey(key);
		return arrays.get(key);
	}

	public double interpolate(double fromValue, final String fromKey, final String toKey){
		checkIsSorted(fromKey);

		final double[] fromArray = getArray(fromKey);
		final double[] toArray = getArray(toKey);

		fromValue %= fromArray[fromArray.length - 1];

		final int index = getIndexBefore(fromValue, fromKey);

		// The indices of the two elements that are the closest above and
		// below the given length.
		final int before = checkIndex(index % fromArray.length);
		final int after = checkIndex((index + 1) % fromArray.length);

		final double portionOfWayBetween = (fromValue - fromArray[before]) / (fromArray[after] - fromArray[before]);

		final double beforeValue = toArray[before];
		final double afterValue = toArray[after];

		return beforeValue + portionOfWayBetween * (afterValue - beforeValue);

	}

	double get(String key, int index){
		checkKey(key);
		checkIndex(index);

		return arrays.get(key)[index];
	}

	int getIndexBefore(double value, String key){
		checkIsSorted(key);

		final int index = Arrays.binarySearch(getArray(key), value);

		// If index is greater than zero, then the exact value was found in the list
		// at the returned index. This will be very rare.
		if(index >= 0)
			return index;

		final int insertionPoint = -(index + 1);

		return indexBoundaryHandler.normalize(insertionPoint);
	}

	private int checkIndex(int index) {
		return checkElementIndex(index, steps);
	}

	private String checkIsSorted(String key) {
		checkArgument(isSorted.contains(key), "The array associated with key '%s' is not sorted", key);
		return key;
	}

	String checkKey(final String key) {
		checkArgument(arrays.containsKey(key), "The key '%s' is not in the hash table", key);
		return key;
	}

	private double[] checkArray(final double[] array) {
		checkArgument(array.length == steps, "The array was %s but this table requires a length of %s", array.length, steps);
		return array;
	}

	private double[] checkSortedArray(double[] array) {
		checkArray(array);
		checkArgument(isIncreasesMonotonically(array), "independent variable array is not sorted in ascending order");
		return array;
	}

	public int getSteps() {
		return steps;
	}

	private boolean isIncreasesMonotonically(double[] array) {
		double max = -Double.MAX_VALUE;
		for(Double d : array){

			if(d < max)
				return false;

			max = d;
		}

		return true;
	}

}
