package no.uib.inf112.map.npcs.factory;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.map.npcs.Ghoul;

public class SpawnPoint {
    private Rectangle2D.Double bounds;
    private IMap map;
    private Random random;

    private static final int SMALL = Config.getInt("smallEnemy");
    private static final int MEDIUM = Config.getInt("mediumEnemy");
    private static final int LARGE = Config.getInt("largeEnemy");

    /* Ensures no zombies spawn in view of / near player */
    private static final double SAFE_ZONE = Math.ceil(Math.hypot(Config.getInt("screenWidth"), Config.getInt("screenHeight")));

    public SpawnPoint(IMap map, Rectangle2D.Double bounds) {
        this.map = map;
        this.bounds = bounds;
        this.random = new Random();
    }

    public Rectangle2D.Double bounds(){
        return this.bounds;
    }

    public boolean spawnEnemy(EnemyType type) {
        IPlayer player = map.getPlayer();
        if (player == null) {
            return false;
        }

        if (distance(this.bounds, player.getHitbox()) <= SAFE_ZONE) {
            return false;
        }

        // try to spawn 10 times, break if failed 10 times or success
        for (int i = 0; i < 10; i++) {
            if (createEnemy(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean createEnemy(EnemyType type) {
        Rectangle2D.Double hitBox;

        switch (type) {
            case GHOUL -> {
                hitBox = new Rectangle2D.Double(
                        random.nextDouble(this.bounds.getMinX(), this.bounds.getMaxX() - MEDIUM),
                        random.nextDouble(this.bounds.getMinY(), this.bounds.getMaxY() - MEDIUM),
                        MEDIUM, MEDIUM);
            }

            default -> throw new IllegalArgumentException("Unknown EnemyType");
        }

        if (isLegal(hitBox)) {
            addEnemy(type, hitBox);
            return true;
        }
        return false;
    }

    private void addEnemy(EnemyType type, Double hitBox) {
        switch (type) {
            case GHOUL -> {
                this.map.addEnemy(new Ghoul(hitBox, map));
            }

            default -> throw new IllegalArgumentException("Unknown EnemyType");
        }
    }

    private boolean isLegal(Double hitBox) {
        if (hitBox == null) {
            return false;
        }
        for (IEnemy enemy : this.map.getEnemies()) {
            if (hitBox.intersects(enemy.getHitbox())) {
                return false;
            }
        }
        for (IStaticObject obj : this.map.getStaticObjects()) {
            if (hitBox.intersects(obj.getBounds())) {
                return false;
            }
        }
        return true;
    }

    private double distance(Rectangle2D.Double from, Rectangle2D.Double to) {
        double dx = from.getCenterX() - to.getCenterX();
        double dy = from.getCenterY() - to.getCenterY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}
