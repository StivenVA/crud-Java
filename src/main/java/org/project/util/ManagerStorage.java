package org.project.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ManagerStorage {

    private final File directory;

    public ManagerStorage(String path) {
        this.directory = new File(path);
    }

    public void saveFile(File file) {
        Path directoryPath = Paths.get(this.directory.getPath());
        System.out.println("Directorio: " + directoryPath); // Imprimir la ruta del directorio
        System.out.println("Archivo: "+file.getPath());
        try {
            Files.createDirectories(directoryPath); // Crear directorio si no existe
            Path destination = directoryPath.resolve(file.getName());
            Files.move(file.toPath(), destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
