package paul.wintz.uioptiontypes.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventsTest {

    EventOption.Builder builder = EventOption.builder();
    EventQueue eventQueue = new EventQueue();
    @Mock EventOption.Event event;

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

        eventQueue.handleEvents();
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

        eventQueue.handleEvents();
        verify(event).doEvent();
    }

    @Test
    public void allQueuedEventsAreExecuted() {
        builder.eventQueue(eventQueue).event(event).build()
                .triggerEvent();
        builder.eventQueue(eventQueue).event(event).build()
                .triggerEvent();

        eventQueue.handleEvents();

        verify(event, times(2)).doEvent();
    }

}