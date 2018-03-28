package paul.wintz.nodes;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

public class AdderNodeTest {

	IntegerAdderNode finalAdder = new IntegerAdderNode();
	IntegerAdderNode inputAdder1 = new IntegerAdderNode();
	IntegerAdderNode inputAdder2 = new IntegerAdderNode();

	@Test
	public void test() {
		finalAdder.getPlug("A").plugin(inputAdder1);
		finalAdder.getPlug("B").plugin(inputAdder2);
		
		inputAdder1.getPlug("A").plugin(new ConstantIntegerSocket(1));
		inputAdder1.getPlug("B").plugin(new ConstantIntegerSocket(2));
		inputAdder2.getPlug("A").plugin(new ConstantIntegerSocket(3));
		inputAdder2.getPlug("B").plugin(new ConstantIntegerSocket(5));
		

		assertThat(finalAdder.getOutput(), is(equalTo(11)));
		
	}

}
