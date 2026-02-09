package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {

    /**
     * @param url relative filepath
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage fetcImage(String url) {
        try {
            return ImageIO.read(new File(url));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.printf("Cannot load url: %s\n", url);
        }
        return null;
    }
}

