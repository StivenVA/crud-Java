package org.project;

import org.project.desktop.views.UsersTable;
import org.project.desktop.views.console.ConsoleView;
import org.project.desktop.views.gui.HomeScreen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

            ConsoleView consoleView= new ConsoleView();
            consoleView.iniciar();

            HomeScreen homeScreen = new HomeScreen();
            homeScreen.iniciar();

            UsersTable usersTable = new UsersTable();
            usersTable.iniciar();

            homeScreen.registryObserver(usersTable);
            consoleView.registryObserver(usersTable);

            SpringApplication.run(Main.class, args);

    }

}