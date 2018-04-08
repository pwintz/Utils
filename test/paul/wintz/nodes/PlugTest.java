package paul.wintz.nodes;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlugTest {

	IPlug<Integer> intPlug = new IntegerPlug();
	IPlug<Object> objectPlug = new Plug<>(Object.class);
	ISocket<Integer> intSocket = new ConstantIntegerSocket(17);
	ISocket<Boolean> boolSocket = new ConstantBooleanSocket(true);

	@Test
	public void startsUnplugged() {
		assertFalse(intPlug.isPlugged());
	}

	@Test
	public void canPluginToASocketOfMatchingType() {
		assertTrue(intPlug.plugin(intSocket));
		assertTrue(intPlug.isPlugged());
	}

	@Test
	public void cannotPluginToASocketOfMismatchedType() {
		assertFalse(intPlug.plugin(boolSocket));
		assertFalse(intPlug.isPlugged());
	}

	@Test
	public void canUnplug() {
		intPlug.plugin(intSocket);

		intPlug.unplug();

		assertFalse(intPlug.isPlugged());
	}

	@Test
	public void pluggingInToSecondSocketOverridesFirst() {
		 objectPlug.plugin(intSocket);
		 objectPlug.plugin(boolSocket);

		 assertThat(objectPlug.getInput(), is(equalTo(true)));
	}

	@Test
	public void canQueryInputType() {
		assertThat(intPlug.getInputType(), is(equalTo(Integer.class)));
	}

	@Test
	public void getInputReturnsValueOfSocket() {
		intPlug.plugin(new ConstantIntegerSocket(13));

		assertThat(intPlug.getInput(), is(equalTo(13)));
	}

	@Test
	public void getInputDefaultsToNullIfUnplugged() {
		assertThat(objectPlug.getInput(), is(equalTo(null)));
	}

	@Test
	public void getInputDefaultsToObjectPassedToConstructorIfUnplugged() {
		IPlug<Object> plug = new Plug<>(Object.class, "default");

		assertThat(plug.getInput(), is(equalTo("default")));
	}

	@Test
	public void canPlugIntoSocketThatProvidesSubclassType() {
		assertTrue(objectPlug.plugin(new ConstantIntegerSocket(17)));
		assertThat(objectPlug.getInput(), is(equalTo(17)));
	}

}
