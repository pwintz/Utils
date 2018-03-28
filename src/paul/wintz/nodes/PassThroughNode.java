package paul.wintz.nodes;

class PassThroughNode extends Node<Integer> {

	private final Plug<Integer> in;
	
	public PassThroughNode() {
		super(Integer.class);
		in = new IntegerPlug();
		addPlug("in", in);
	}
	
	@Override
	public Integer getOutput() {
		return in.getInput();
	}
	
}