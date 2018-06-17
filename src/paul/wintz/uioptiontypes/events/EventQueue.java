package paul.wintz.uioptiontypes.events;

import java.util.*;

import paul.wintz.uioptiontypes.events.EventOption.Event;

public class EventQueue {

    private final Queue<Event> queue = new LinkedList<>();

    public void add(Event event) {
        queue.add(event);
    }

    public void handleEvents() {
        Event event;
        while ((event = queue.poll()) != null) {
            event.doEvent();
        }
    }

}
