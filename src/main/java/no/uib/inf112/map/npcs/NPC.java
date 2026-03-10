package no.uib.inf112.map.npcs;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public abstract class NPC implements IEnemy {
    private double speed;
    private Rectangle2D.Double pos;
    private List<ICell> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int animationIndex = 0;
    private int animationCount;
    private int attackAnimationCount;
    private double facingAngle = 0.0;
    private double rotationSpeed = 0.12;
    private EnemySize size;
    private EnemyType type;
    private IMap map;
    private double goalOffsetX;
    private double goalOffsetY;
    private ICell lastStart;
    private ICell lastGoal;
    private IEnemy nextInCell = null;
    private ICell from;

    private int lastMinR = -1;
    private int lastMaxR = -1;
    private int lastMinC = -1;
    private int lastMaxC = -1;

    // test
    private int currentTarget;
    private boolean sliding = false;
    private int slideXDir = 1;
    private int slideYDir = 1;
    private boolean slidePreferX = true;

    private int health;

    private IPlayer player;

    public NPC(Rectangle2D.Double pos, IMap map, int health) {
        this.pos = pos;
        this.map = map;

        double r = pos.getHeight(); // Eller width
        this.goalOffsetX = (Math.random() * 2 - 1) * r;
        this.goalOffsetY = (Math.random() * 2 - 1) * r;
        this.player = map.getPlayer();

        this.currentTarget = 0;
        this.health = health;

    }

    // methods that need to be implemented per NPC:

    public abstract void attack(Rectangle2D.Double target);

    // shared methods

    @Override
    public Ellipse2D.Double getTrueHitbox() {
        return null;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal = grid.getCellFromPos(targetBounds);

        if (start == null || goal == null)
            return;

        if (start.equals(lastStart) && goal.equals(lastGoal) && currentPath != null && !currentPath.isEmpty()) {
            return; // no need to repath
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
        this.animationIndex = (this.animationIndex + 1) % animationCount;
    }

    @Override
    public ICell getOldCell() {
        return this.from;
    }

    @Override
    public void move(IGrid grid, double dt) {
        this.from = getStandingCell();
        if (currentPath == null || pathIndex >= currentPath.size())
            return;

        double frameSpeed = this.speed * dt * 60.0;

        int lookAheadLimit = Math.min(currentPath.size(), pathIndex + 3);
        for (int i = pathIndex; i < lookAheadLimit; i++) {
            Rectangle2D target = currentPath.get(i).getBounds();
            double dx = target.getCenterX() - pos.getCenterX();
            double dy = target.getCenterY() - pos.getCenterY();
            double dist = Math.hypot(dx, dy);

            // Pass frameSpeed down
            if (tryMove(dx, dy, dist, target, frameSpeed)) {
                updateFacing(dx, dy, dist);
                this.pathIndex = i;

                // Use frameSpeed here too
                if (dist <= frameSpeed) {
                    pathIndex++;
                }

                if (this.sliding) {
                    this.currentTarget = i;
                } else {
                    this.currentTarget = pathIndex;
                }
                break;
            }
        }
        if (from == lastStart) {
            unStuck(true, true);
        }
    }

    private void unStuck(boolean moveX, boolean moveY) {
        ICell myCell = this.getStandingCell();
        if (myCell == null) {
            return;
        }

        checkCellAndPush(myCell, moveX, moveY);
        for (ICell neighbor : map.getGrid().getNeighboursAtDepth(myCell, 2)) {
            if (neighbor != null) {
                checkCellAndPush(neighbor, moveX, moveY);
            }
        }
    }

    private void checkCellAndPush(ICell cell, boolean moveX, boolean moveY) {

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
    private boolean tryMove(double dx, double dy, double dist, Rectangle2D target, double frameSpeed) {
        Rectangle2D.Double candidate = generateCandidate(dx, dy, dist, target, frameSpeed);

        if (isLegal(candidate)) {
            this.sliding = false;
            this.pos = candidate;
            return true;

        } else {
            if (!this.sliding) {
                this.slideXDir = (dx >= 0) ? 1 : -1;
                this.slideYDir = (dy >= 0) ? 1 : -1;

                Rectangle2D.Double testX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
                Rectangle2D.Double testY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

                testX.x += slideXDir * frameSpeed;
                testY.y += slideYDir * frameSpeed;

                boolean xOk = isLegal(testX);
                boolean yOk = isLegal(testY);

                if (xOk && !yOk)
                    this.slidePreferX = true;
                else if (!xOk && yOk)
                    this.slidePreferX = false;
                else
                    this.slidePreferX = Math.abs(dx) >= Math.abs(dy);
            }

            if (trySlide(dx, dy, dist, target, frameSpeed)) {
                sliding = true;
                return true;
            }
        }
        this.sliding = false;
        return false;
    }
    private boolean trySlide(double dx, double dy, double dist, Rectangle2D target, double frameSpeed) {
        Rectangle2D.Double slideX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        Rectangle2D.Double slideY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        int x = 1;
        int y = 1;
        if (dx <= 0) { x = x * (-1); }
        if (dy <= 0) { y = y * (-1); }

        // Slide X
        if (dist <= frameSpeed) {
            slideX.x = target.getCenterX() - pos.width / 2.0;
        } else {
            slideX.x += (dx / dist) * frameSpeed; // Use frameSpeed
        }
        if (isLegal(slideX)) {
            this.pos = slideX;
            return true;

        } else {
            // Slide Y
            if (dist <= frameSpeed) {
                slideY.y = target.getCenterY() - pos.height / 2.0;
            } else {
                slideY.y += (dy / dist) * frameSpeed; // Use frameSpeed
            }
            if (isLegal(slideY)) {
                this.pos = slideY;
                return true;
            }
        }
        return false;
    }
    private Rectangle2D.Double generateCandidate(double dx, double dy, double dist, Rectangle2D target, double frameSpeed) {
        Rectangle2D.Double candidate = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

        if (dist <= frameSpeed) {
            candidate.x = target.getCenterX() - pos.width / 2.0;
            candidate.y = target.getCenterY() - pos.height / 2.0;
        } else {
            candidate.x += (dx / dist) * frameSpeed;
            candidate.y += (dy / dist) * frameSpeed;
        }
        return candidate;
    }

    private boolean isLegal(Rectangle2D.Double candidate) {
        double padding = 6.0;
        Rectangle2D.Double movementHitbox = new Rectangle2D.Double(
                candidate.x + padding, candidate.y + padding,
                candidate.width - (padding * 2), candidate.height - (padding * 2));
        if (movementHitbox.intersects(this.player.getHitbox())) {
            return false;
        }

        if (getStandingCell() == null) {
            return false;
        }

        if (!checkCell(getStandingCell(), movementHitbox)) {
            return false;
        }

        for (ICell cell : map.getGrid().getNeighboursAtDepth(getStandingCell(), 2)) {
            if (!checkCell(cell, movementHitbox)) {
                return false;
            }
        }

        return true; // The path is clear enough to squeeze through!
    }

    private boolean checkCell(ICell cell, Rectangle2D.Double movementHitbox) {
        if (cell == null){
            return false;
        }

        for (IEnemy enemy : map.getEnemies()) {
            if (enemy != this) {
                Rectangle2D enemyHitbox = enemy.getHitbox();
                double shrinkFactor = 0.8; // Only 80% of the center is "solid" to other NPCs

                double coreW = enemyHitbox.getWidth() * shrinkFactor;
                double coreH = enemyHitbox.getHeight() * shrinkFactor;
                double coreX = enemyHitbox.getCenterX() - (coreW / 2);
                double coreY = enemyHitbox.getCenterY() - (coreH / 2);
                Rectangle2D enemyCore = new Rectangle2D.Double(coreX, coreY, coreW, coreH);
                if (movementHitbox.intersects(enemyCore)) {
                    return false;
                }
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

    /// //////////////////GETTERS////////////////////////

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
            this.map.removeEnemy(this);
        }
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

}
