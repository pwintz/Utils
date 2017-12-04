package paul.wintz.math.tables;

import static paul.wintz.utils.Utils.*;

public class IndexBoundaryHandler {

	public enum BoundaryHandling {
		LOOP,
		CONSTANT,
		//REFLECT,
		ERROR
	}

	private IndexBoundaryHandler.BoundaryHandling boundaryHandling;
	private int steps;

	public IndexBoundaryHandler() {

	}

	public IndexBoundaryHandler(BoundaryHandling boundaryHandling) {
		setBoundaryHandling(boundaryHandling);
	}

	public IndexBoundaryHandler(BoundaryHandling boundaryHandling, int steps) {
		setBoundaryHandling(boundaryHandling);
		setSteps(steps);
	}

	public void setSteps(int steps) {
		this.steps = checkPositive(steps);
	}

	public void setBoundaryHandling(IndexBoundaryHandler.BoundaryHandling boundaryHandling) {
		this.boundaryHandling = boundaryHandling;
	}

	public int normalize(final int i) {
		switch(boundaryHandling) {
		case CONSTANT:
			if (i < 0)
				return 0;
			else if(i >= steps)
				return steps - 1;
			else
				return i;
		case LOOP:
			return modulus(i, steps);
		case ERROR:
			throw new ArrayIndexOutOfBoundsException(i);
		default:
			throw new RuntimeException("Case not implemented: " + boundaryHandling);
		}
	}
}