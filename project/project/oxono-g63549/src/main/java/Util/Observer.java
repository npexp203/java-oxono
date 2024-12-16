/**
 * Interface for classes that can be notified of events.
 *
 * @author Lo c POTTIER
 */
package Util;

public interface Observer {
    /**
     * Called when an event occurs.
     *
     * @param event The type of event that occurred.
     * @param value The value associated with the event.
     */
    void update(String event, Object value);
}