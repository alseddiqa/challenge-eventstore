package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class EventStoreImpl implements EventStore {

    private Map<String, TreeMap<Long, Event>> events;

    public EventStoreImpl(){
        this.events = new ConcurrentHashMap<>();
    }

    /**
     * Stores an event
     *
     * @param event
     */
    @Override
    public void insert(Event event) {
        if (event == null) {
            return;
        }
        TreeMap<Long, Event> typeEvents = events.get(event.type());
        if (typeEvents == null) {
            typeEvents = new TreeMap<>();
            typeEvents.put(event.timestamp(),event);
            events.put(event.type(), typeEvents);
        }else {
            typeEvents.put(event.timestamp(),event);
        }

    }

    /**
     * Removes all events of specific type.
     *
     * @param type
     */
    @Override
    public void removeAll(String type) {
        events.remove(type);
    }

     /**
     * Retrieves an iterator for events based on their type and timestamp.
     *
     * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events have same type as
     * {@param type} and timestamp between {@param startTime}
     * (inclusive) and {@param endTime} (exclusive).
     */
    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        if (startTime > endTime) {
            throw new IllegalStateException("Warning, start time is greater than end time!, Start time should be <= end time");
        }
        TreeMap<Long, Event> typeEvents = events.get(type);
        if (typeEvents == null) {
             return new EventIteratorImpl(new ArrayList<>());
        }
        NavigableMap<Long, Event> matchedEvents = typeEvents.subMap(startTime, true, endTime, false);
        List<Event> eventsList = new ArrayList<>(matchedEvents.values());
        return new EventIteratorImpl(eventsList);
    }
    
}
