package no.uib.inf112.core;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;

import no.uib.inf112.config.Config;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.npcs.Zombie;

public class Spawner {
    private static final double THUG_WIDTH = Config.getInt("thugWidth");
    private static final double THUG_HEIGHT = Config.getInt("thugHeight");

    private final IMap map;

    public Spawner(IMap map) {
        this.map = map;
    }

    public boolean spawnThug() {
        var p = map.getPlayer().getHitbox();

        double xOffset = ThreadLocalRandom.current().nextDouble(200, 500);
        double yOffset = ThreadLocalRandom.current().nextDouble(200, 500);

        Rectangle2D.Double hitbox = new Rectangle2D.Double(
                p.getX() + xOffset,
                p.getY() + yOffset,
                THUG_WIDTH,
                THUG_HEIGHT
        );
            ICell spawnCell = map.getGrid().getCellFromPos(hitbox);
            if (spawnCell == null || spawnCell.isBlocked()) return false;
            if (!map.getBounds().contains(hitbox)) return false;

        for(ICell cell : map.getGrid().getNeighboursAtDepth(spawnCell, 2)){
            for(IEnemy neighbor : cell.getEnemies()){
                if (hitbox.intersects(neighbor.getHitbox())) return false;
            }
        }

            map.addEnemy(new Zombie(hitbox, this.map));
            return true;
        }
}
