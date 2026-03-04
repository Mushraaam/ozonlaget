package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.interfaces.IDrawer;

public class MainMenu implements IDrawer{

    private Rectangle2D.Double startButton;

    @Override
    public void draw(Graphics2D graphic) {
       
        drawBackground(graphic);
        drawTitle(graphic);
        drawStartButton(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();

        graphic.setColor(new Color(187, 173, 160));
        graphic.fillRect(0, 0, bounds.width, bounds.height);
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

        int buttonWidth = 300;
        int buttonHeight = 80;

        int x = (bounds.width - buttonWidth) / 2;
        int y = bounds.height / 2;

        startButton = new Rectangle2D.Double(x, y, buttonWidth, buttonHeight);

        //background
        graphic.setColor(new Color(120, 100, 80));
        graphic.fill(startButton);

        //border
        graphic.setColor(Color.WHITE);
        graphic.draw(startButton);

        //text
        graphic.setFont(new Font("Arial", Font.BOLD, 30));

        String text = "START";

        FontMetrics metrics = graphic.getFontMetrics();

        int textX = (int) (startButton.getX() + 
                (startButton.getWidth() - metrics.stringWidth(text)) / 2);
        
        int textY = (int) (startButton.getY() + 
                (startButton.getHeight() + metrics.getAscent()) / 2 - 8);
        
        graphic.drawString(text, textX, textY);
    }

    public Rectangle2D getStartButton() {
        return startButton;
    }
    
}
