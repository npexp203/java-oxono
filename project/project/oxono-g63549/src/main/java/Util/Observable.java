package Util;

/**
 * The Observable interface provides methods to manage observers.
 * It allows adding, removing, and notifying observers about events.
 */
public interface Observable {

    /**
     * Removes an observer from the list of observers.
     * 
     * @param o the observer to be removed
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers about a specific event.
     * 
     * @param event the event that occurred
     * @param value additional information about the event
     */
    void notifyObservers(String event, Object value);

    /**
     * Adds an observer to the list of observers.
     * 
     * @param o the observer to be added
     */
    void addObserver(Observer o);
}