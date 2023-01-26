package net.intelie.challenges;

import java.util.List;

public class EventIteratorImpl implements EventIterator {

    private List<Event> events;
    private int currIndex = -1;
    private boolean moveNextLastState = false;

    /**
     * Event iterator initializer 
     * @param events list of events
     */
    public EventIteratorImpl(List<Event> events) {
        this.events = events;
    }

    /**
     * Move the iterator to the next event, if any.
     *
     * @return false if the iterator has reached the end, true otherwise.
     */
    @Override
    public boolean moveNext() {
        currIndex++;
        if (events == null || events.isEmpty()) {
            moveNextLastState = false;
        }
        else if(currIndex < events.size()){
            moveNextLastState = true;
        }
        else {
            moveNextLastState = false;
        }
        return moveNextLastState;
    }

    /**
     * Gets the current event ref'd by this iterator.
     *
     * @return the event itself.
     * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
     */
    @Override
    public Event current() {
        if (currIndex < 0) {
            throw new IllegalStateException("Current event is not accessible! movenext() was never called!");
        }
        if (!moveNextLastState) {
            throw new IllegalStateException("Current event is not accessible! moveNext() last result was false.");
        }
        Event currentEvent = this.events.get(currIndex);
        return currentEvent;
    }

    /**
     * Remove current event from its store.
     *
     * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
     */
    @Override
    public void remove() {
        if (currIndex >= events.size()) {
            throw new IllegalStateException("You have reached end of evnets list, no more events to remove after last event is removed!");
        }
        if (currIndex < 0) {
            throw new IllegalStateException("cannot remove event!, movenext() was never called");
        }
        if (!moveNextLastState) {
            throw new IllegalStateException("cannot remove event! moveNext() last result was false.");
        }
        if (this.events.size() > currIndex) {
            this.events.remove(currIndex);
        }
    }

    /**
     * A helper function to get the number of events
     * @return total number of events in the iterator
     */
    public int getTotalEvents() {
        if (events == null) {
            return 0;
        }
        return events.size();
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        
    }

}
