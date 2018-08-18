package paul.wintz.uioptiontypes.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventOption {

    @FunctionalInterface
    public interface Event {
        void doEvent();
    }

    @Nonnull private final Event event;
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

        private Event event;
        private EventQueue eventQueue;

        public Builder event(Event event) {
            this.event = checkNotNull(event);
            return this;
        }

        public Builder eventQueue(EventQueue eventQueue) {
            this.eventQueue = checkNotNull(eventQueue);
            return this;
        }

        public EventOption build() {
            checkNotNull(event);
            return new EventOption(this);
        }

        private Builder() {
            // Prevent external instantiation.
        }
    }


}