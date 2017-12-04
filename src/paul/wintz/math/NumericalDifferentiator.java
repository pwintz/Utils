package paul.wintz.math;

import paul.wintz.math.tables.IndexBoundaryHandler;
import paul.wintz.math.tables.IndexBoundaryHandler.BoundaryHandling;
import paul.wintz.utils.exceptions.UnhandledCaseException;

public final class NumericalDifferentiator {
	// See this page for details about numerical difference equations:
	// https://en.wikipedia.org/wiki/Finite_difference_coefficient

	enum DerivativeFormula {
		SIMPLE,
		SYMMETRIC_DIFFERENCE_QUOTIENT,
		FOURTH_ORDER
	}

	public static double calculateDerivative(final DifferentiableDoubleArray array, final int index) {
		return calculateDerivate(array, index, DerivativeFormula.FOURTH_ORDER);
	}

	private static double calculateDerivate(final DifferentiableDoubleArray array, final int index, DerivativeFormula derivativeFormula) {
		switch(derivativeFormula) {
		case SIMPLE:
			return finiteDifferenceSimple(array.get(index), array.get(index + 1), array.getDTime());
		case SYMMETRIC_DIFFERENCE_QUOTIENT:
			return finiteDifferenceSymetricDifferenceQuotient(array.get(index - 1), array.get(index + 1), array.getDTime());
		case FOURTH_ORDER:
			return finiteDifferenceFourthOrder(array.get(index-2), array.get(index-1), array.get(index+1), array.get(index+2), array.getDTime());
		default:
			throw new UnhandledCaseException(derivativeFormula);
		}
	}

	private static double finiteDifferenceSimple(final double yOfX, final double yOfXPlusH, final double h) {
		return (yOfXPlusH - yOfX) / h;
	}

	private static double finiteDifferenceSymetricDifferenceQuotient(final double yOfXMinusH, final double yOfXPlusH, final double h) {
		return (yOfXPlusH - yOfXMinusH) / (2*h);
	}

	private static double finiteDifferenceFourthOrder(final double yOfXMinus2H, final double yOfXMinusH, final double yOfXPlusH, final double yOfXPlus2H, final double h) {
		return (-yOfXPlus2H + 8 * yOfXPlusH - 8 * yOfXMinusH + yOfXMinus2H) / (12*h);
	}

	public static double calculateSecondDerivative(final DifferentiableDoubleArray array, final int index) {
		return finiteDifferenceSecondDerivative(array.get(index-2), array.get(index-1), array.get(index), array.get(index+1),array.get(index+2), array.getDTime());
	}

	private static double finiteDifferenceSecondDerivative(double yOfXMinus2H, double yOfXMinusH, double yOfX, double yOfXPlusH, double yOfXPlus2H, double h) {
		return ( -yOfXMinus2H + 8*yOfXMinusH - 30*yOfX + 8*yOfXPlusH + -yOfXPlus2H) / (12 * h * h);
	}

	public static class DifferentiableDoubleArray {

		private final IndexBoundaryHandler indexBoundaryHandler = new IndexBoundaryHandler();

		private final double[] array;
		private final double dTime;

		public DifferentiableDoubleArray(double[] array, double dTime, BoundaryHandling boundaryHandling) {
			this.array = array;
			this.dTime = dTime;
			indexBoundaryHandler.setSteps(array.length);
			indexBoundaryHandler.setBoundaryHandling(boundaryHandling);
		}

		public double get(int index) {
			return array[indexBoundaryHandler.normalize(index)];
		}

		public double getDTime() {
			return dTime;
		}
	}

	private NumericalDifferentiator() {
		// Do not instantiate
	}

}
