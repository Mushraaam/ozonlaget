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

    private int shakeFrames = 0;
    private double shakeMagnitude = 0;

    public Camera(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }

    public void startShake(int frames, double magnitude) {
        this.shakeFrames = frames;
        this.shakeMagnitude = magnitude;
    }

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
    }

    public void apply(Graphics2D g) {
        double currentOffsetX = 0;
        double currentOffsetY = 0;

        if (this.shakeFrames > 0) {
            currentOffsetX = (Math.random() * 2 - 1) * this.shakeMagnitude;
            currentOffsetY = (Math.random() * 2 - 1) * this.shakeMagnitude;
            this.shakeFrames--;
        }
        g.translate(this.translateX + currentOffsetX, this.translateY + currentOffsetY);
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
