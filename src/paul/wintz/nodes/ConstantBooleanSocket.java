package paul.wintz.nodes;

public final class ConstantBooleanSocket extends Socket<Boolean> {
	private final boolean value;
	
	public ConstantBooleanSocket(boolean value) {
		super(Boolean.class);
		this.value = value;
	}

	@Override
	public Boolean getOutput() {
		return value;
	}

}