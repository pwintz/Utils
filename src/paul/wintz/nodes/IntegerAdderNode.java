package paul.wintz.nodes;

public class IntegerAdderNode extends Node<Integer> {

	private final Plug<Integer> in1;
	private final Plug<Integer> in2;
	
	public IntegerAdderNode() {
		super(Integer.class);
		in1 = new IntegerPlug();
		in2 = new IntegerPlug();
		addPlug("A", in1);
		addPlug("B", in2);
	}
	
	@Override
	public Integer getOutput() {
		return in1.getInput() + in2.getInput();
	}
	
}