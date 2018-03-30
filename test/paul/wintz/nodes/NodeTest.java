package paul.wintz.nodes;

import static org.hamcrest.Matchers.*;
import static paul.wintz.nodes.PassThroughIntegerNode.PLUG_NAME;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

	Node<Integer> passThroughNode = new PassThroughIntegerNode();
	ConstantIntegerSocket intSocket = new ConstantIntegerSocket();
	PlugAddableNode plugAddableNode = new PlugAddableNode();
	
	private static class PlugAddableNode extends Node<String> {
		public PlugAddableNode() {
			super(String.class);
		}

		@Override
		public void addPlug(String name, Plug<?> plug) {
			super.addPlug(name, plug);
		}

		@Override
		public String getOutput() {
			return toString();
		}
	}

	@Test
	public void getPlugReturnsEmptyIfPlugDoesntExist() {
		assertFalse(passThroughNode.getPlug("not a plug").isPresent());
	}

	@Test
	public void getPlugReturnsPresentOptionIfPlugExist() {
		assertTrue(passThroughNode.getPlug(PLUG_NAME).isPresent());
	}

	@Test
	public void canAttachPlugToSocket() {
		intSocket.setValue(6);
		passThroughNode.attemptPlugin(PLUG_NAME, intSocket);
		assertThat(passThroughNode.getOutput(), is(equalTo(6)));
	}
	
	@Test(expected = NullPointerException.class)
	public void cannotAttachNullSocket() {
		 passThroughNode.attemptPlugin("not a plug", null);
	}
	
	@Test
	public void getPlugNamesReturnsAllAddedPlugs() {
		 plugAddableNode.addPlug("Plug 1", new IntegerPlug());
		 plugAddableNode.addPlug("Plug 2", new IntegerPlug());
		 
		 assertThat(plugAddableNode.getPlugNames(), contains("Plug 1", "Plug 2"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwsIfSameNameIsAddedTwice() {
		 plugAddableNode.addPlug("Plug 1", new IntegerPlug());
		 plugAddableNode.addPlug("Plug 1", new IntegerPlug());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwsIfSamePlugIsAddedTwice() {
		 IntegerPlug intPlug = new IntegerPlug();

		 plugAddableNode.addPlug("Plug 1", intPlug);
		 plugAddableNode.addPlug("Plug 2", intPlug);
	}
	
	@Test(expected = NullPointerException.class)
	public void throwsIfNullNameIsAdded() {
		 plugAddableNode.addPlug(null, new IntegerPlug());
	}
	
	@Test(expected = NullPointerException.class)
	public void throwsIfNullPlugIsAdded() {
		 plugAddableNode.addPlug("Plug 1", null);
	}
	
}
