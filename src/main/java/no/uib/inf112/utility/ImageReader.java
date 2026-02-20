package no.uib.inf112.utility;

import java.awt.*;
import java.awt.image.BufferedImage;
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

        public static BufferedImage resizeExact(BufferedImage original, int width, int height) {
            GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage resized = config.createCompatibleImage(width, height, original.getTransparency());

            Graphics2D g2d = resized.createGraphics();

            // This is gippity magic, improves image quality /////////
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();

            return resized;
        }
}
