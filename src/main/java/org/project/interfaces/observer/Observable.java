package org.project.interfaces.observer;

public interface Observable {
    void registryObserver(Observer observer);
    void notifyObservers();
}
