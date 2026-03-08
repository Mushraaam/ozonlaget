package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class MainMenu implements IDrawer{

    private Rectangle2D.Double startButton;
    private ImageHandler handler;

    public MainMenu(ImageHandler handler) {
        this.handler = handler;
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

        int buttonWidth = 350;
        int buttonHeight = 150;

        int x = (bounds.width - buttonWidth) / 2;
        int y = bounds.height / 2;

        graphic.drawImage(buttonImage, x, y, buttonWidth, buttonHeight ,null);

        int marginLeft = 85;
        int marginRight = 85;

        int marginTop = 55;
        int marginBottom = 60;

        startButton = new Rectangle2D.Double(
            x + marginLeft, 
            y + marginTop, 
            buttonWidth - marginLeft - marginRight, 
            buttonHeight - marginTop - marginBottom
        );

        // DEBUG – viser hitboxen
        graphic.setColor(Color.RED);
        graphic.draw(startButton);
    }

    public Rectangle2D.Double getStartButton() {
        return startButton;
    }
    
}
