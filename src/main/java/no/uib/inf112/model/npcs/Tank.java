package no.uib.inf112.model.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IModel;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;


public class Tank extends NPC {

    private static final double SPEED = Config.getInt("tankSpeed");
    private static final double ROTATION_SPEED = 0.08;
    private static final EnemySize SIZE = EnemySize.LARGE;
    private static final EnemyType ENEMY_TYPE = EnemyType.TANK;
    private static final int DAMAGE = 8;
    private static final int ANIMATION_COUNT = 8;
    private static final int AGGRO_RANGE = 300;
    private int attackSlowDown;
    private boolean meleeSwing;

    public Tank(Double pos, IModel map) {
        super(pos, map, Config.getInt("tankHP"));
        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
        setAggroRange(AGGRO_RANGE);

        super.hasRangedAmmo = false;
        this.attackSlowDown = 0; // used to slow down attack animations
        this.meleeSwing = false;
        this.map = map;

    }

    @Override
    public void attack(Double target) {
        this.attackSlowDown = (this.attackSlowDown + 1) % 15;

        if (this.attackSlowDown == 0) {
            this.incrementAnimationIndex();
            this.meleeSwing = true;
        }
        if (this.animationIndex == 5 && this.meleeSwing) {
            Rectangle2D.Double playerPos = this.player.getHitbox();
            if (playerPos.intersects(this.attackTarget)) {
                this.player.takeDamage(DAMAGE);
                
            }
            this.meleeSwing = false;
        }

        if (this.animationIndex == 0 && !inMeleeRange()) {
            setAction(EnemyAction.WALK);
        }
    }

    @Override
    protected void rangedAttack(Double attackTarget2) {
        // Tank has no ranged attack
    }
    
}
