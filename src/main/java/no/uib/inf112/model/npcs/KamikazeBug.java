package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.npcs.projectiles.puddles.ExplosionPuddle;

import java.awt.geom.Rectangle2D;

public class KamikazeBug extends NPC {

    private static final double SPEED = 3.5;
    private static final int HP = 50;
    private static final EnemySize SIZE = EnemySize.SMALL;
    private static final EnemyType ENEMY_TYPE = EnemyType.BUG;

    public KamikazeBug(Rectangle2D.Double pos, IModel map) {
        super(pos, map, HP);
        setSpeed(SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(9);
        setAggroRange(1000);
        this.aggroed = true; // Instantly mad when it hatches
    }

    @Override
    public void attack(Rectangle2D.Double target) {
        // Instead of swinging, it instantly explodes
        Rectangle2D.Double boomArea = new Rectangle2D.Double(
                this.pos.x - 40, this.pos.y - 40,
                this.pos.width + 80, this.pos.height + 80
        );
        this.map.addAOEPuddle(new ExplosionPuddle(boomArea, this.map, PuddleType.GASEXPLOSION ));
        this.takeDamage(9999);
        this.map.removeEnemy(this);
    }

    @Override
    protected void rangedAttack(Rectangle2D.Double attackTarget) {
        // Bugs don't shoot!
    }

    @Override
    public void move(IGrid grid) {
        super.move(grid);
        if (this.isAlive() && this.getHitbox().intersects(this.player.getHitbox())) {

            Rectangle2D.Double boomArea = new Rectangle2D.Double(
                    this.pos.x - 40, this.pos.y - 40,
                    this.pos.width + 80, this.pos.height + 80
            );

            this.map.addAOEPuddle(new ExplosionPuddle(boomArea, this.map, PuddleType.GASEXPLOSION));
            this.takeDamage(9999);
        }
    }
}