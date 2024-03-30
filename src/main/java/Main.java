import gui.HomeScreen;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        HomeScreen screen = new HomeScreen();

        screen.setVisible(true);
        screen.setTitle("Welcome");
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setSize(900, 1000);
        screen.pack();
        screen.setLocationRelativeTo(null);


    }

}