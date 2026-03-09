package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class MainMenu implements IDrawer{

    private Rectangle2D.Double startButton;

    private final ImageHandler handler;

    private double startButtonX;
    private double startButtonY;
    private boolean animationStarted = false;

    private static final int BUTTON_WIDTH = 350;
    private static final int BUTTON_HEIGHT = 150;

    private static final int MARGIN_LEFT = 85;
    private static final int MARGIN_RIGHT = 85;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_BOTTOM = 60;

    public MainMenu(ImageHandler handler) {
        this.handler = handler;
        this.startButton = new Rectangle2D.Double();
    }

    //use for later when going back to mainmenu
    public void resetAnimation() {
        animationStarted = false;
    }

    @Override
    public void draw(Graphics2D graphic) {
       
        drawBackground(graphic);
        drawTitle(graphic);
        drawStartButton(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }

    private void drawTitle(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        graphic.setFont(new Font("Arial", Font.BOLD, 70));
        graphic.setColor(Color.WHITE);

        String title = "FUN GAME";

        FontMetrics metrics = graphic.getFontMetrics();
        int x = (bounds.width - metrics.stringWidth(title)) / 2;
        int y = bounds.height / 3;

        graphic.drawString(title, x, y);
    }

    private void drawStartButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage buttonImage = handler.getStartButton();

        int targetX = (bounds.width - BUTTON_WIDTH) / 2;
        int targetY = bounds.height / 2;

        if (!animationStarted) {
            startButtonX = -BUTTON_WIDTH;
            startButtonY = targetY;
            animationStarted = true;
        }

        startButtonX += (targetX - startButtonX) * 0.05;

        if (Math.abs(targetX - startButtonX) < 0.5) {
            startButtonX = targetX;
        }

        graphic.drawImage(
            buttonImage, 
            (int) startButtonX, 
            (int) startButtonY, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT,
            null
        );

        startButton.setRect(
            startButtonX + MARGIN_LEFT,
            startButtonY + MARGIN_TOP,
            BUTTON_WIDTH - MARGIN_LEFT - MARGIN_RIGHT,
            BUTTON_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM
        );

        // DEBUG – viser hitboxen
        graphic.setColor(Color.RED);
        graphic.draw(startButton);
    }

    public Rectangle2D.Double getStartButton() {
        return startButton;
    }
    
}
