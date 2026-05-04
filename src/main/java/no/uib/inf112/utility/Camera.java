package no.uib.inf112.utility;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.config.Config;

// class for handling camera translation, centering on player, and converting screen to world coordinates
public class Camera {
    private double translateX;
    private double translateY;
    private static final int UI_HEIGHT = Config.getInt("uiSize");
    double currentOffsetX = 0;
    double currentOffsetY = 0;
    private int shakeFrames = 0;
    private double shakeMagnitude = 0;

    /**
     * Translates between screen coordinates and map coordinates
     * @param translateX
     * @param translateY
     */
    public Camera(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }

    /**
     * Creates a screen shake - starts it
     * @param frames
     * @param magnitude
     */
    public void startShake(int frames, double magnitude) {
        this.shakeFrames = frames;
        this.shakeMagnitude = magnitude;
    }

    /**
     * Updates the coordinates to player on map
     * @param playerHitbox
     * @param screenWidth
     * @param screenHeight
     * @param mapBounds
     */
    public void update(Rectangle2D.Double playerHitbox, double screenWidth, double screenHeight,
            Rectangle2D.Double mapBounds) {
        double playerCenterX = playerHitbox.getCenterX();
        double playerCenterY = playerHitbox.getCenterY();

        double tx = (screenWidth / 2 - playerCenterX);
        double ty = (screenHeight / 2 - playerCenterY);

        double minTranslateX = screenWidth - mapBounds.getWidth();
        double minTranslateY = screenHeight - mapBounds.getHeight() - UI_HEIGHT; // allow UI to go below grid

        tx = Math.clamp(tx, minTranslateX, 0);
        ty = Math.clamp(ty, minTranslateY, 0);

        this.translateX = tx;
        this.translateY = ty;


        if (this.shakeFrames > 0) {
            this.currentOffsetX = (Math.random() * 2 - 1) * this.shakeMagnitude;
            this.currentOffsetY = (Math.random() * 2 - 1) * this.shakeMagnitude;
            this.shakeFrames--;
        } else {
            this.currentOffsetX = 0;
            this.currentOffsetY = 0;
        }
    }

    /**
     * Applies translation
     * @param g
     */
    public void apply(Graphics2D g) {
        g.translate(this.translateX + currentOffsetX, this.translateY + currentOffsetY);
    }

    /**
     * returns translated coordinates screen to world
     * @param screenX
     * @param screenY
     * @return
     */
    public Point2D.Double screenToWorld(double screenX, double screenY) {
        return new Point2D.Double(screenX - translateX, screenY - translateY);
    }

    /**
     * @return translated x
     */
    public double getTranslateX() {
        return translateX;
    }

    /**
     * @return translated y
     */
    public double getTranslateY() {
        return translateY;
    }

}
