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
        var p = map.getPlayer().getHitbox();

        double xOffset = ThreadLocalRandom.current().nextDouble(200, 500);
        double yOffset = ThreadLocalRandom.current().nextDouble(200, 500);

        Rectangle2D.Double hitbox = new Rectangle2D.Double(
                p.getX() + xOffset,
                p.getY() + yOffset,
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
        var p = map.getPlayer().getHitbox();

        double xOffset = ThreadLocalRandom.current().nextDouble(200, 500);
        double yOffset = ThreadLocalRandom.current().nextDouble(200, 500);

        Rectangle2D.Double hitbox = new Rectangle2D.Double(
                p.getX() + xOffset,
                p.getY() + yOffset,
                ZOMBIE_WIDTH,
                ZOMBIE_HEIGHT);
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
