package no.uib.inf112.view.drawstates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class HelpMenu implements IDrawer {

    private ImageHandler handler;
    private boolean helpAnimationStarted = false;
    private boolean fartAnimationStarted = false;

    private static final int BUTTON_WIDTH = 320;
    private static final int BUTTON_HEIGHT = 150;

    private static final int MARGIN_LEFT = 85;
    private static final int MARGIN_RIGHT = 85;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_BOTTOM = 60;

    private double backButtonX;
    private double backButtonY;
    private Rectangle2D.Double backButton;
    private Rectangle2D.Double fartButton;
    private double fartButtonX;
    private double fartButtonY;

    public HelpMenu(ImageHandler handler) {
        this.handler = handler;
        this.backButton = new Rectangle2D.Double();
        this.fartButton = new Rectangle2D.Double();
    }

    @Override
    public void draw(Graphics2D graphic) {
        drawBackground(graphic);
        drawHelpText(graphic);
        drawControls(graphic);
        drawItemHelp(graphic);
        drawHelicopter(graphic);
        drawBackButton(graphic);
        drawFartButton(graphic);
    }

    private void drawHelicopter(Graphics2D graphic) {
        BufferedImage helicopter = handler.getVehicleImage(1);

        graphic.setColor(Color.DARK_GRAY);
        Rectangle2D.Double background = new Rectangle.Double(355, 10, 595, 350);
        graphic.fill(background);
        graphic.setColor(Color.WHITE);
        graphic.draw(background);

        drawImage(graphic, helicopter, background);

        Font font = graphic.getFont();
        graphic.setFont(new Font(font.getName(), font.getStyle(), 20));
        graphic.drawString("Helicopter (we drew this ourselves, believe it or not)", 410, 330);

    }

    private void drawControls(Graphics2D graphic) {

        BufferedImage wasd = handler.getWASD();
        BufferedImage mouse = handler.getMouseImage();
        BufferedImage numbers = handler.getNumbersImage();

        graphic.setColor(Color.DARK_GRAY);
        Rectangle2D.Double background = new Rectangle.Double(10, 370, 940, 320);
        graphic.fill(background);
        graphic.setColor(Color.WHITE);
        graphic.draw(background);

        //Draw images
        drawImage(graphic, wasd, new Rectangle.Double(15, 375, 500, 200));
        drawImage(graphic, mouse, new Rectangle.Double(515, 375, 150, 200));
        drawImage(graphic, numbers, new Rectangle2D.Double(665, 375, 200, 200));

        //Draw text
        Font font = graphic.getFont();
        graphic.setFont(new Font(font.getName(), font.getStyle(), 30));
        graphic.drawString("Move", 230, 600);
        graphic.drawString("Shoot", 530, 600);
        graphic.drawString("Change weapons", 660, 600);
        
    }

    private void drawItemHelp(Graphics2D graphic) {
        // Fetch all images
        BufferedImage rainbowBuff = handler.getCollectableImage(CollectableType.POWERUP_RAINBOW);
        BufferedImage speedBuff = handler.getCollectableImage(CollectableType.POWERUP_SPEED);
        BufferedImage damageBuff = handler.getCollectableImage(CollectableType.POWERUP_DAMAGE);
        BufferedImage gasoline = handler.getCollectableImage(CollectableType.GASCAN);
        BufferedImage keycard = handler.getCollectableImage(CollectableType.CHOPPERKEY);

        // Draw background
        graphic.setColor(Color.DARK_GRAY);
        Rectangle2D.Double background = new Rectangle.Double(960, 10, 230, 680);
        graphic.fill(background);
        graphic.setColor(Color.WHITE);
        graphic.draw(background);

        // Draw images
        drawImage(graphic, rainbowBuff, new Rectangle2D.Double(1000, 30, 150, 120));
        drawImage(graphic, speedBuff, new Rectangle2D.Double(1000, 180, 150, 120));
        drawImage(graphic, damageBuff, new Rectangle2D.Double(1000, 330, 150, 120));
        drawImage(graphic, gasoline, new Rectangle2D.Double(1040, 480, 60, 80));
        drawImage(graphic, keycard, new Rectangle2D.Double(1030, 600, 85, 60));

        // Draw text
        Font font = graphic.getFont();
        graphic.setFont(new Font(font.getName(), font.getStyle(), 15));
        graphic.drawString("Double damage, infinite ammo", 970, 165);
        graphic.drawString("Superspeed", 1030, 315);
        graphic.drawString("Double damage", 1020, 465);
        graphic.drawString("Gasoline", 1040, 580);
        graphic.drawString("Keycard", 1040, 680);
    }

    private void drawHelpText(Graphics2D graphic) {

        Font font = graphic.getFont();
        Rectangle2D.Double textBackground = new Rectangle.Double(10, 10, 335, 350);
        graphic.setColor(Color.DARK_GRAY);
        graphic.fill(textBackground);
        graphic.setColor(Color.WHITE);
        graphic.draw(textBackground);

        graphic.setFont(new Font(font.getName(), font.getStyle(), 30));
        graphic.setColor(Color.WHITE);
        graphic.drawString("Story:", 25, 60);
        graphic.setFont(new Font(font.getName(), font.getStyle(), 20));
        graphic.drawString("You are stranded in a village.", 30, 100);
        graphic.drawString("There are monsters all around you", 30, 130);
        graphic.drawString("There is a helicopter nearby:", 30, 160);
        graphic.drawString("- Deliver 6 cans of fuel", 40, 190);
        graphic.drawString("- Pick up the keycard", 40, 220);
        graphic.drawString("- Fly awayyyyy", 40, 250);

        graphic.drawString("And most importantly...", 30, 280);
        graphic.setFont(new Font(font.getName(), font.getStyle(), 40));
        graphic.setColor(Color.RED);
        graphic.drawString("DON'T DIE", 30, 330);

    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();
        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }

    public void resetAnimation() {
        this.helpAnimationStarted = false;
        this.fartAnimationStarted = false;
    }

    private void drawBackButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage helpImage = handler.getbackButton();

        double targetX = 0;
        double targetY = bounds.height / 1.2;

        if (!helpAnimationStarted) {
            backButtonX = targetX;
            backButtonY = -BUTTON_HEIGHT;
            helpAnimationStarted = true;
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

    private void drawFartButton(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        BufferedImage helpImage = handler.getfartButton();

        double targetX = 920;
        double targetY = (bounds.height / 1.2);

        if (!fartAnimationStarted) {
            fartButtonX = targetX;
            fartButtonY = -BUTTON_HEIGHT;
            fartAnimationStarted = true;
        }

        fartButtonY += (targetY - fartButtonY) * 0.05;

        if (Math.abs(targetY - fartButtonY) < 0.5) {
            fartButtonY = targetY;
        }

        graphic.drawImage(
                helpImage,
                (int) fartButtonX,
                (int) fartButtonY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                null);

        fartButton.setRect(
                fartButtonX + MARGIN_LEFT,
                fartButtonY + MARGIN_TOP - 2,
                (double) BUTTON_WIDTH - MARGIN_LEFT - MARGIN_RIGHT,
                (double) BUTTON_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM - 2);
    }

    public Rectangle2D.Double getFartButton() {
        return this.fartButton;
    }
}
