package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

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
    private static final double ROTATION_SPEED = 0.2;
    private static final EnemySize SIZE = EnemySize.MEDIUM;

    public Zombie(Rectangle2D.Double pos) {
        this.pos = pos;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds
    ) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal = grid.getCellFromPos(targetBounds);
        currentPath = pathfinder.findPath(start, goal, SIZE);
        pathIndex = (currentPath.size() > 1) ? 1 : 0; // gå etter første steg i stien for å følge den.

    }
    private void updateFacing(double dx, double dy, double dist) { //noe assistanse med matten trengtes.....
        if (dist > 0.1) {
            double targetAngle = Math.atan2(dy, dx);
            double angleDiff = targetAngle - this.facingAngle;

            while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;
            while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
            this.facingAngle += angleDiff * ROTATION_SPEED;
        }
    }
    @Override
    public void incrementAnimationIndex() {
        this.animationIndex = (this.animationIndex + 1) % ANIMATION_COUNT;
    }


    @Override
    public void move(IGrid grid) {
        if (currentPath == null || pathIndex >= currentPath.size()) return;
        Rectangle2D target = currentPath.get(pathIndex).getBounds();
        double dx = target.getCenterX() - pos.getCenterX();
        double dy = target.getCenterY() - pos.getCenterY();
        double dist = Math.hypot(dx, dy);

        updateFacing(dx, dy, dist);
        if (dist <= SPEED) {
            pos.x = target.getCenterX() - pos.width / 2.0;
            pos.y = target.getCenterY() - pos.height / 2.0;
            pathIndex++;
        } else {
            pos.x += (dx / dist) * SPEED;
            pos.y += (dy / dist) * SPEED;
        }
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
