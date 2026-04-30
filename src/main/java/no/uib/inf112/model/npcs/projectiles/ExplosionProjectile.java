package no.uib.inf112.model.npcs.projectiles;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.npcs.projectiles.puddles.ExplosionPuddle;

import java.awt.geom.Rectangle2D;

public class ExplosionProjectile extends PuddleProjectile {

    private Rectangle2D.Double target;
    private static final PuddleType TYPE = PuddleType.BOSS_FIREBALL;
    private static final int SPEED = 12;
    private static final double WIDTH =64;
    private static final double HEIGHT = 36;

    public ExplosionProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IModel map) {
        super(startPos, endPos, map, WIDTH, HEIGHT, SPEED, TYPE);
        this.target = endPos;
    }

    @Override
    protected void payload() {
        Rectangle2D.Double explosionArea = new Rectangle2D.Double(
                this.target.getX() - 60,
                this.target.getY() - 60,
                this.target.getWidth() + 120,
                this.target.getHeight() + 120);
        map.addAOEPuddle(new ExplosionPuddle(explosionArea, super.map, PuddleType.EXPLOSION ));

    }
}
