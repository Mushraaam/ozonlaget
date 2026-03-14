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

    public Camera(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }
    
    public void update(Rectangle2D.Double playerHitbox, double screenWidth, double screenHeight,
            Rectangle2D.Double mapBounds) {
        double playerCenterX = playerHitbox.getCenterX();
        double playerCenterY = playerHitbox.getCenterY();

        double tx = (screenWidth / 2 - playerCenterX);
        double ty = (screenHeight / 2 - playerCenterY);

        double minTranslateX = screenWidth - mapBounds.getWidth();
        double minTranslateY = screenHeight - mapBounds.getHeight() - UI_HEIGHT; // allow UI to go below grid

        tx = Math.min(0, Math.max(tx, minTranslateX));
        ty = Math.min(0, Math.max(ty, minTranslateY));

        this.translateX = tx;
        this.translateY = ty;
    }

    public void apply(Graphics2D g) {
        g.translate(translateX, translateY);
    }

    public Point2D.Double screenToWorld(double screenX, double screenY) {
        return new Point2D.Double(screenX - translateX, screenY - translateY);
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

}
