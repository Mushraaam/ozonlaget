package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IVehicle;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class NPC implements IEnemy {
    private double speed;
    private List<ICell> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int animationCount;
    private double facingAngle = 0.0;
    private double rotationSpeed = 0.12;
    private EnemySize size;
    private EnemyType type;

    private ICell lastStart;
    private ICell lastGoal;
    private IEnemy nextInCell = null;
    private ICell from;
    private EnemyAction currentAction;
    private boolean moving;

    private int lastMinR = -1;
    private int lastMaxR = -1;
    private int lastMinC = -1;
    private int lastMaxC = -1;

    // test
    private boolean sliding = false;

    private int health;
    private int maxHealth;

    // Wander
    private IGrid grid;
    private Random random;
    private int wanderDelay;
    private int aggroRange;
    private ICell wanderGoal;



    // Protected variables
    protected boolean aggroed;
    protected IModel map;
    protected IPlayer player;
    protected Rectangle2D.Double attackTarget;
    protected int animationIndex = 0;
    protected boolean hasRangedAmmo;
    protected int range = 0;
    protected Rectangle2D.Double pos;
    // Dying
    protected int deathDelay;

    protected NPC(Rectangle2D.Double pos, IModel map, int health) {
        this.pos = pos;
        this.map = map;

        this.player = map.getPlayer();
        this.grid = this.map.getGrid();
        this.maxHealth = health;
        this.health = health;
        this.currentAction = EnemyAction.WALK;
        this.moving = false;

        // Wander
        this.aggroed = false;
        this.random = new Random();
        this.wanderDelay = 0;
        this.wanderGoal = this.grid.getCellFromPos(this.pos);

        // Dying
        this.deathDelay = 200;

    }

    private void wander() {
        ICell current = this.grid.getCellFromPos(this.pos);
        if (current == null) {
            return;
        }

        List<ICell> nearbyCells = this.grid.getNearbyCells(this.pos, 400);
        if (nearbyCells == null || nearbyCells.isEmpty()) {
            return;
        }

        // try to find a legal wandering goal
        for (int attempts = 0; attempts < 20; attempts++) {
            ICell cell = nearbyCells.get(this.random.nextInt(nearbyCells.size()));

            if (cell.equals(current) || !map.getPathfinder().canEnter(cell, size)) {
                continue;
            }

            this.wanderGoal = cell;
            return;
        }

        this.wanderGoal = null;
    }

    @Override
    public Ellipse2D.Double getTrueHitbox() {
        return null;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds,
            boolean fromController) {
        if (!this.aggroed) {
            checkAggro();
        }

        if (!this.aggroed && fromController) {

            if ((this.wanderGoal == null || this.wanderDelay % 5 == 0) && pathIndex >= currentPath.size()) {
                this.wanderDelay = (this.wanderDelay + 1) % 1000;
                wander();
            }

            if (this.wanderGoal == null) {
                return;
            }
            if ((this.wanderDelay % 1000 != 0)) {
                this.wanderDelay = (this.wanderDelay + 1) % 1000;

            }
            targetBounds = this.wanderGoal.getBounds();
        }

        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal = grid.getCellFromPos(targetBounds);

        if (start == null || goal == null) {
            return;
        }

        if (start.equals(lastStart) && goal.equals(lastGoal) && currentPath != null && !currentPath.isEmpty()) {
            return;
        }

        lastStart = start;
        lastGoal = goal;

        currentPath = pathfinder.findPath(this, start, goal, size, currentPath);
        pathIndex = (currentPath.size() > 1) ? 1 : 0;
    }

    private void updateFacing(double dx, double dy, double dist) { // noe assistanse med matten trengtes.....
        if (dist > 0.1) {
            double targetAngle = Math.atan2(dy, dx);
            double angleDiff = targetAngle - this.facingAngle;

            while (angleDiff < -Math.PI)
                angleDiff += 2 * Math.PI;
            while (angleDiff > Math.PI)
                angleDiff -= 2 * Math.PI;
            this.facingAngle += angleDiff * rotationSpeed;

        }
    }

    @Override
    public void incrementAnimationIndex() {

        if (this.currentAction == EnemyAction.WALK && !this.moving) {
            return;
        }
        if (!this.moving) {
            return;
        }
        this.animationIndex = (this.animationIndex + 1) % animationCount;
    }

    @Override
    public ICell getOldCell() {
        return this.from;
    }

    @Override
    public void move(IGrid grid) {

        if (this.map.getGameState() != GameState.ACTIVE_GAME) {
            this.moving = false;
            return;
        }

        this.moving = false;

        if (this.currentAction == EnemyAction.DEAD) {
            this.deathDelay--;
            setDeathAnimationIndex();
            if (this.deathDelay <= 0) {
                this.map.removeEnemy(this);
                map.getItemFactory().rollDropFromTable(this.getHitbox());

            }
            return;

        }
        
        // Continue ongoing attacks
        if (this.currentAction == EnemyAction.ATTACK) {
            this.moving = true;
            attack(this.attackTarget);
            return;
        }

        if (this.currentAction == EnemyAction.RANGED_ATTACK) {
            this.moving = true;
            rangedAttack(this.attackTarget);
            return;
        }

        // Start new attacks if in range

        if (this.aggroed) {
            if (canShootPlayer()) {
                this.currentAction = EnemyAction.RANGED_ATTACK;
                this.animationIndex = 0;
                this.attackTarget = this.player.getHitbox();
                rangedAttack(this.attackTarget);
                return;
            } else if (inMeleeRange()) {
                this.currentAction = EnemyAction.ATTACK;
                this.animationIndex = 0;
                this.attackTarget = this.player.getHitbox();
                this.moving = true;
                attack(this.attackTarget);
                return;
            }
        }

        // Walk or wander if not attacking

        this.currentAction = EnemyAction.WALK;

        this.from = getStandingCell();
        if (currentPath == null || pathIndex >= currentPath.size()) {
            return;
        }

        int lookAheadLimit = Math.min(currentPath.size(), pathIndex + 3);
        for (int i = pathIndex; i < lookAheadLimit; i++) {
            Rectangle2D target = currentPath.get(i).getBounds();
            double dx = target.getCenterX() - pos.getCenterX();
            double dy = target.getCenterY() - pos.getCenterY();
            double dist = Math.hypot(dx, dy);

            if (tryMove(dx, dy, dist, target)) {
                this.moving = true;
                updateFacing(dx, dy, dist);
                this.pathIndex = i;
                if (dist <= speed) {
                    pathIndex++;
                }
                break;
            }
        }
        // Recalculate route if wandering but unable to move
        if (!this.moving && !this.aggroed) {
            this.wanderGoal = null;
            this.currentPath = new ArrayList<>();
            this.pathIndex = 0;
            this.lastStart = null;
            this.lastGoal = null;
            wander();
        }

    }

    protected void setDeathAnimationIndex() {
        if (this.deathDelay > 190) {
            this.animationIndex = 0;
        } else if (this.deathDelay > 180) {
            this.animationIndex = 1;
        } else if (this.deathDelay > 170) {
            this.animationIndex = 2;
        } else if (this.deathDelay > 160) {
            this.animationIndex = 3;
        } else if (this.deathDelay > 150) {
            this.animationIndex = 4;
        } else {
            this.animationIndex = 5;
        }
    }

    private void checkAggro() {
        if (this.health < this.maxHealth){
            this.aggroed = true; //we aggro if we take damage
            return;
        }

        double dist = distance(this.pos, this.player.getHitbox());
        if (dist <= this.aggroRange && hasLineOfSight()) {
            this.aggroed = true;
        }
    }


    private void unStuck(boolean moveX, boolean moveY) {
        ICell myCell = this.getStandingCell();
        if (myCell == null) {
            return;
        }

        checkCellAndPush(moveX, moveY);
        for (ICell neighbor : map.getGrid().getNeighboursAtDepth(myCell, 2)) {
            if (neighbor != null) {
                checkCellAndPush(moveX, moveY);
            }
        }
    }

    private void checkCellAndPush(boolean moveX, boolean moveY) {

        for (IEnemy other : map.getEnemies()) {
            if (other == this) {
                continue;
            }

            Rectangle2D otherBox = other.getHitbox();
            if (this.pos.intersects(otherBox)) {
                double dx = this.pos.getCenterX() - otherBox.getCenterX();
                double dy = this.pos.getCenterY() - otherBox.getCenterY();

                if (dx == 0 && dy == 0) {
                    dx = Math.random() - 0.5;
                    dy = Math.random() - 0.5;
                }

                Rectangle2D.Double pushed = new Rectangle2D.Double(pos.x, pos.y, pos.width, pos.height);

                if (moveX)
                    pushed.x += Math.signum(dx) * 0.5;
                if (moveY)
                    pushed.y += Math.signum(dy) * 0.5;

                if (!pushed.intersects(player.getHitbox()) &&
                        map.getPathfinder().canEnter(map.getGrid().getCellFromPos(pushed), size)) {
                    this.pos = pushed;
                } else if (moveX && moveY) {
                    unStuck(true, false);
                    unStuck(false, true);
                    return;
                }
            }
        }
    }

    private boolean tryMove(double dx, double dy, double dist, Rectangle2D target) {
        Rectangle2D.Double candidate = generateCandidate(dx, dy, dist, target);

        if (isLegal(candidate)) {
            this.sliding = false;
            this.pos = candidate;
            return true;

        } else {
            int slideXDir = 1;
            int slideYDir = 1;

            if (!this.sliding) {
                slideXDir = (dx >= 0) ? 1 : -1;
                slideYDir = (dy >= 0) ? 1 : -1;

                Rectangle2D.Double testX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width,
                        this.pos.height);
                Rectangle2D.Double testY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width,
                        this.pos.height);
                testX.x += slideXDir * speed;
                testY.y += slideYDir * speed;
            }

            if (trySlide(dx, dy, dist, target)) {
                sliding = true;
                return true;
            }
        }
        this.sliding = false;
        return false;
    }

    private boolean trySlide(double dx, double dy, double dist, Rectangle2D target) {
        Rectangle2D.Double slideX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        Rectangle2D.Double slideY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

        // Slide X
        if (dist <= speed) {
            slideX.x = target.getCenterX() - pos.width / 2.0;
        } else {
            slideX.x += (dx / dist) * speed;
        }
        if (isLegal(slideX)) {
            this.pos = slideX;
            return true;

        } else { // Slide Y
            if (dist <= speed) {
                slideY.y = target.getCenterY() - pos.height / 2.0;
            } else {
                slideY.y += (dy / dist) * speed;
            }
            if (isLegal(slideY)) {
                this.pos = slideY;
                return true;
            } else {
                /* Do nothing */
            }
        }

        return false;

    }

    private Rectangle2D.Double generateCandidate(double dx, double dy, double dist, Rectangle2D target) {

        Rectangle2D.Double candidate = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        if (dist <= speed) {
            candidate.x = target.getCenterX() - pos.width / 2.0;
            candidate.y = target.getCenterY() - pos.height / 2.0;
        } else {
            candidate.x += (dx / dist) * speed;
            candidate.y += (dy / dist) * speed;
        }
        return candidate;

    }

    private boolean isLegal(Rectangle2D.Double candidate) {
        double padding = 6.0;
        Rectangle2D.Double movementHitbox = new Rectangle2D.Double(
                candidate.x + padding,
                candidate.y + padding,
                candidate.width - (padding * 2),
                candidate.height - (padding * 2));

        if (movementHitbox.intersects(this.player.getHitbox())) {
            return false;
        }

        ICell candidateCell = map.getGrid().getCellFromPos(candidate);
        if (candidateCell == null) {
            return false;
        }

        if (!map.getPathfinder().canEnter(candidateCell, size)) {
            return false;
        }

        for (IEnemy enemy : map.getEnemies()) {
            if (enemy == this || !enemy.isAlive()) {
                continue;
            }

            Rectangle2D enemyHitbox = enemy.getHitbox();
            double shrinkFactor = 0.8;

            double coreW = enemyHitbox.getWidth() * shrinkFactor;
            double coreH = enemyHitbox.getHeight() * shrinkFactor;
            double coreX = enemyHitbox.getCenterX() - (coreW / 2);
            double coreY = enemyHitbox.getCenterY() - (coreH / 2);

            Rectangle2D enemyCore = new Rectangle2D.Double(coreX, coreY, coreW, coreH);
            if (movementHitbox.intersects(enemyCore)) {
                return false;
            }
        }

        // Check vehicle collision
        for (IVehicle vehicle : this.map.getVehicles()) {
            if (candidate.intersects(vehicle.getBounds())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean boundsChanged(int minR, int maxR, int minC, int maxC) {
        return minR != lastMinR || maxR != lastMaxR || minC != lastMinC || maxC != lastMaxC;
    }

    @Override
    public void updateBounds(int minR, int maxR, int minC, int maxC) {
        this.lastMinR = minR;
        this.lastMaxR = maxR;
        this.lastMinC = minC;
        this.lastMaxC = maxC;
    }

    // //////////////////GETTERS////////////////////////

    @Override
    public int getLastMinR() {
        return lastMinR;
    }

    @Override
    public int getLastMaxR() {
        return lastMaxR;
    }

    @Override
    public int getLastMinC() {
        return lastMinC;
    }

    public int getLastMaxC() {
        return lastMaxC;
    }

    public IEnemy getNextInCell() {
        return this.nextInCell;
    }

    @Override
    public void setNextInCell(IEnemy next) {
        this.nextInCell = next;
    }

    @Override
    public ICell getStandingCell() {
        return this.map.getGrid().getCellFromPos(getHitbox());
    }

    public List<ICell> getCurrentPath() {
        return this.currentPath;
    }

    public double getFacingAngle() {
        return this.facingAngle;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.pos;
    }

    @Override
    public EnemyType getEnemyType() {
        return this.type;
    }

    @Override
    public int getAnimationIndex() {
        return animationIndex;
    }

    @Override
    public EnemySize size() {
        return this.size;
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health <= 0) {
            this.currentAction = EnemyAction.DEAD;
            this.animationIndex = 0;
            player.increaseKillCount();
        }
    }

    @Override
    public EnemyAction currentAction() {
        return this.currentAction;
    }

    @Override
    public void setAction(EnemyAction action) {
        this.currentAction = action;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    // CONSTRUCTOR SETTERS
    protected void setSpeed(double speed) {
        this.speed = speed;
    }

    protected void setRotationSpeed(double rspeed) {
        this.rotationSpeed = rspeed;
    }

    protected void setSize(EnemySize size) {
        this.size = size;
    }

    protected void setEnemyType(EnemyType type) {
        this.type = type;
    }

    protected void setAnimationCount(int count) {
        this.animationCount = count;
    }

    /**
     * Sets animationCount and sets animationIndex to 0
     * 
     * @param count
     */
    protected void setAttackAnimationCount(int count) {
        this.animationCount = count;
        this.animationIndex = 0;
    }

    protected double distance(Rectangle2D.Double source, Rectangle2D.Double target) {
        double dx = target.getCenterX() - source.getCenterX();
        double dy = target.getCenterY() - source.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected boolean inMeleeRange() {
        double w = this.pos.width;
        return distance(this.pos, this.player.getHitbox()) <= w;
    }

    protected boolean canShootPlayer() {
        return inShootingRange() && hasLineOfSight() && this.hasRangedAmmo;

    }

    protected void setAggroRange(int range) {
        this.aggroRange = range;
    }

    private boolean hasLineOfSight() {
        Rectangle2D.Double pos1 = this.getHitbox();
        Rectangle2D.Double pos2 = this.player.getHitbox();
        Line2D.Double line = new Line2D.Double(pos1.getCenterX(), pos1.getCenterY(), pos2.getCenterX(),
                pos2.getCenterY());

        for (IStaticObject obj : this.map.getStaticObjects()) {
            if (!obj.isWall()) {
                continue;
            }
            if (line.intersects(obj.getBounds())) {
                return false;
            }
        }
        return true;
    }

    private boolean inShootingRange() {
        return distance(this.getHitbox(), this.player.getHitbox()) <= this.range;
    }

    // ABSTRACT METHODS

    protected abstract void rangedAttack(Double attackTarget2);

}
