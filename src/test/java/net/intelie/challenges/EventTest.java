package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EventTest {
    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }

    /**
     * validate inserting events of specific type
     * @throws Exception
     */
    @Test
    public void insertTest() throws Exception {
        Event event = new Event("Biking", 123L);
        Event event2 = new Event("Biking", 124L);
        Event event3 = new Event("Biking", 125L);
        Event event4 = new Event("Biking", 126L);

        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(event);
        eventStore.insert(event2);
        eventStore.insert(event3);
        eventStore.insert(event4);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 125L);
        assertEquals(2, eventIterator.getTotalEvents());
    }

    /**
     * validate removing events of specific type
     *
     * @throws Exception
     */
    @Test
    public void removeAllEventsOfTypeTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);
        Event bike_event4 = new Event("Biking", 126L);

        Event swim_event = new Event("Swimming", 123L);
        Event swim_event2 = new Event("Swimming", 124L);
        Event swim_event3 = new Event("Swimming", 125L);
        Event swim_event4 = new Event("Swimming", 126L);

        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);
        eventStore.insert(bike_event4);

        eventStore.insert(swim_event);
        eventStore.insert(swim_event2);
        eventStore.insert(swim_event3);
        eventStore.insert(swim_event4);

        eventStore.removeAll("Biking");
        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Swimming", 123L, 127L);
        assertEquals(4, eventIterator.getTotalEvents());

        EventIteratorImpl eventIterator2 = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(0, eventIterator2.getTotalEvents());

    }

    /**
     * validate iterating events list
     *
     * @throws Exception
     */
    @Test
    public void moveNextTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);
        Event bike_event4 = new Event("Biking", 126L);


        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);
        eventStore.insert(bike_event4);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(4, eventIterator.getTotalEvents());
        assertEquals(true, eventIterator.moveNext()); // 1st event exist check
        assertEquals(true, eventIterator.moveNext()); // 2nd event exist check
        assertEquals(true, eventIterator.moveNext()); // 3rd event exist check
        assertEquals(true, eventIterator.moveNext()); // 4th event exist check
        assertEquals(false, eventIterator.moveNext()); // 5th event doesn't exist check

    }

    /**
     * validate iterating through empty events list
     *
     * @throws Exception
     */
    @Test
    public void moveNextWhenEventsEmptyTest() throws Exception {
        EventStore eventStore = new EventStoreImpl();
        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(0, eventIterator.getTotalEvents());
        assertEquals(false, eventIterator.moveNext()); // events list is empty

    }

    /**
     * validate current event retrieval
     *
     * @throws Exception
     */
    @Test
    public void currentEventTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);
        Event bike_event4 = new Event("Biking", 126L);


        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);
        eventStore.insert(bike_event4);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(4, eventIterator.getTotalEvents());
        eventIterator.moveNext();
        Event currentEvent = eventIterator.current();

        assertEquals("Biking", currentEvent.type()); // 1st event type check
        assertEquals(123L, currentEvent.timestamp()); //1st event timestamp check 

        eventIterator.moveNext();
        currentEvent = eventIterator.current();
        assertEquals("Biking", currentEvent.type()); // 2nd event type check
        assertEquals(124L, currentEvent.timestamp()); // 2nd event timestamp check 

        eventIterator.moveNext();
        currentEvent = eventIterator.current();
        assertEquals("Biking", currentEvent.type()); // 3rd event type check
        assertEquals(125L, currentEvent.timestamp()); // 3rd event timestamp check 

        eventIterator.moveNext();
        currentEvent = eventIterator.current();
        assertEquals("Biking", currentEvent.type()); // 4th event type check
        assertEquals(126L, currentEvent.timestamp()); // 4th event timestamp check 

        eventIterator.moveNext();
        Exception currentExceptioonResult = assertThrows(IllegalStateException.class, () -> {eventIterator.current();});
        String expectedExceptionMessage = "Current event is not accessible! moveNext() last result was false.";
        String actString = currentExceptioonResult.getMessage();
        assertEquals(expectedExceptionMessage, actString); // check exception message

    }

    /**
     * Validate current event exception before calling move next
     * @throws Exception
     */
    @Test
    public void currentEventBeforeMoveNextCalledTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);
        Event bike_event4 = new Event("Biking", 126L);


        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);
        eventStore.insert(bike_event4);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(4, eventIterator.getTotalEvents());
        
        Exception currentExceptioonResult = assertThrows(IllegalStateException.class, () -> {eventIterator.current();});
        String expectedExceptionMessage = "Current event is not accessible! movenext() was never called!";
        String actString = currentExceptioonResult.getMessage();
        assertEquals(expectedExceptionMessage, actString); // check exception message

    }

    /**
     * validate removing event 
     *
     * @throws Exception
     */
    @Test
    public void removeCurrentEventTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);


        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(3, eventIterator.getTotalEvents());
        eventIterator.moveNext();

        Event curEvent = eventIterator.current();
        assertEquals("Biking", curEvent.type()); // 1st event type check
        assertEquals(123L, curEvent.timestamp()); //1st event timestamp check 

        eventIterator.remove();
        //Current event should be 2nd inserted event after removing first event
        curEvent = eventIterator.current();

        assertEquals("Biking", curEvent.type()); 
        assertEquals(124L, curEvent.timestamp()); 

        eventIterator.remove();
        //Current event should be 3rd inserted event
        curEvent = eventIterator.current();

        assertEquals("Biking", curEvent.type()); 
        assertEquals(125L, curEvent.timestamp()); 

        eventIterator.remove();

        Exception currentExceptioonResult = assertThrows(IllegalStateException.class, () -> {eventIterator.remove();});
        String expectedExceptionMessage = "You have reached end of evnets list, no more events to remove after last event is removed!";
        String actString = currentExceptioonResult.getMessage();
        assertEquals(expectedExceptionMessage, actString); // check exception message

    }

    /**
     * Validate removing an event before moveNext() is called
     * 
     * @throws Exception
     */
    @Test
    public void removeEventBeforeMoveNextCalledTest() throws Exception {
        Event bike_event = new Event("Biking", 123L);
        Event bike_event2 = new Event("Biking", 124L);
        Event bike_event3 = new Event("Biking", 125L);
        Event bike_event4 = new Event("Biking", 126L);


        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(bike_event);
        eventStore.insert(bike_event2);
        eventStore.insert(bike_event3);
        eventStore.insert(bike_event4);

        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 127L);
        assertEquals(4, eventIterator.getTotalEvents());
        
        Exception currentExceptioonResult = assertThrows(IllegalStateException.class, () -> {eventIterator.remove();});
        String expectedExceptionMessage = "cannot remove event!, movenext() was never called";
        String actString = currentExceptioonResult.getMessage();
        assertEquals(expectedExceptionMessage, actString); // check exception message

    }

    /**
     * validate invalid qurey for end time greater than start time
     * @throws Exception
     */
    @Test
    public void queryEndTimeLessThanStartTime() throws Exception {
        Event event = new Event("Biking", 123L);
        Event event2 = new Event("Biking", 124L);
        Event event3 = new Event("Biking", 125L);
        Event event4 = new Event("Biking", 126L);

        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(event);
        eventStore.insert(event2);
        eventStore.insert(event3);
        eventStore.insert(event4);
        
        Exception currentExceptioonResult = assertThrows(IllegalStateException.class, () -> {eventStore.query("Biking", 123L, 100L);});
        String expectedExceptionMessage = "Warning, start time is greater than end time!, Start time should be <= end time";
        String actString = currentExceptioonResult.getMessage();
        assertEquals(expectedExceptionMessage, actString);
    }

    /**
     * validate query for events between equal start and end time
     * @throws Exception
     */
    @Test
    public void queryEndTimeEqualStartTime() throws Exception {
        Event event = new Event("Biking", 123L);
        Event event2 = new Event("Biking", 124L);
        Event event3 = new Event("Biking", 125L);
        Event event4 = new Event("Biking", 126L);

        EventStore eventStore = new EventStoreImpl();
        eventStore.insert(event);
        eventStore.insert(event2);
        eventStore.insert(event3);
        eventStore.insert(event4);
        
        EventIteratorImpl eventIterator = (EventIteratorImpl) eventStore.query("Biking", 123L, 123L);
        //expecting empty list as endtime is equals to start time
        assertEquals(0, eventIterator.getTotalEvents());
    }
}