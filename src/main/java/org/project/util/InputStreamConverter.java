package org.project.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputStreamConverter {

    public static  Image convertBinaryStreamToImage(InputStream inputStream) throws IOException{
        byte[] bytes = inputStream.readAllBytes();

        return ImageIO.read(new ByteArrayInputStream(bytes));
    }

    public static FileInputStream convertFileToInputStream(File image){
        try {
            return new FileInputStream(image);
        } catch (FileNotFoundException | NullPointerException e) {
            return null;
        }
    }

    public static File inputStreamToFile(InputStream inputStream) {
        File tempFile;
        try {
            tempFile = File.createTempFile("archivo", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tempFile;
    }

    public static File byteArrayToFile(byte[] byteArray, String fileName,String format){
        // Crea un archivo temporal en el directorio de archivos temporales del sistema
        Path tempFilePath;
        try {
            tempFilePath = Files.createTempFile(fileName, "."+format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Crea un flujo de entrada de bytes desde el array de bytes
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
             FileOutputStream fos = new FileOutputStream(tempFilePath.toFile())) {

            // Lee los bytes del flujo de entrada de bytes y escríbelos en el archivo temporal
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bais.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Retorna el archivo temporal creado
        return tempFilePath.toFile();
    }
}
