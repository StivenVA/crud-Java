package org.project.interfaces;

public interface Observable {
    void registryObserver(Observer observer);
    void notifyObservers();
}
