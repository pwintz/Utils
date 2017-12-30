package paul.wintz.math;

import static java.lang.Math.*;

public class GaussianMaximum {
	private float centerFrequency;
	private float strength;
	private float focus;

	//CONSTRUCTOR, given three sequential points with the middle being the highest, the maximum is found.
	public GaussianMaximum (Vector2D leftPoint, Vector2D centerPoint, Vector2D rightPoint) {
		caclulateFrequencyStrengthAndFocus(leftPoint, centerPoint, rightPoint);
		//		    note, if the strength of all the points are equal, no max will be found.
	}

	//CONSTRUCTOR #2 USING KNOWN STRENGTH, FREQUENCY AND FOCUS
	public GaussianMaximum (float centerFrequencyIn, float strengthIn, float focusIn) {
		setCenterFrequency(centerFrequencyIn);
		setStrength(strengthIn);
		focus = focusIn;
	}

	//	//CONSTRUCTOR #3: FROM AN INDEX FOUND TO BE A MAXIMUM
	public GaussianMaximum(float[] amplitudes, float[] frequencies, int centerIndex)  {
		Vector2D leftPoint;
		Vector2D centerPoint;
		Vector2D rightPoint;

		//HANDLE BOUNDARY EXCEPTIONS
		if (centerIndex < amplitudes.length - 1 && centerIndex > 1) {
			leftPoint = new Vector2D( frequencies[centerIndex - 1], amplitudes[centerIndex - 1]);
			centerPoint = new Vector2D( frequencies[centerIndex], amplitudes[centerIndex]);
			rightPoint = new Vector2D( frequencies[centerIndex + 1], amplitudes[centerIndex + 1]);
		} else {
			leftPoint = new Vector2D( 0, 0);
			centerPoint = new Vector2D( frequencies[centerIndex], amplitudes[centerIndex]);
			rightPoint = new Vector2D( frequencies[0], 0);
		}

		caclulateFrequencyStrengthAndFocus(leftPoint, centerPoint, rightPoint);
	}

	public GaussianMaximum(float amplitudeLeft, float amplitudeCenter, float amplitudeRight,
			float frequencyLeft, float frequencyCenter, float frequencyRight)  {

		Vector2D leftPoint = new Vector2D( frequencyLeft, amplitudeLeft);
		Vector2D centerPoint = new Vector2D( frequencyCenter, amplitudeCenter);
		Vector2D rightPoint = new Vector2D( frequencyRight, amplitudeRight);

		caclulateFrequencyStrengthAndFocus(leftPoint, centerPoint, rightPoint);
	}

	private void caclulateFrequencyStrengthAndFocus(Vector2D leftPoint, Vector2D centerPoint, Vector2D rightPoint){
		double a = log(centerPoint.y()) - log(rightPoint.y()); //y2 - y3
		double b = pow(leftPoint.x(), 2) - pow(centerPoint.x(), 2);
		double c = log(leftPoint.y()) - log(centerPoint.y());
		double d = pow(centerPoint.x(), 2) - pow(rightPoint.x(), 2);

		setCenterFrequency((float)(( a*b - c*d )/( c*( 2*rightPoint.x() - 2*centerPoint.x() ) - a*( 2*centerPoint.x() - 2*leftPoint.x() ) )));

		double e = pow(rightPoint.x() - getCenterFrequency(), 2);
		double f = pow(centerPoint.x() - getCenterFrequency(), 2);

		setStrength((float) exp(( log(centerPoint.y()) * e - log(rightPoint.y()) * f) / ( e - f )));
		focus = (float)(( log(centerPoint.y()) - log(getStrength())) / pow(centerPoint.x() - getCenterFrequency(), 2));
	}


	static float gaussian(float x, float frequency, float strength, float focus) {
		return strength * (float) exp(focus * pow(x - frequency, 2));
	}

	public float value(double d) {
		return getStrength() * (float) exp(focus * pow(d - getCenterFrequency(), 2));
	}

	public float getCenterFrequency() {
		return centerFrequency;
	}

	public void setCenterFrequency(float centerFrequency) {
		this.centerFrequency = centerFrequency;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}


	public float getFocus() {
		return focus;
	}

	public void setFocus(float focus) {
		this.focus = focus;
	}
}
