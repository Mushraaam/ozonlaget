package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ZombieCIRCLEHITBOX implements IEnemy {
    private static final double SPEED = 0.2 * (Config.getInt("playerMoveSpeed"));
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private static final int ANIMATION_COUNT = 8;

    private final IMap map;
    private final IPlayer player;

    private Rectangle2D.Double drawbox;   // Visuals
    private Ellipse2D.Double hitbox;      // Physics

    private List<ICell> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    private int animationIndex = 0;
    private double facingAngle = 0.0;

    public ZombieCIRCLEHITBOX(Rectangle2D.Double pos, IMap map) {
        this.drawbox = pos;
        // The hitbox is a circle inscribed in the drawbox
        this.hitbox = new Ellipse2D.Double(pos.x, pos.y, pos.width, pos.height);
        this.map = map;
        this.player = map.getPlayer();
    }

    @Override
    public void move(IGrid grid) {
        if (currentPath == null || pathIndex >= currentPath.size()) return;

        Rectangle2D target = currentPath.get(pathIndex).getBounds();
        double dx = target.getCenterX() - hitbox.getCenterX();
        double dy = target.getCenterY() - hitbox.getCenterY();
        double dist = Math.hypot(dx, dy);

        if (dist <= SPEED) {
            pathIndex++;
            return;
        }

        updateFacing(dx, dy, dist);

        // Try moving directly
        Ellipse2D.Double candidate = generateCandidate(dx, dy, dist, target);

        if (isLegal(candidate)) {
            updatePosition(candidate.x, candidate.y);
        } else {
            // If direct path is blocked, try sliding
            trySlide(dx, dy, dist, target);
        }
    }

    private void trySlide(double dx, double dy, double dist, Rectangle2D target) {
        Ellipse2D.Double slideX = new Ellipse2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        slideX.x += (dx / dist) * SPEED*2;

        if (isLegal(slideX)) {
            updatePosition(slideX.x, slideX.y);
            return;
        }

        Ellipse2D.Double slideY = new Ellipse2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        slideY.y += (dy / dist) * SPEED*2;

        if (isLegal(slideY)) {
            updatePosition(slideY.x, slideY.y);
        }
    }

    private boolean isLegal(Ellipse2D.Double candidate) {
        if (candidate.intersects(player.getHitbox())) return false;

        //vs other Enemies circle vs circle
        double radius = candidate.width / 2.0;
        double centerX = candidate.getCenterX();
        double centerY = candidate.getCenterY();

        for (IEnemy other : map.getEnemies()) {
            if (other == this) continue;

            Rectangle2D otherBox = other.getHitbox();
            double otherRadius = otherBox.getWidth() / 2.0;

            double dx = centerX - otherBox.getCenterX();
            double dy = centerY - otherBox.getCenterY();
            double distSq = dx * dx + dy * dy;
            double minCombinedDist = radius + otherRadius;

            if (distSq < minCombinedDist * minCombinedDist) {
                return false; // Collision detected
            }
        }
        return true;
    }

    private void updatePosition(double newX, double newY) {
        this.hitbox.x = newX;
        this.hitbox.y = newY;
        this.drawbox.x = newX;
        this.drawbox.y = newY;
    }

    private Ellipse2D.Double generateCandidate(double dx, double dy, double dist, Rectangle2D target) {
        Ellipse2D.Double candidate = new Ellipse2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        candidate.x += (dx / dist) * SPEED;
        candidate.y += (dy / dist) * SPEED;
        return candidate;
    }

    private void updateFacing(double dx, double dy, double dist) {
        if (dist > 0.1) {
            double targetAngle = Math.atan2(dy, dx);
            double angleDiff = targetAngle - this.facingAngle;
            while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;
            while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
            this.facingAngle += angleDiff * ROTATION_SPEED;
        }
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.drawbox;
    }

    @Override
    public Ellipse2D.Double getTrueHitbox() {
        return this.hitbox;
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal  = grid.getCellFromPos(targetBounds);
        currentPath = pathfinder.findPath(start, goal, SIZE);
        pathIndex = (currentPath.size() > 1) ? 1 : 0;
    }

    @Override
    public EnemySize size() { return SIZE; }

    @Override
    public double getFacingAngle() {
        return facingAngle;
    }

    @Override
    public EnemyType getEnemyType() { return EnemyType.ZOMBIE; }

    @Override
    public int getAnimationIndex() { return animationIndex; }

    @Override
    public List<ICell> getCurrentPath() {
        return currentPath;
    }

    @Override
    public void incrementAnimationIndex() {
        this.animationIndex = (this.animationIndex + 1) % ANIMATION_COUNT;
    }
}