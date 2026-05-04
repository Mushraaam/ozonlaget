package no.uib.inf112.model.npcs.factory;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IVehicle;
import no.uib.inf112.model.npcs.Ghoul;
import no.uib.inf112.model.npcs.GigachadLV5;
import no.uib.inf112.model.npcs.Sprinter;
import no.uib.inf112.model.npcs.BigHands;
import no.uib.inf112.model.npcs.Tank;

public class SpawnPoint {
    private Rectangle2D.Double bounds;
    private IModel map;
    private Random random;

    private static final int SMALL = Config.getInt("smallEnemy");
    private static final int MEDIUM = Config.getInt("mediumEnemy");
    private static final int LARGE = Config.getInt("largeEnemy");
    private static final int HUGE = Config.getInt("hugeEnemy");

    /* Ensures no zombies spawn in view of / near player */
    private static final double SAFE_ZONE = Math
            .ceil(Math.hypot(Config.getInt("screenWidth"), Config.getInt("screenHeight")) * 0.7);

    public SpawnPoint(IModel map, Rectangle2D.Double bounds) {
        this.map = map;
        this.bounds = bounds;
        this.random = new Random();
    }

    public Rectangle2D.Double bounds() {
        return this.bounds;
    }

    public boolean spawnEnemy(EnemyType type) {

        IPlayer player = map.getPlayer();
        if (player == null) {
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
            case BIGHANDS -> {
                hitBox = new Rectangle2D.Double(
                        random.nextDouble(this.bounds.getMinX(), this.bounds.getMaxX() - MEDIUM),
                        random.nextDouble(this.bounds.getMinY(), this.bounds.getMaxY() - MEDIUM),
                        MEDIUM, MEDIUM);
            }
            case SPRINTER -> {
                hitBox = new Rectangle2D.Double(
                        random.nextDouble(this.bounds.getMinX(), this.bounds.getMaxX() - SMALL),
                        random.nextDouble(this.bounds.getMinY(), this.bounds.getMaxY() - SMALL),
                        SMALL, SMALL);
            }
            case MEGABOSS -> {
                double centerX = 1080;
                double centerY = 1080;
                hitBox = new Rectangle2D.Double(centerX, centerY, HUGE, HUGE);
                if (isLegal(hitBox)) {
                    addEnemy(type, hitBox);
                    return true;
                }
            }
            case TANK -> {
                hitBox = new Rectangle2D.Double(
                        random.nextDouble(this.bounds.getMinX(), this.bounds.getMaxX() - LARGE),
                        random.nextDouble(this.bounds.getMinY(), this.bounds.getMaxY() - LARGE),
                        LARGE, LARGE);
            }

            default -> throw new IllegalArgumentException("Unknown EnemyType");
        }

        IPlayer player = map.getPlayer();
        if (player == null) {
            return false;
        }
        double requiredSafeZone = (type == EnemyType.MEGABOSS) ? SAFE_ZONE / 2 : SAFE_ZONE;
        if (distance(hitBox, player.getHitbox()) <= requiredSafeZone) {
            return false;
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
            case SPRINTER -> {
                this.map.addEnemy(new Sprinter(hitBox, map));
            }
            case MEGABOSS -> {
                this.map.addEnemy(new GigachadLV5(hitBox, map));
            }
            case BIGHANDS -> {
                this.map.addEnemy(new BigHands(hitBox, map));
            }
            case TANK -> {
                this.map.addEnemy(new Tank(hitBox, map));
            }

            default -> throw new IllegalArgumentException("Unknown EnemyType");
        }
    }

    private boolean isLegal(Double hitBox) {
        if (hitBox == null) {
            return false;
        }

        if (hitBox.intersects(this.map.getPlayer().getHitbox())){
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

        // Check vehicle collision
        for (IVehicle vehicle : this.map.getVehicles()) {
            if (hitBox.intersects(vehicle.getBounds())) {
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
