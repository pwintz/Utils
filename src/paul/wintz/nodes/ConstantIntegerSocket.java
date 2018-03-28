package paul.wintz.nodes;

public final class ConstantIntegerSocket extends Socket<Integer> {
	private final int value;
	
	public ConstantIntegerSocket(int value) {
		super(Integer.class);
		this.value = value;
	}

	@Override
	public Integer getOutput() {
		return value;
	}

}