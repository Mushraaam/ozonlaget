package no.uib.inf112.model.npcs.projectiles.puddles;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IModel;

import java.awt.geom.Rectangle2D;

public class ExplosionPuddle extends Puddle {
    private static final int DAMAGE = 12;
    private static final int EXPLOSION_LIFE = 40;

    private boolean hasDealtDamage = false;

    public ExplosionPuddle(Rectangle2D.Double bounds, IModel map, PuddleType type) {
        super(bounds, map, type, DAMAGE);
    }

    @Override
    public void dealDamage() {

        if (!hasDealtDamage && this.animationIndex >= 1) {
            map.getCamera().startShake(25, 8);
            if (this.bounds.intersects(this.map.getPlayer().getHitbox())) {
                this.map.getPlayer().takeDamage(DAMAGE);
                map.getCamera().startShake(25, 8);
            }
            hasDealtDamage = true;
        }
    }

    @Override
    public int lifeTime() {
        return EXPLOSION_LIFE;
    }
}
