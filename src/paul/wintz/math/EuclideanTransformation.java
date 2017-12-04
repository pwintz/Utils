package paul.wintz.math;

public class EuclideanTransformation {

	// The transforms are in the order:
	// (1) Counter-clockwise Rotation about the origin.
	// (2) Translation

	public final double rotation;
	public final double x;
	public final double y;

	public EuclideanTransformation(double rotation, double translationX, double translationY) {
		this.rotation = rotation;
		this.x = translationX;
		this.y = translationY;
	}

	public EuclideanTransformation(double rotation, Vector2D translation) {
		this.rotation = rotation;
		x = translation.x();
		y = translation.y();
	}

}
