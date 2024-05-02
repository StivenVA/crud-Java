package org.project;


import org.project.views.UsersTable;
import org.project.views.console.ConsoleView;
import org.project.views.gui.HomeScreen;


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
    }

}