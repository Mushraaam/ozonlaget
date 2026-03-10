package no.uib.inf112.core;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.npcs.Ghoul;
import no.uib.inf112.map.npcs.Zombie;

public class Spawner {
    private static final double GHOUL_WIDTH = Config.getInt("smallEnemy");
    private static final double GHOUL_HEIGHT = Config.getInt("smallEnemy");

    private static final double ZOMBIE_WIDTH = Config.getInt("largeEnemy");
    private static final double ZOMBIE_HEIGHT = Config.getInt("largeEnemy");

    private final IMap map;

    public Spawner(IMap map) {
        this.map = map;
    }

    public boolean spawnGhoul() {
        double x = ThreadLocalRandom.current().nextDouble(100, map.getBounds().getMaxX() - 100);
        double y = ThreadLocalRandom.current().nextDouble(100, map.getBounds().getMaxY() - 100);

        Rectangle2D.Double hitbox = new Rectangle2D.Double(
                x, y,
                GHOUL_WIDTH,
                GHOUL_HEIGHT);
        ICell spawnCell = map.getGrid().getCellFromPos(hitbox);
        if (spawnCell == null || (spawnCell.pathType() == PathType.BLOCKED))
            return false;
        if (!map.getBounds().contains(hitbox))
            return false;

        for (IEnemy neighbor : map.getEnemies()) {
            if (hitbox.intersects(neighbor.getHitbox()))
                return false;
        }

        map.addEnemy(new Ghoul(hitbox, this.map));
        return true;
    }

    public boolean spawnZombie() {

        double x = ThreadLocalRandom.current().nextDouble(100, map.getBounds().getMaxX() - 100);
        double y = ThreadLocalRandom.current().nextDouble(100, map.getBounds().getMaxY() - 100);

        Rectangle2D.Double hitbox = new Rectangle2D.Double(
                x, y,
                ZOMBIE_WIDTH,
                ZOMBIE_HEIGHT);

        if (!map.getBounds().contains(hitbox))
            return false;
        ICell spawnCell = map.getGrid().getCellFromPos(hitbox);
        if (spawnCell == null || (spawnCell.pathType() == PathType.BLOCKED))
            return false;
        if (!map.getBounds().contains(hitbox))
            return false;

        for (IEnemy neighbor : map.getEnemies()) {
            if (hitbox.intersects(neighbor.getHitbox()))
                return false;
        }

        map.addEnemy(new Zombie(hitbox, this.map));
        return true;
    }
}
