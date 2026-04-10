package no.uib.inf112.model.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IModel;

import java.awt.geom.Rectangle2D;

public class GigachadLV5 extends NPC {

    private static final double SPEED = 2;
    private static final double ROTATION_SPEED = 0.08;
    private static final EnemySize SIZE = EnemySize.LARGE;
    private static final EnemyType ENEMY_TYPE = EnemyType.MEGABOSS;

    private static final int DAMAGE = 6;
    private static final int ANIMATION_COUNT = 8;

    // --- RANGE ZONES ---
    private static final int LONG_RANGE = 600;
    private static final int MEDIUM_RANGE = 150;
    private static final int AGGRO_RANGE = 500;

    private static final int ATTACK_DELAY = 15;
    private static final int RANGED_DELAY = 20;

    private int attackSlowDown;
    private boolean meleeSwing;
    private boolean isDoingMediumAttack;
    private boolean projectileFired;

    public GigachadLV5(Rectangle2D.Double pos, IModel map, int health) {
        super(pos, map, health);

        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
        setAggroRange(AGGRO_RANGE);

        this.hasRangedAmmo = true;
        this.range = LONG_RANGE;

        this.attackSlowDown = 0;
        this.meleeSwing = false;
        this.projectileFired = false;
    }

    @Override
    public void attack(Rectangle2D.Double target) {
        this.attackSlowDown = (this.attackSlowDown + 1) % ATTACK_DELAY;

        if (this.attackSlowDown == 0) {
            this.incrementAnimationIndex();
            this.meleeSwing = true;
        }

        // mid swing deals the damage
        if (this.animationIndex == 4 && this.meleeSwing) {
            Rectangle2D.Double playerPos = this.player.getHitbox();
            if (playerPos.intersects(this.attackTarget)) {
                this.player.takeDamage(DAMAGE);
            }
            this.meleeSwing = false;
        }

        // walk again
        if (this.animationIndex == 0 && !inMeleeRange()) {
            setAction(EnemyAction.WALK);
        }
    }

    @Override
    protected void rangedAttack(Rectangle2D.Double attackTarget) {
        this.attackSlowDown = (this.attackSlowDown + 1) % RANGED_DELAY;

        if (this.attackSlowDown == 0) {

            if (this.animationIndex == 0) {
                double dist = distance(this.pos, this.player.getHitbox());
                this.isDoingMediumAttack = (dist <= MEDIUM_RANGE);
                this.projectileFired = false;
            }

            this.incrementAnimationIndex();
        }

        if (this.animationIndex == 4 && !this.projectileFired) {

            Rectangle2D.Double playerPos = this.player.getHitbox();

            if (this.isDoingMediumAttack) {
                // TODO: Add Medium Projectile
                // this.map.addProjectile(new MediumBossProjectile(this.pos, playerPos, this.map));
                System.out.println("Gigachad used Medium Attack!");
            } else {
                // TODO: Add Long Range Projectile
                // this.map.addProjectile(new LongBossProjectile(this.pos, playerPos, this.map));
                System.out.println("Gigachad used Long Range Attack!");
            }

            this.projectileFired = true;
        }


        if (this.animationIndex == 7) {
            this.hasRangedAmmo = false;
            setAction(EnemyAction.WALK);
        }
    }
}