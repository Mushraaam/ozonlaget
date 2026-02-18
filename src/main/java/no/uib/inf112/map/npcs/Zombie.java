package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.lang.reflect.Array;
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
        Rectangle2D.Double shiftedTarget = new Rectangle2D.Double(
                targetBounds.x, // + goalOffsetX,
                targetBounds.y, // + goalOffsetY,
                targetBounds.width,
                targetBounds.height);

        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal = grid.getCellFromPos(shiftedTarget);

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
        if (currentPath == null || pathIndex >= currentPath.size())
            return;
        int lookAheadLimit = Math.min(currentPath.size(), pathIndex + 10);
        int start = sliding ? currentTarget : pathIndex;
        for (int i = start; i < lookAheadLimit; i++) {

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
                return;
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

        if (Math.abs(dx) <= SPEED) {
            slideX.x = target.getCenterX() - pos.width / 2.0;
        } else {
            slideX.x += slideXDir * SPEED;
        }
        if (Math.abs(dy) <= SPEED) {
            slideY.y = target.getCenterY() - pos.height / 2.0;
        } else {
            slideY.y += slideYDir * SPEED;
        }
        if (slidePreferX) {
            if (isLegal(slideX)) {
                this.pos = slideX;
                return true;
            }
            if (isLegal(slideY)) {
                this.pos = slideY;
                return true;
            }
        } else {
            if (isLegal(slideY)) {
                this.pos = slideY;
                return true;
            }
            if (isLegal(slideX)) {
                this.pos = slideX;
                return true;
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

    private boolean isLegal(Double candidate) {
        for (IEnemy enemy : this.map.getEnemies()) {
            if (enemy == this)
                continue;
            if (candidate.intersects(enemy.getHitbox())) {
                return false;
            }
        }
        return !candidate.intersects(this.player.getHitbox());
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
