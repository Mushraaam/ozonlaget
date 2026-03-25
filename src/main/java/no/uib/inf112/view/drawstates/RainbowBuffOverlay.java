package no.uib.inf112.view.drawstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;

public class RainbowBuffOverlay implements IDrawer {

    private IMap map;

    private static final int ALPHA = 120;

    private static final ArrayList<Color> colors = new ArrayList<>(List.of(
            new Color(255, 0, 0, ALPHA), // red
            new Color(255, 105, 180, ALPHA), // pink
            new Color(0, 0, 255, ALPHA), // blue
            new Color(255, 255, 0, ALPHA), // yellow
            new Color(0, 255, 255, ALPHA), // teal / cyan
            new Color(0, 255, 0, ALPHA), // green
            new Color(255, 0, 255, ALPHA), // magenta
            new Color(255, 165, 0, ALPHA), // orange
            new Color(128, 0, 255, ALPHA), // purple
            new Color(0, 255, 128, ALPHA) // turquoise
    ));

    public RainbowBuffOverlay(IMap map) {
        this.map = map;
        //
    }

    @Override
    public void draw(Graphics2D graphic) {
        IPlayer player = this.map.getPlayer();
        int index = player.buffCountDown() % 10;

        Rectangle2D bounds = graphic.getClipBounds();
        graphic.setColor(colors.get(index));
        graphic.fill(bounds);

    }

}
