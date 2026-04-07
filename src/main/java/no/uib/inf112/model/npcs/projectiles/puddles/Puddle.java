package no.uib.inf112.model.npcs.projectiles.puddles;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IPuddle;

public class Puddle implements IPuddle {

    public static final int LIFETIME = 200;
    public Rectangle2D.Double bounds;
    public int animationIndex;
    public IModel map;
    private PuddleType type;
    private IPlayer player;
    private int damage;

    public Puddle(Rectangle2D.Double bounds, IModel map, PuddleType type, int damage){
        this.bounds = bounds;
        this.map = map;
        this.animationIndex = 0;
        this.type = type;
        this.player = this.map.getPlayer();
        this.damage = damage;
    }

    @Override
    public Double getBounds() {
        return bounds;
    }

    @Override
    public void incrementAnimationIndex() {
        this.animationIndex++;
        if (this.animationIndex >= LIFETIME){
            this.map.removeAOEPuddle(this);
        }
    }

    @Override
    public int getAnimationIndex() {
        return this.animationIndex;
    }

    @Override
    public PuddleType getType() {
        return this.type;
    }

    @Override
    public void dealDamage() {
        if (this.bounds.intersects(this.player.getHitbox())){
            this.player.takeDamage(this.damage);
        }
    }

    @Override
    public int lifeTime() {
        return LIFETIME;
    }
    
}
