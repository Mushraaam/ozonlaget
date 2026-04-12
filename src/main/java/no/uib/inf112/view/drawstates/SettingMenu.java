package no.uib.inf112.view.drawstates;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageHandler;

public class SettingMenu implements IDrawer {
    private ImageHandler handler;

    public SettingMenu(ImageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void draw(Graphics2D graphic) {
        drawBackground(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle bounds = graphic.getClipBounds();
        BufferedImage background = handler.getMenuBackground();

        graphic.drawImage(background, 0, 0, bounds.width, bounds.height, null);
    }
}
