package no.uib.inf112.view.drawstates;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class SettingMenu implements IDrawer {
    private final ImageHandler handler;
    private boolean settingAnimationStarted = false;

    private static final int BUTTON_WIDTH = 320;
    private static final int BUTTON_HEIGHT = 150;

    private static final int MARGIN_LEFT = 85;
    private static final int MARGIN_RIGHT = 85;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_BOTTOM = 60;

    private double backButtonX;
    private double backButtonY;
    private Rectangle2D.Double backButton;

    public SettingMenu(ImageHandler handler) {
        this.handler = handler;
        this.backButton = new Rectangle2D.Double();
    }

    @Override
    public void draw(Graphics2D graphic) {
        drawBackground(graphic);
        drawBackButton(graphic);
    }

    public void resetAnimation() {
        this.settingAnimationStarted = false;
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();
        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }

    private void drawBackButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage helpImage = handler.getbackButton();

        double targetX = 0;
        double targetY = bounds.height / 1.2;

        if (!settingAnimationStarted) {
            backButtonX = targetX;
            backButtonY = -BUTTON_HEIGHT;
            settingAnimationStarted = true;
        }

        backButtonY += (targetY - backButtonY) * 0.05;

        if (Math.abs(targetY - backButtonY) < 0.5) {
            backButtonY = targetY;
        }

        graphic.drawImage(
                helpImage,
                (int) backButtonX,
                (int) backButtonY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                null);

        backButton.setRect(
                backButtonX + MARGIN_LEFT,
                backButtonY + MARGIN_TOP - 2,
                (double) BUTTON_WIDTH - MARGIN_LEFT - MARGIN_RIGHT,
                (double) BUTTON_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM - 2);
    }

    public Rectangle2D.Double getBackButton() {
        return this.backButton;
    }
}
