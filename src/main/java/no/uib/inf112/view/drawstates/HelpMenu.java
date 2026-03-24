package no.uib.inf112.view.drawstates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class HelpMenu implements IDrawer {

    private ImageHandler handler;

    public HelpMenu(ImageHandler handler){
        this.handler = handler;
    }

    @Override
    public void draw(Graphics2D graphic) {
        drawBackground(graphic);
        drawHelpText(graphic);
    }

    private void drawHelpText(Graphics2D graphic) {
        Font font = graphic.getFont();
        graphic.setFont(new Font(font.getName(), font.getStyle(), 50));
        graphic.setColor(Color.LIGHT_GRAY);
        graphic.drawString("VERY HELPFUL TEXT", 310, 400);
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();
        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }

}
