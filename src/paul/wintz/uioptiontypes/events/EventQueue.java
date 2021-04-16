package paul.wintz.uioptiontypes.events;

import java.util.*;

public class EventQueue {

    private final Queue<Event> queue = new LinkedList<>();

    public void add(Event event) {
        queue.add(event);
    }

    public void callEvents() {
        Event event;
        while ((event = queue.poll()) != null) {
            event.doEvent();
        }
    }

    public int size(){
        return queue.size();
    }

}
