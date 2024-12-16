package Util;


public interface Observable {
    void removeObserver(Observer o);
    void notifyObservers(String event, Object value);

    void addObserver(Observer o);
}



