package paul.wintz.uioptiontypes.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventsTest {

    EventOption.Builder builder = EventOption.builder();
    EventQueue eventQueue = new EventQueue();
    @Mock Event event;

    @Test(expected = NullPointerException.class)
    public void throwsIfEventIsNull() {
        builder.build();
    }

    @Test
    public void triggerEventImmediatelyIfNoQueue() {
        EventOption option = builder.event(event).build();

        option.triggerEvent();
        verify(event).doEvent();

        Mockito.reset(event);

        eventQueue.callEvents();
        verify(event, Mockito.never()).doEvent();
    }

    @Test
    public void triggerEventWaitsForQueue() {
        EventOption option = builder
                .event(event)
                .eventQueue(eventQueue)
                .build();

        option.triggerEvent();
        verify(event, Mockito.never()).doEvent();

        eventQueue.callEvents();
        verify(event).doEvent();
    }

    @Test
    public void allQueuedEventsAreExecuted() {
        builder.eventQueue(eventQueue).event(event).build()
                .triggerEvent();
        builder.eventQueue(eventQueue).event(event).build()
                .triggerEvent();

        eventQueue.callEvents();

        verify(event, times(2)).doEvent();
    }

}