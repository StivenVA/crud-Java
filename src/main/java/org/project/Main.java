package org.project;

import org.project.gui.HomeScreen;

import javax.swing.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(()->{
            HomeScreen screen;
            try {
                screen = new HomeScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            screen.setVisible(true);
            screen.setTitle("Welcome");
            screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            screen.setSize(900, 1000);
            screen.pack();
            screen.setLocationRelativeTo(null);
        });

    }

}