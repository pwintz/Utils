package paul.wintz.nodes;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

public class AdderNodeTest {

	IntegerAdderNode finalAdder = new IntegerAdderNode();
	IntegerAdderNode inputAdder1 = new IntegerAdderNode();
	IntegerAdderNode inputAdder2 = new IntegerAdderNode();

	@Test
	public void test() {
		finalAdder.attemptPlugin("A", inputAdder1);
		finalAdder.attemptPlugin("B", inputAdder2);
		
		inputAdder1.attemptPlugin("A", new ConstantIntegerSocket(1));
		inputAdder1.attemptPlugin("B", new ConstantIntegerSocket(2));
		inputAdder2.attemptPlugin("A", new ConstantIntegerSocket(3));
		inputAdder2.attemptPlugin("B", new ConstantIntegerSocket(5));
		
		assertThat(finalAdder.getOutput(), is(equalTo(11)));
		
	}

}
