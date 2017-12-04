package paul.wintz.uioptiontypes.events;

public class ImmediateEventOption extends EventOption {

	public ImmediateEventOption(String description, Event event) {
		super(description, event);
	}

	@Override
	public void triggerEvent() {
		event.doEvent();
	}


}