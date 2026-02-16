package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
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
    private static final double ROTATION_SPEED = 0.2;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private IMap map;

    public Zombie(Rectangle2D.Double pos, IMap map) {
        this.pos = pos;
        this.map = map;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal = grid.getCellFromPos(targetBounds);
        currentPath = pathfinder.findPath(start, goal, SIZE);
        pathIndex = (currentPath.size() > 1) ? 1 : 0; // gå etter første steg i stien for å følge den.

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
        if (currentPath == null || pathIndex >= currentPath.size())
            return;
        Rectangle2D target = currentPath.get(pathIndex).getBounds();
        double dx = target.getCenterX() - pos.getCenterX();
        double dy = target.getCenterY() - pos.getCenterY();
        double dist = Math.hypot(dx, dy);

        if (dist <= SPEED) {
            pathIndex++;
            return;
        }

        updateFacing(dx, dy, dist);

        Rectangle2D.Double candidate = generateCandidate(dx, dy, dist, target);

        if (isLegal(candidate)) {
            this.pos = candidate;

        } else {
            trySlide(dx, dy, dist, target);
        }
    }

    private boolean trySlide(double dx, double dy, double dist, Rectangle2D target) {
        Rectangle2D.Double slideX = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);
        Rectangle2D.Double slideY = new Rectangle2D.Double(this.pos.x, this.pos.y, this.pos.width, this.pos.height);

        boolean nextReached = false;
        // Slide X
        if (dist <= SPEED) {
            nextReached = true;
            slideX.x = target.getCenterX() - pos.width / 2.0;
        } else {
            slideX.x += (dx / dist) * SPEED;
        }
        if (isLegal(slideX)) {
            this.pos = slideX;

        } else { // Slide Y
            nextReached = false;
            if (dist <= SPEED) {
                nextReached = true;
                slideY.y = target.getCenterY() - pos.height / 2.0;
            } else {
                slideY.y += (dy / dist) * SPEED;
            }
            if (isLegal(slideY)) {
                this.pos = slideY;
            } else {
                nextReached = false;
            }
        }
        return nextReached;

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

    private boolean isLegal(Double candidate) {
        for (IEnemy enemy : this.map.getEnemies()) {
            if (candidate.intersects((enemy.getHitbox())) && enemy != this) {
                return false;
            }
        }
        return true;
    }

    /// //////////////////GETTERS////////////////////////

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
