package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageReader {

    /**
     * @param url relative filepath
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage fetchImage(String url) {
        // url should look like: "/no/uib/inf112/player/player1_east.png"
        try (InputStream in = ImageHandler.class.getResourceAsStream(url)) {
            if (in == null) {
                System.out.printf("Cannot load resource: %s%n", url);
                return null;
            }
            return ImageIO.read(in);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.printf("Error reading resource: %s%n", url);
            return null;
        }
    }
}

