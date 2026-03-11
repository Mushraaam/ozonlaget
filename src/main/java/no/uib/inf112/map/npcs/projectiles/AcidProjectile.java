package no.uib.inf112.map.npcs.projectiles;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IProjectile;
import no.uib.inf112.map.npcs.projectiles.puddles.AcidPuddle;

public class AcidProjectile implements IProjectile {

    private double startX;
    private double startY;
    private double destX;
    private double destY;
    private IMap map;
    private Rectangle2D.Double bounds;
    private Rectangle2D.Double target;

    private static final PuddleType TYPE = PuddleType.ACID;
    private static final int SPEED = 10;
    private static final double WIDTH = 100;
    private static final double HEIGHT = 20;

    public AcidProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IMap map) {
        this.startX = startPos.getCenterX();
        this.startY = startPos.getCenterY();
        this.destX = endPos.getCenterX();
        this.destY = endPos.getCenterY();
        this.bounds = new Rectangle2D.Double(startX, startY, WIDTH, HEIGHT);
        this.target = endPos;
        this.map = map;
    }

    @Override
    public PuddleType getType() {
        return TYPE;
    }

    @Override
    public void move() {
        double dx = destX - startX;
        double dy = destY - startY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= SPEED) {
            map.addAOEPuddle(new AcidPuddle(target, map));
            map.removeProjectile(this);
            return;
        }

        // Normalize
        double dirX = dx / distance;
        double dirY = dy / distance;

        startX += dirX * SPEED;
        startY += dirY * SPEED;

        this.bounds = new Rectangle2D.Double(startX, startY, WIDTH, HEIGHT);
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
