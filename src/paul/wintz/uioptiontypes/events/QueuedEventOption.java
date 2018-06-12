package paul.wintz.uioptiontypes.events;

public class QueuedEventOption extends EventOption {

    public QueuedEventOption(Event event) {
        super(event);
    }

    @Override
    public void triggerEvent() {
        EventQueue.add(event);
    }

}