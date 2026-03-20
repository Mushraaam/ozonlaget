package no.uib.inf112.view.drawstates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class MainMenu implements IDrawer{

    private Rectangle2D.Double startButton;
    private Rectangle2D.Double settingsButton;
    private Rectangle2D.Double helpButton;

    private final ImageHandler handler;

    private double startButtonX;
    private double startButtonY;
    private double settingsButtonX;
    private double settingsButtonY;
    private double helpButtonX;
    private double helpButtonY;
    private boolean startAnimationStarted = false;
    private boolean settingAnimationStarted = false;
    private boolean helpAnimationStarted = false;

    private static final int BUTTON_WIDTH = 350;
    private static final int BUTTON_HEIGHT = 150;

    private static final int MARGIN_LEFT = 85;
    private static final int MARGIN_RIGHT = 85;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_BOTTOM = 60;

    private static final int TITLE_OFFSET_Y = -80;

    public MainMenu(ImageHandler handler) {
        this.handler = handler;
        this.startButton = new Rectangle2D.Double();
        this.settingsButton = new Rectangle2D.Double();
        this.helpButton = new Rectangle2D.Double();
    }

    //use for later when going back to mainmenu
    public void resetAnimation() {
        startAnimationStarted = false;
        settingAnimationStarted = false;
        helpAnimationStarted = false;
    }

    @Override
    public void draw(Graphics2D graphic) {
        drawBackground(graphic);
        drawTitle(graphic);
        drawStartButton(graphic);
        drawSettingsButton(graphic);
        drawHelpButton(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }

    private void drawTitle(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage titleImage = handler.getMenuTitle();

        int width = 1100;
        
        double aspectRatio = (double) titleImage.getHeight() / titleImage.getWidth();
        int height = (int) (width * aspectRatio);

        int x = (bounds.width - width) / 2;
        int y = 5;

        graphic.drawImage(titleImage, x, y + TITLE_OFFSET_Y, width, height, null);
    }

    private void drawStartButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage startImage = handler.getStartButton();

        int targetX = bounds.width / 2 - BUTTON_WIDTH + 35;
        int targetY = bounds.height / 2;

        if (!startAnimationStarted) {
            startButtonX = -BUTTON_WIDTH;
            startButtonY = targetY;
            startAnimationStarted = true;
        }

        startButtonX += (targetX - startButtonX) * 0.05;

        if (Math.abs(targetX - startButtonX) < 0.5) {
            startButtonX = targetX;
        }

        graphic.drawImage(
            startImage, 
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
    }

    private void drawSettingsButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage settingImage = handler.getSettingsButton();

        int targetX = bounds.width / 2 - 35;
        int targetY = bounds.height / 2;

        if (!settingAnimationStarted) {
            settingsButtonX = bounds.width;
            settingsButtonY = targetY;
            settingAnimationStarted = true;
        }

        settingsButtonX += (targetX - settingsButtonX) * 0.05;

        if (Math.abs(targetX - settingsButtonX) < 0.5) {
            settingsButtonX = targetX;
        }

        graphic.drawImage(
            settingImage,
            (int) settingsButtonX,
            (int) settingsButtonY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            null
        );

        settingsButton.setRect(
            settingsButtonX + MARGIN_LEFT - 2,
            settingsButtonY + MARGIN_TOP - 3,
            BUTTON_WIDTH - MARGIN_LEFT - MARGIN_RIGHT + 3,
            BUTTON_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM - 3
        );
    }

    private void drawHelpButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage helpImage = handler.getHelpButton();

        int targetX = bounds.width / 2 - BUTTON_WIDTH / 2;
        int targetY = bounds.height / 2 + 120;

        if (!helpAnimationStarted) {
            helpButtonX = targetX;
            helpButtonY = bounds.height + BUTTON_HEIGHT;
            helpAnimationStarted = true;
        }

        helpButtonY += (targetY - helpButtonY) * 0.05;

        if (Math.abs(targetY - helpButtonY) < 0.5) {
            helpButtonY = targetY;
        }

        graphic.drawImage(
            helpImage,
            (int) helpButtonX,
            (int) helpButtonY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            null
        );

        helpButton.setRect(
            helpButtonX + MARGIN_LEFT,
            helpButtonY + MARGIN_TOP - 2,
            BUTTON_WIDTH - MARGIN_LEFT - MARGIN_RIGHT,
            BUTTON_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM - 2
        );
    }

    public Rectangle2D.Double getStartButton() {
        return startButton;
    }

    public Rectangle2D.Double getHelpButton() {
        return helpButton;
    }
    
}
