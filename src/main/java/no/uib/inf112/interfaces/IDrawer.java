package no.uib.inf112.interfaces;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface IDrawer {
    
    /**
     * Draws an image
     * @param graphic
     * @param image - Image to be drawn
     * @param bounds - Bounds for the image
     */
    default void drawImage(Graphics2D graphic, BufferedImage image, Rectangle2D.Double bounds) {

        
        graphic.drawImage(image,
                (int) bounds.getX(),
                (int) bounds.getY(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight(),
                null);
    }

    /**
     * @param graphic
     * @param objectBounds
     * @return true if object is withing view bounds
     * Use to decide if something should be drawn or not
     */
    default boolean isVisible(Graphics2D graphic, Rectangle2D.Double objectBounds){
        Rectangle2D clip = graphic.getClipBounds();
        return objectBounds.intersects(clip);
    }

    /**
     * Draws the screen for the corresponding class
     * @param graphic
     */
    public void draw(Graphics2D graphic);

}
