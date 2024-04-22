package org.project;

import org.project.views.console.ConsoleView;
import org.project.views.gui.HomeScreen;

import javax.swing.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        Thread thread = new Thread(new ConsoleView());
        thread.start();

        try {
            new HomeScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}