package no.uib.inf112.view.drawstates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;

public class VictoryOverlay implements IDrawer {

    @Override
    public void draw(Graphics2D graphic) {
        graphic.setTransform(new AffineTransform());

        Rectangle2D bounds = graphic.getClipBounds();
        if (bounds == null) {
            return;
        }

        String text = "You live... for now!";
        int fontSize = (int) (bounds.getHeight() / 10);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        graphic.setFont(font);

        FontMetrics metrics = graphic.getFontMetrics(font);

        int xText = (int) ((bounds.getWidth() - metrics.stringWidth(text)) / 2);
        int yText = (int) ((bounds.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());

        graphic.setColor(Color.WHITE);
        graphic.drawString(text, xText, yText);
    }
}
