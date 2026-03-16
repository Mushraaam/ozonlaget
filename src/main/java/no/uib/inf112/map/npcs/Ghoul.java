package no.uib.inf112.map.npcs;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.npcs.projectiles.AcidPuddleProjectile;

public class Ghoul extends NPC {

    private static final double SPEED = Config.getInt("ghoulSpeed");
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private static final EnemyType ENEMY_TYPE = EnemyType.GHOUL;
    private static final int DAMAGE = 2;
    private static final int ANIMATION_COUNT = 8;
    private static final int RANGE = 200;
    private static final int RANGED_DELAY = 20;
    private static final int AGGRO_RANGE = 450;
    private IMap map;
    private int attackSlowDown;
    private boolean meleeSwing;

    public Ghoul(Double pos, IMap map) {
        super(pos, map, Config.getInt("ghoulHP"));
        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
        setAggroRange(AGGRO_RANGE);

        super.hasRangedAmmo = true; // change to true when ranged attack implemented
        this.attackSlowDown = 0; // used to slow down attack animations
        this.meleeSwing = false;
        this.range = RANGE;
        this.map = map;

    }

    @Override
    public void attack(Double target) {


        this.attackSlowDown = (this.attackSlowDown + 1) % 10;

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
        this.attackSlowDown = (this.attackSlowDown + 1) % RANGED_DELAY;

        if (this.attackSlowDown == 0) {
            this.incrementAnimationIndex();

        }
        if (this.animationIndex == 4) {
            Rectangle2D.Double playerPos = this.player.getHitbox();
            Rectangle2D.Double puddlePos = new Rectangle2D.Double(
                playerPos.x - 30, playerPos.y - 30, playerPos.getWidth() + 60, playerPos.getHeight() + 60);
            
            this.map.addProjectile(new AcidPuddleProjectile(this.pos, puddlePos, this.map));

            this.hasRangedAmmo = false;
            setAction(EnemyAction.WALK);
        }
    }

}
