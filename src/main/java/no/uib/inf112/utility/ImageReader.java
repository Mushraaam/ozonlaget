package no.uib.inf112.utility;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public final class ImageReader {
    private ImageReader() {
        /* This utility class should not be instantiated */
    }

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
            System.out.printf("Error reading resource: %s%n", url);
            return null;
        }
    }

    // Originally hand made, but improved with chatgpt in order to improve image
    // quality
    public static BufferedImage resizeExact(BufferedImage original, int width, int height) {
        GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();

        int w = original.getWidth();
        int h = original.getHeight();

        BufferedImage img = original;

        // gradually scale seems to improve quality compared to a single scale action
        while (w / 2 >= width && h / 2 >= height) {
            w /= 2;
            h /= 2;

            BufferedImage tmp = config.createCompatibleImage(w, h, original.getTransparency());
            Graphics2D g = tmp.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.drawImage(img, 0, 0, w, h, null);
            g.dispose();

            img = tmp;
        }

        BufferedImage resized = config.createCompatibleImage(width, height, original.getTransparency());
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.drawImage(img, 0, 0, width, height, null);
        g2d.dispose();

        return resized;
    }
}
