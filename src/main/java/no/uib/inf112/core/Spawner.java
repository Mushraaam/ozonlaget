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

            // OPTIMIZED CHECK: Only check enemies in the target cell
            // This is O(1) or O(small constant) instead of O(N)
            for (IEnemy neighborEnemy : map.getEnemiesAroundCell(spawnCell)) {
                if (hitbox.intersects(neighborEnemy.getHitbox())) {
                    return false; // Space is occupied!
                }
            }

            map.addEnemy(new Zombie(hitbox, this.map));
            return true;
        }
}
