package paul.wintz.nodes;

class PassThroughIntegerNode extends Node<Integer> {
	public static final String PLUG_NAME = "in";
	private final Plug<Integer> in;
	
	public PassThroughIntegerNode() {
		super(Integer.class);
		in = new IntegerPlug();
		addPlug(PLUG_NAME, in);
	}
	
	@Override
	public Integer getOutput() {
		return in.getInput();
	}
	
}