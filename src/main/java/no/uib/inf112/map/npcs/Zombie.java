package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.map.Cell;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Zombie implements IEnemy {
    private static final double SPEED = 0.5 * (Config.getInt("playerMoveSpeed"));
    private Rectangle2D.Double pos;
    private List<ICell> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int animationIndex = 0;
    private int ANIMATION_COUNT = 8;
    private double facingAngle = 0.0;
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private IMap map;
    double goalOffsetX;
    double goalOffsetY;
    ICell lastStart;
    ICell lastGoal;
    private IEnemy nextInCell = null;


    // test
    private int currentTarget;
    private boolean sliding = false;
    private int slideXDir = 1;
    private int slideYDir = 1;
    private boolean slidePreferX = true;

    private IPlayer player;

    public Zombie(Rectangle2D.Double pos, IMap map) {
        this.pos = pos;
        this.map = map;

        double r = pos.getHeight(); // Eller width
        this.goalOffsetX = (Math.random() * 2 - 1) * r;
        this.goalOffsetY = (Math.random() * 2 - 1) * r;
        this.player = map.getPlayer();

        this.currentTarget = 0;
    }

    @Override
    public Ellipse2D.Double getTrueHitbox() {
        return null;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal  = grid.getCellFromPos(targetBounds);

        if (start == null || goal == null) return;

        if (start.equals(lastStart) && goal.equals(lastGoal) && currentPath != null && !currentPath.isEmpty()) {
            return; // no need to repath
        }

        lastStart = start;
        lastGoal = goal;

        currentPath = pathfinder.findPath(start, goal, SIZE);
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
            this.facingAngle += angleDiff * ROTATION_SPEED;

        }
    }

    @Override
    public void incrementAnimationIndex() {
        this.animationIndex = (this.animationIndex + 1) % ANIMATION_COUNT;
    }

    @Override
    public void move(IGrid grid) {
        ICell from = getStandingCell();
        if (currentPath == null || pathIndex >= currentPath.size())
            return;
        int lookAheadLimit = Math.min(currentPath.size(), pathIndex + 3);
        for (int i = pathIndex; i < lookAheadLimit; i++) {
            Rectangle2D target = currentPath.get(i).getBounds();
            double dx = target.getCenterX() - pos.getCenterX();
            double dy = target.getCenterY() - pos.getCenterY();
            double dist = Math.hypot(dx, dy);

            if (tryMove(dx, dy, dist, target)) {
                updateFacing(dx, dy, dist);
                this.pathIndex = i;
                if (dist <= SPEED) {
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
        if(from == lastStart){
        unStuck(true,true);}
    }

    private void unStuck(boolean moveX, boolean moveY) {
        ICell myCell = this.getStandingCell();

        checkCellAndPush(myCell, moveX, moveY);
        for (ICell neighbor : myCell.getNeighbours()) {
            checkCellAndPush(neighbor, moveX, moveY);
        }
    }

    private void checkCellAndPush(ICell cell, boolean moveX, boolean moveY) {
        IEnemy other = (cell).getFirstEnemy();

        while (other != null) {
            if (other == this) {
                other = other.getNextInCell();
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

                if (moveX) pushed.x += Math.signum(dx) * 0.5;
                if (moveY) pushed.y += Math.signum(dy) * 0.5;

                if (!pushed.intersects(player.getHitbox()) &&
                        map.getPathfinder().canEnter(map.getGrid().getCellFromPos(pushed), SIZE)) {
                    this.pos = pushed;
                }
                else if (moveX && moveY) {
                    unStuck(true, false);
                    unStuck(false, true);
                    return;
                }
            }
            other = other.getNextInCell();
        }
    }





    private boolean tryMove(double dx, double dy, double dist, Rectangle2D target) {
        Rectangle2D.Double candidate = generateCandidate(dx, dy, dist, target);

        if (isLegal(candidate)) {
            this.sliding = false;
            this.pos = candidate;
            return true;

        } else {

            if (!this.sliding) {
                this.slideXDir = (dx >= 0) ? 1 : -1;
                this.slideYDir = (dy >= 0) ? 1 : -1;

                Rectangle2D.Double testX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width,
                        this.pos.height);
                Rectangle2D.Double testY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width,
                        this.pos.height);
                testX.x += slideXDir * SPEED;
                testY.y += slideYDir * SPEED;

                boolean xOk = isLegal(testX);
                boolean yOk = isLegal(testY);

                if (xOk && !yOk)
                    this.slidePreferX = true;
                else if (!xOk && yOk)
                    this.slidePreferX = false;
                else
                    this.slidePreferX = Math.abs(dx) >= Math.abs(dy);
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
        int x = 1; int y = 1;
        if (dx <= 0){x = x * (-1);}
        if (dy <= 0){y = y * (-1);}
        // Slide X
        if (dist <= SPEED) {
            slideX.x = target.getCenterX() - pos.width / 2.0;
        } else {
            slideX.x += (dx / dist) * SPEED;
        }
        if (isLegal(slideX)) {
            this.pos = slideX;
            return true;

        } else { // Slide Y
            if (dist <= SPEED) {
                slideY.y = target.getCenterY() - pos.height / 2.0;
            } else {
                slideY.y += (dy / dist) * SPEED;
            }
            if (isLegal(slideY)) {
                this.pos = slideY;
                return true;
            } else {
            }
        }

        return false;

    }

    private Rectangle2D.Double generateCandidate(double dx, double dy, double dist, Rectangle2D target) {

        Rectangle2D.Double candidate = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        if (dist <= SPEED) {
            candidate.x = target.getCenterX() - pos.width / 2.0;
            candidate.y = target.getCenterY() - pos.height / 2.0;
        } else {
            candidate.x += (dx / dist) * SPEED;
            candidate.y += (dy / dist) * SPEED;
        }
        return candidate;

    }

    private boolean isLegal(Rectangle2D.Double candidate) {
        double padding = 6.0;
        Rectangle2D.Double movementHitbox = new Rectangle2D.Double(
                candidate.x + padding, candidate.y + padding,
                candidate.width - (padding * 2), candidate.height - (padding * 2)
        );
        if (movementHitbox.intersects(this.player.getHitbox())) {
            return false;
        }
        if (!checkCell(getStandingCell(), movementHitbox)) {
            return false;
        }

        for (ICell cell : getStandingCell().getNeighbours()) {
            if(!checkCell(cell, movementHitbox))
                {return false;}
        }

        return true; // The path is clear enough to squeeze through!
    }

    private boolean checkCell(ICell cell, Rectangle2D.Double movementHitbox){
        IEnemy enemy = cell.getFirstEnemy();

        while(enemy != null) {
            if (enemy != this) {
                Rectangle2D enemyHitbox = enemy.getHitbox();
                double shrinkFactor = 0.6; // Only 60% of the center is "solid" to other NPCs

                double coreW = enemyHitbox.getWidth() * shrinkFactor;
                double coreH = enemyHitbox.getHeight() * shrinkFactor;
                double coreX = enemyHitbox.getCenterX() - (coreW / 2);
                double coreY = enemyHitbox.getCenterY() - (coreH / 2);
                Rectangle2D enemyCore = new Rectangle2D.Double(coreX, coreY, coreW, coreH);
                if (movementHitbox.intersects(enemyCore)) {
                    return false;
                }
            }
            enemy = enemy.getNextInCell();
        }
        return true;
    }





    /// //////////////////GETTERS////////////////////////


    @Override
    public IEnemy getNextInCell() { return nextInCell; }

    @Override
    public void setNextInCell(IEnemy next) { this.nextInCell = next; }

    @Override
    public ICell getStandingCell(){
        return map.getGrid().getCellFromPos(getHitbox());
    }

    public List<ICell> getCurrentPath() {
        return currentPath;
    }

    public double getFacingAngle() {
        return facingAngle;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.pos;
    }

    @Override
    public EnemyType getEnemyType() {
        return EnemyType.ZOMBIE;
    }

    @Override
    public int getAnimationIndex() {
        return animationIndex;
    }

    @Override
    public EnemySize size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }
}
