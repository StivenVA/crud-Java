package org.project.fachada;

import org.project.fachada.views.console.ConsoleView;
import org.project.fachada.views.gui.HomeScreen;

import java.io.IOException;

public class VistasFachada {
    private HomeScreen homeScreen;
    private ConsoleView consoleView;

    public VistasFachada(){
        try {
            homeScreen = new HomeScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        consoleView = new ConsoleView();
    }

    public void iniciarVistas() {
        consoleView.iniciar();
        homeScreen.iniciar();
    }

}
