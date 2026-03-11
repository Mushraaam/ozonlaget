package no.uib.inf112.map.npcs;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IMap;

public class Ghoul extends NPC {

    private static final double SPEED = Config.getInt("ghoulSpeed");
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private static final EnemyType ENEMY_TYPE = EnemyType.GHOUL;
    private static final int DAMAGE = 2;
    private static final int ANIMATION_COUNT = 8;
    private boolean hasRangedAmmo;
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

        this.hasRangedAmmo = false; // change to true when ranged attack implemented
        this.attackSlowDown = 0; // used to slow down attack animations
        this.meleeSwing = false;

    }

    @Override
    public void attack(Double target) {

        // TODO implement checks for ranged attacks

        if (!hasRangedAmmo) {
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
    }

}
