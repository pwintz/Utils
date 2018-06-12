package paul.wintz.uioptiontypes.events;

import paul.wintz.uioptiontypes.UserInputOption;

public class EventOption extends UserInputOption {

    public interface Event {
        void doEvent();
    }

    protected final Event event;

    public void triggerEvent() {
        event.doEvent();
    }

    public EventOption(Event event) {
        this.event = event;
    }


}