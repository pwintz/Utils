package paul.wintz.uioptiontypes.events;

import paul.wintz.uioptiontypes.UserInputOption;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventOption extends UserInputOption {

    public interface Event {
        void doEvent();
    }

    protected final Event event;
    @Nullable private final EventQueue eventQueue;

    public void triggerEvent() {
        if(eventQueue == null) {
            event.doEvent();
        } else {
            eventQueue.add(event);
        }
    }

    private EventOption(Builder builder) {
        this.event = builder.event;
        this.eventQueue = builder.eventQueue;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        public Event event;
        public EventQueue eventQueue;

        public Builder event(Event event) {
            this.event = checkNotNull(event);
            return this;
        }

        public Builder eventQueue(EventQueue eventQueue) {
            this.eventQueue = checkNotNull(eventQueue);
            return this;
        }

        public EventOption build() {
            return new EventOption(this);
        }
    }


}