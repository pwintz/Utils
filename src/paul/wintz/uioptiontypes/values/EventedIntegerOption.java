package paul.wintz.uioptiontypes.values;

import paul.wintz.uioptiontypes.events.EventQueue;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventedIntegerOption extends IntegerOption {

    private final EventQueue eventQueue;

    private EventedIntegerOption(Builder builder) {
        super(builder);
        eventQueue = builder.eventQueue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends IntegerOption.Builder {

        private EventQueue eventQueue;

        public EventedIntegerOption build() {
            checkNotNull(eventQueue, "eventQueue must be set.");
            return new EventedIntegerOption(this);
        }

        public Builder eventQueue(EventQueue eventQueue) {
            this.eventQueue = checkNotNull(eventQueue);
            return this;
        }

        public Builder addViewValueChangeCallback(ValueChangeCallback<Integer> viewValueChangeCallback) {
            super.addViewValueChangeCallback(value -> {
                // When the view value changes, instead of immediately calling viewValueChangeCallback,
                // we add an event to the queue that is called when the event queue is processed.
                eventQueue.add(() -> viewValueChangeCallback.callback(value));
            });
            return this;
        }

        private Builder() {
            super();
        }

    }
}
