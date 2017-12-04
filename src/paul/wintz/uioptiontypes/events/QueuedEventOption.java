package paul.wintz.uioptiontypes.events;

public class QueuedEventOption extends EventOption {

	public QueuedEventOption(String description, Event event) {
		super(description, event);
	}

	@Override
	public void triggerEvent() {
		EventQueue.add(event);
	}

}