package paul.wintz.uioptiontypes.events;

import java.util.*;

import paul.wintz.uioptiontypes.events.EventOption.Event;

public class EventQueue {

    private static final Queue<Event> queue = new LinkedList<>();

    public static void add(Event event) {
        queue.add(event);
    }

    public static void handleEvents() {

        Event event;
        while ((event = queue.poll()) != null) {
            event.doEvent();
        }
    }

    private EventQueue() {
        // Do not instantiate
    }

}
