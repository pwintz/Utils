package paul.wintz.uioptiontypes.events;

import paul.wintz.uioptiontypes.UserInputOption;

public abstract class EventOption extends UserInputOption<Void> {

	protected final Event event;

	public interface Event {
		public void doEvent();
	}

	public EventOption(String description, Event event) {
		super(description);
		this.event = event;
	}

	public abstract void triggerEvent();

}