package no.uib.inf112.model.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.npcs.projectiles.ExplosionProjectile;
import no.uib.inf112.model.npcs.projectiles.MinionProjectile;

import java.awt.geom.Rectangle2D;

public class GigachadLV5 extends NPC {

    private static final double SPEED = Config.getInt("gigachadSpeed");
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.LARGE;
    private static final EnemyType ENEMY_TYPE = EnemyType.MEGABOSS;

    private static final int DAMAGE = 6;
    private static final int ANIMATION_COUNT = 8;

    //RANGES
    private static final int LONG_RANGE = 680;
    private static final int MEDIUM_RANGE = 450;
    private static final int AGGRO_RANGE = 700;

    private static final int ATTACK_DELAY = 8;
    private static final int RANGED_DELAY = 6;

    //COOLDOWNS
    private static final int FIREBALL_CD_MAX = 240;
    private int fireballCooldown = 0;

    private static final int MINION_CD_MAX = 600;
    private int minionCooldown = 0;

    private enum BossState { IDLE, FIREBALL, MINION }
    private BossState activeRangedState = BossState.IDLE;

    //Stopwatch
    private int attackTimer = 0;
    private int attackSlowDown = 0;
    private boolean meleeSwing = false;
    private boolean projectileFired = false;

    public GigachadLV5(Rectangle2D.Double pos, IModel map) {
        super(pos, map, Config.getInt("gigachadHP"));

        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
        setAggroRange(AGGRO_RANGE);

        this.hasRangedAmmo = true;
        this.range = LONG_RANGE;
    }

    @Override
    public void incrementAnimationIndex() {
        if (currentAction() == EnemyAction.RANGED_ATTACK ||
                currentAction() == EnemyAction.LONG_RANGED_ATTACK ||
                currentAction() == EnemyAction.ATTACK) {
            return; // handle its own frames
        }
        super.incrementAnimationIndex();
    }
    // =====================================================================

    @Override
    public void attack(Rectangle2D.Double target) {
        this.attackSlowDown = (this.attackSlowDown + 1) % ATTACK_DELAY;
        if (this.attackSlowDown == 0) {
            this.animationIndex = (this.animationIndex + 1) % ANIMATION_COUNT;
            this.meleeSwing = true;
        }
        if (this.animationIndex == 4 && this.meleeSwing) {
            if (this.player.getHitbox().intersects(this.attackTarget)) {
                this.player.takeDamage(DAMAGE);
            }
            this.meleeSwing = false;
        }
        if (this.animationIndex == 0 && !inMeleeRange()) {
            setAction(EnemyAction.WALK);
        }
    }

    @Override
    protected boolean canShootPlayer() {
        if (!super.canShootPlayer()) return false;

        double dist = distance(this.pos, this.player.getHitbox());
        boolean canFireball = (this.fireballCooldown <= 0 && dist <= MEDIUM_RANGE);
        boolean canMinion = (this.minionCooldown <= 0 && dist <= LONG_RANGE);

        if (canFireball || canMinion) {
            this.attackTimer = 0; // Fresh timer for a fresh attack
            return true;
        }
        return false;
    }

    @Override
    protected void rangedAttack(Rectangle2D.Double attackTarget) {
        if (this.attackTimer == 0) {
            this.projectileFired = false;
            double dist = distance(this.pos, this.player.getHitbox());

            if (this.minionCooldown <= 0 && (dist > MEDIUM_RANGE || this.fireballCooldown > 0)) {
                this.activeRangedState = BossState.MINION;
                setAction(EnemyAction.LONG_RANGED_ATTACK);
            } else {
                this.activeRangedState = BossState.FIREBALL;
            }
        }
        this.animationIndex = Math.min(7, this.attackTimer / RANGED_DELAY);

        if (this.animationIndex == 4 && !this.projectileFired && this.activeRangedState != BossState.IDLE) {
            Rectangle2D.Double pBox = this.player.getHitbox();
            Rectangle2D.Double targetBox = new Rectangle2D.Double(pBox.x, pBox.y, pBox.width, pBox.height);

            if (this.activeRangedState == BossState.MINION) {
                this.map.addProjectile(new MinionProjectile(this.pos, targetBox, this.map));
            } else {
                this.map.addProjectile(new ExplosionProjectile(this.pos, targetBox, this.map));
            }

            this.projectileFired = true;
        }

        this.attackTimer++;

        if (this.attackTimer >= ANIMATION_COUNT * RANGED_DELAY) {
            if (this.activeRangedState == BossState.MINION) {
                this.minionCooldown = MINION_CD_MAX;
                this.fireballCooldown = Math.max(this.fireballCooldown, 60);
            } else {
                this.fireballCooldown = FIREBALL_CD_MAX;
                this.minionCooldown = Math.max(this.minionCooldown, 60);
            }

            this.activeRangedState = BossState.IDLE;
            this.attackTimer = 0;
            this.projectileFired = false;
            this.animationIndex = 0;
            setAction(EnemyAction.WALK);
        }
    }

    @Override
    protected void dropLoot(){
        map.getItemFactory().dropSpecificItem(CollectableType.CHOPPER_KEYCARD, getHitbox());
    }

    @Override
    public void move(IGrid grid) {
        if (fireballCooldown > 0) fireballCooldown--;
        if (minionCooldown > 0) minionCooldown--;

        if (minionCooldown <= 0) {
            this.range = LONG_RANGE;
        } else if (fireballCooldown <= 0) {
            this.range = MEDIUM_RANGE;
        } else {
            this.range = 50;
        }

        if (currentAction() != EnemyAction.RANGED_ATTACK && currentAction() != EnemyAction.LONG_RANGED_ATTACK) {
            this.activeRangedState = BossState.IDLE;
            this.attackTimer = 0;
        }
        if (currentAction() == EnemyAction.LONG_RANGED_ATTACK) {
            rangedAttack(this.attackTarget);
            return;
        }

        super.move(grid);
    }

    @Override
    protected void setDeathAnimationIndex() {
        int elapsedTicks = 200 - this.deathDelay;
        int ticksPerFrame = 10;
        this.animationIndex = Math.min(13, elapsedTicks / ticksPerFrame);
    }

    @Override
    protected boolean isLegal(Rectangle2D.Double candidate) {
        double padding = 6.0;
        Rectangle2D.Double movementHitbox = new Rectangle2D.Double(
                candidate.x + padding, candidate.y + padding,
                candidate.width - (padding * 2), candidate.height - (padding * 2));
        if (movementHitbox.intersects(this.player.getHitbox())) return false;
        ICell candidateCell = map.getGrid().getCellFromPos(candidate);
        if (candidateCell == null || !map.getPathfinder().canEnter(candidateCell, SIZE)) return false;
        for (IEnemy enemy : map.getEnemies()) {
            if (enemy == this || !enemy.isAlive() || enemy.getEnemyType() == EnemyType.BUG) {
                continue; //dont squash is little friends
            }
            if (movementHitbox.intersects(enemy.getHitbox())) {
                if (enemy.size() != EnemySize.LARGE) enemy.takeDamage(9999);
                else return false;
            }
        }
        for (IVehicle vehicle : this.map.getVehicles()) {
            if (candidate.intersects(vehicle.getBounds())) return false;
        }
        return true;
    }
}