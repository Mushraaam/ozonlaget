package no.uib.inf112.view.drawstates;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;
import java.awt.AlphaComposite;
import java.awt.Color;

public class DeathOverlay implements IDrawer {

    private ImageHandler handler;
    private float alpha;

    /**
     * Draws the death overlay
     * @param handler
     */
    public DeathOverlay(ImageHandler handler) {
        this.handler = handler;
        this.alpha = 0;
    }

    @Override
    public void draw(Graphics2D graphic) {

        if (this.alpha < 1) {
            this.alpha = this.alpha + (float) 0.006;
            if (this.alpha > 1) {
                this.alpha = 1;
            }
        }

        // save old settings
        var oldComposite = graphic.getComposite();
        var oldTransform = graphic.getTransform();

        // transform coordinates to screen
        graphic.setTransform(new java.awt.geom.AffineTransform());
        Rectangle2D bounds = graphic.getClipBounds();

        // set transparrency - should gradually become more solid
        graphic.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                this.alpha));

        // draw image/background
        int screenWidth = (int) bounds.getWidth();
        int imageHeight = 800;
        int x = 0;
        int y = (int) ((bounds.getHeight() - imageHeight) / 2);
        graphic.setColor(Color.BLACK);
        graphic.fill(bounds);
        graphic.drawImage(
                this.handler.youDied(),
                x,
                y,
                screenWidth,
                imageHeight,
                null);

        // reset coordinates
        graphic.setComposite(oldComposite);
        graphic.setTransform(oldTransform);
    }

}
