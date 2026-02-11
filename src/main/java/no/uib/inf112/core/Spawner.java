package no.uib.inf112.core;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;

import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.npcs.Thug;

public class Spawner {
    private static final double THUG_WIDTH = 80;
    private static final double THUG_HEIGHT = 80;

    private final IMap map;

    public Spawner(IMap map) {
        this.map = map;
    }

    public boolean spawnThug() {
        var p = map.getPlayer().getBounds();

        double xOffset = ThreadLocalRandom.current().nextDouble(20, 1000);
        double yOffset = ThreadLocalRandom.current().nextDouble(20, 1000);

        Rectangle2D.Double bounds = new Rectangle2D.Double(
                p.getX() + xOffset,
                p.getY() + yOffset,
                THUG_WIDTH,
                THUG_HEIGHT
        );

        if (!map.getBounds().contains(bounds)) return false;
        if (map.getGrid().getCellFromPos(bounds).isBlocked()) return false;

        map.addEnemy(new Thug(bounds));
        return true;
    }
}
