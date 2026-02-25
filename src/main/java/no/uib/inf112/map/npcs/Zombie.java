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
import no.uib.inf112.map.NavigationLane;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Zombie implements IEnemy {
    private static final double SPEED = 0.5 * (Config.getInt("playerMoveSpeed"));
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.SMALL;
    double goalOffsetX;
    double goalOffsetY;
    private Rectangle2D.Double pos;
    private List<NavigationLane> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int animationIndex = 0;
    private int ANIMATION_COUNT = 8;
    private double facingAngle = 0.0;
    private IMap map;
    private IEnemy nextInCell = null;

    private boolean sliding = false;
    private int slideXDir = 1;
    private int slideYDir = 1;
    private boolean slidePreferX = true;
    private NavigationLane currentLane = null;

    private IPlayer player;

    public Zombie(Rectangle2D.Double pos, IMap map) {
        this.pos = pos;
        this.map = map;

        double r = pos.getHeight(); // Eller width
        this.goalOffsetX = (Math.random() * 2 - 1) * r;
        this.goalOffsetY = (Math.random() * 2 - 1) * r;
        this.player = map.getPlayer();
    }

    @Override
    public Ellipse2D.Double getTrueHitbox() {
        return null;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds) {
        // Look up the Macro-Lane from the Micro-Cell
        Cell startCell = (Cell) grid.getCellFromPos(getHitbox());
        Cell goalCell = (Cell) grid.getCellFromPos(targetBounds);

        if (startCell == null || goalCell == null) return;

        NavigationLane startLane = startCell.getNavigationLane(SIZE);
        NavigationLane goalLane = goalCell.getNavigationLane(SIZE);

        if (startLane == null || goalLane == null) return;

        this.currentPath = pathfinder.findPath(startLane, goalLane, SIZE);
        this.pathIndex = 1;
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
        ICell standingCell = getStandingCell();
        if (standingCell instanceof Cell) {
            NavigationLane newLane = ((Cell) standingCell).getNavigationLane(SIZE);

            if (newLane != currentLane) {
                if (currentLane != null) {
                    currentLane.removeOccupant(this);
                }
                if (newLane != null) {
                    newLane.addOccupant(this);
                }
                this.currentLane = newLane;
            }
        }

        if (currentPath == null || pathIndex >= currentPath.size()) return;

        NavigationLane targetLane = currentPath.get(pathIndex);

        double dx = targetLane.getCenterX() - pos.getCenterX();
        double dy = targetLane.getCenterY() - pos.getCenterY();
        double dist = Math.hypot(dx, dy);

        if (tryMove(dx, dy, dist, targetLane.getCenterX(), targetLane.getCenterY())) {
            updateFacing(dx, dy, dist);
            if (dist <= SPEED * 2) {
                pathIndex++;
            }
        }

        // Push away from other zombies to prevent stacking
        unStuck(true, true);
    }

    private void unStuck(boolean moveX, boolean moveY) {
        NavigationLane myLane = currentLane;
        if (myLane == null) return;

        checkLaneAndPush(myLane, moveX, moveY);

        for (NavigationLane neighbor : myLane.getNeighbors()) {
            checkLaneAndPush(neighbor, moveX, moveY);
        }
    }

    private void checkLaneAndPush(NavigationLane lane, boolean moveX, boolean moveY) {
        for (IEnemy other : map.getEnemies()) {
            if (other == this) continue;

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

                ICell targetCell = map.getGrid().getCellFromPos(pushed);
                if (targetCell != null && map.getPathfinder().canEnter(targetCell, SIZE)) {
                    if (!pushed.intersects(player.getHitbox())) {
                        this.pos = pushed;
                    }
                }
            }
        }
    }


    private boolean tryMove(double dx, double dy, double dist, double targetX, double targetY) {
        Rectangle2D.Double candidate = generateCandidate(dx, dy, dist, targetX, targetY);

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

            if (trySlide(dx, dy, dist, targetX, targetY)) {
                sliding = true;
                return true;
            }
        }
        return false;
    }

    private boolean trySlide(double dx, double dy, double dist, double targetX, double targetY) {
        Rectangle2D.Double slideX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        Rectangle2D.Double slideY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

        if (dist <= SPEED) {
            slideX.x = targetX - pos.width / 2.0;
        } else {
            slideX.x += (dx / dist) * SPEED;
        }

        if (isLegal(slideX)) {
            this.pos = slideX;
        }

        if (dist <= SPEED) {
            slideY.y = targetY - pos.height / 2.0;
        } else {
            slideY.y += (dy / dist) * SPEED;
        }

        if (isLegal(slideY)) {
            this.pos = slideY;
        }

        return false;
    }

    private Rectangle2D.Double generateCandidate(double dx, double dy, double dist, double targetX, double targetY) {
        Rectangle2D.Double candidate = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

        if (dist <= SPEED) {
            candidate.x = targetX - pos.width / 2.0;
            candidate.y = targetY - pos.height / 2.0;
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

        if (!isWalkableAt(movementHitbox.x, movementHitbox.y) ||
                !isWalkableAt(movementHitbox.x + movementHitbox.width, movementHitbox.y) ||
                !isWalkableAt(movementHitbox.x, movementHitbox.y + movementHitbox.height) ||
                !isWalkableAt(movementHitbox.x + movementHitbox.width, movementHitbox.y + movementHitbox.height)) {
            return false;
        }

        NavigationLane myLane = (getStandingCell()).getNavigationLane(SIZE);
        if (myLane != null) {
            if (!checkLane(myLane, movementHitbox)) return false;
            for (NavigationLane neighborLane : myLane.getNeighbors()) {
                if (!checkLane(neighborLane, movementHitbox)) return false;
            }
        }

        return true;
    }

    // Helper method for clean wall checking
    private boolean isWalkableAt(double x, double y) {
        ICell cell = map.getGrid().getCellFromXY(x, y);
        if (cell == null) return false;
        return map.getPathfinder().canEnter(cell, SIZE);
    }

    private boolean checkLane(NavigationLane lane, Rectangle2D.Double movementHitbox) {
        for (IEnemy enemy : lane.getEnemies()) {
            if (enemy != this) {
                Rectangle2D enemyHitbox = enemy.getHitbox();
                double shrinkFactor = 0.75;

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


    /// //////////////////GETTERS////////////////////////


    @Override
    public IEnemy getNextInCell() {
        return nextInCell;
    }

    @Override
    public void setNextInCell(IEnemy next) {
        this.nextInCell = next;
    }

    @Override
    public ICell getStandingCell() {
        return map.getGrid().getCellFromPos(getHitbox());
    }

    @Override
    public int getCurrentPathIndex() {
        return pathIndex;
    }

    public List<NavigationLane> getCurrentPath() {
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
