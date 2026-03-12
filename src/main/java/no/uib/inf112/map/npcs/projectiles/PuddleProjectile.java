package no.uib.inf112.map.npcs.projectiles;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IProjectile;

public abstract class PuddleProjectile implements IProjectile {

    private double startX;
    private double startY;
    private double destX;
    private double destY;
    private Rectangle2D.Double bounds;

    private double width;
    private double height;
    private int speed;
    private PuddleType type;

    protected IMap map;

    public PuddleProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IMap map, double width,
            double height, int speed, PuddleType type) {
        this.startX = startPos.getCenterX();
        this.startY = startPos.getCenterY();
        this.destX = endPos.getCenterX();
        this.destY = endPos.getCenterY();
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.bounds = new Rectangle2D.Double(startX - this.width * 0.5, startY - this.height * 0.5,
                this.width, this.height);

        this.map = map;
        this.type = type;
    }

    // Abstract func

    /**
     * Defines the effect that happens when projectile reaches its target
     */
    protected abstract void payload();

    @Override
    public PuddleType getType() {
        return this.type;
    }

    @Override
    public void move() {
        double dx = destX - startX;
        double dy = destY - startY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= this.speed) {
            payload();
            map.removeProjectile(this);
            return;
        }

        // Normalize
        double dirX = dx / distance;
        double dirY = dy / distance;

        startX += dirX * this.speed;
        startY += dirY * this.speed;

        this.bounds = new Rectangle2D.Double(startX - this.bounds.width * 0.5, startY - this.bounds.height * 0.5,
                this.width, this.height);
    }

    @Override
    public Double angle() {
        double dx = this.destX - this.startX;
        double dy = this.destY - this.startY;
        return Math.atan2(dy, dx);
    }

    @Override
    public java.awt.geom.Rectangle2D.Double getBounds() {
        return this.bounds;
    }
}
