package no.uib.inf112.view.DrawStates;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;


import no.uib.inf112.interfaces.IDrawer;

public class MainMenu implements IDrawer{

    @Override
    public void draw(Graphics2D graphic) {
        int width = graphic.getClipBounds().width;
        int height = graphic.getClipBounds().height;

        graphic.setColor(new Color(187, 173, 160));
        graphic.fillRect(0, 0, width, height);

        graphic.setFont(new Font("Arial", Font.BOLD, 60));
        graphic.setColor(Color.WHITE);

        String title = "FUN GAME";
        FontMetrics titleMetrics = graphic.getFontMetrics();
        int titleX = (width - titleMetrics.stringWidth(title)) / 2;
        int titleY = height / 3;

        graphic.drawString(title, titleX, titleY);

        graphic.setFont(new Font("Arial", Font.BOLD, 30));

        String start = "PRESS ENTER TO START";
        FontMetrics startMetrics = graphic.getFontMetrics();
        int startX = (width - startMetrics.stringWidth(start)) / 2;
        int startY = height / 2;

        graphic.drawString(start, startX, startY);


    }
    
}
