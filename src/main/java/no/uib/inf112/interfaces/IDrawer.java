package no.uib.inf112.interfaces;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface IDrawer {
    
    
    default void drawImage(Graphics2D graphic, BufferedImage image, Rectangle2D.Double bounds) {
        graphic.drawImage(image,
                (int) bounds.getX(),
                (int) bounds.getY(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight(),
                null);
    }

    public void draw(Graphics2D graphic);

}
