package org.project.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

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
}
