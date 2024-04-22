package org.project.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class InputStreamConverter {

    public Image convertBinaryStreamToImage(InputStream inputStream) throws IOException{
        byte[] bytes = inputStream.readAllBytes();

        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
