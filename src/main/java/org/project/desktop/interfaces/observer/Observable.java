package org.project.desktop.interfaces.observer;

public interface Observable {
    void registryObserver(Observer observer);
    void notifyObservers();
}
