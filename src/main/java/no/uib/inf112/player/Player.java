package no.uib.inf112.player;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.controller.DirectionHandler;
import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.player.guns.DEagle;
import no.uib.inf112.player.guns.MP5;
import no.uib.inf112.player.guns.gunShots.PistolShot;
import no.uib.inf112.records.ShotDestination;
import no.uib.inf112.utility.SoundHandler;

public class Player implements IControllablePlayer, IViewablePlayer {
    private static final int PLAYER_MOVE_SPEED = Config.getInt("playerMoveSpeed");
    private static final int ANIMATION_COUNT = 20;
    private static final int MAX_HP = 100;
    private int currentHP;

    private final DirectionHandler dirHandler;

    private Rectangle2D.Double hitbox;
    private Rectangle2D.Double bounds;
    private Direction currentDirection;
    private double aimAngle;
    private int animationIndex;
    private IMap map;

    private IGun currentGun;
    private HashMap<GunType, IGun> guns;

    private BuffType buffType;
    private int buffCounter;

    public Player(Rectangle2D.Double hitbox, Rectangle2D.Double bounds, IMap map) {
        this.hitbox = hitbox;
        this.bounds = bounds;
        this.dirHandler = new DirectionHandler();
        this.animationIndex = 0;
        this.map = map;
        this.aimAngle = 0;
        setDirection(Direction.WEST);

        // GUNS
        this.currentGun = new DEagle();
        this.guns = new HashMap<>();
        this.guns.put(this.currentGun.type(), this.currentGun);
        this.guns.put(GunType.MP5, new MP5());

        this.currentHP = MAX_HP;

        // buffs
        this.buffType = BuffType.NONE;
        this.buffCounter = 0;
    }

    public void pressMove(Direction dir) {
        this.dirHandler.add(dir);
    }

    public void releaseMove(Direction dir) {
        this.dirHandler.remove(dir);
    }

    public boolean isMoving() {
        return dirHandler.isMoving();
    }

    public void updateMovement() {
        Direction dir = dirHandler.getDirection();
        if (dir != null) {
            movePlayer(dir);
            setDirection(dir);
        }
    }

    public void aimAtWorldPosition(double worldX, double worldY) {
        double playerCenterX = this.hitbox.getCenterX();
        double playerCenterY = this.hitbox.getCenterY();
        double deltaX = worldX - playerCenterX;
        double deltaY = worldY - playerCenterY;
        this.aimAngle = Math.atan2(deltaY, deltaX);
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.hitbox;
    }

    @Override
    public void movePlayer(Direction dir) {
        Rectangle2D.Double proposedMove = possibleMove(dir);
        if (legalMove(proposedMove)) {
            this.hitbox = proposedMove;
        } else {
            trySlide(dir);
        }
        tryPickupItem();
    }

    private void tryPickupItem(){
        for(ICollectable item : map.getActiveItems()){
            if(item.getHitbox().intersects(this.hitbox)){
               item.pickUp();
            }
        }
    }

    private void trySlide(Direction dir) {
        switch (dir) {
            case SOUTH_EAST -> {
                Rectangle2D.Double east = possibleMove(Direction.EAST);
                if (legalMove(east)) {
                    this.hitbox = east;
                }
                Rectangle2D.Double south = possibleMove(Direction.SOUTH);
                if (legalMove(south)) {
                    this.hitbox = south;
                }
                break;
            }
            case SOUTH_WEST -> {
                Rectangle2D.Double west = possibleMove(Direction.WEST);

                if (legalMove(west)) {
                    this.hitbox = west;
                }
                Rectangle2D.Double south = possibleMove(Direction.SOUTH);
                if (legalMove(south)) {
                    this.hitbox = south;
                }
                break;
            }
            case NORTH_WEST -> {
                Rectangle2D.Double north = possibleMove(Direction.NORTH);

                if (legalMove(north)) {
                    this.hitbox = north;
                }
                Rectangle2D.Double west = possibleMove(Direction.WEST);
                if (legalMove(west)) {
                    this.hitbox = west;
                }
                break;
            }
            case NORTH_EAST -> {
                Rectangle2D.Double north = possibleMove(Direction.NORTH);

                if (legalMove(north)) {
                    this.hitbox = north;
                }
                Rectangle2D.Double east = possibleMove(Direction.EAST);
                if (legalMove(east)) {
                    this.hitbox = east;
                }
                break;
            }
            default -> {
                /* No legal move - do nothing */}
        }
    }

    /**
     * Checks that the player can move in a proposed direction.
     * 
     * @param dir proposed movement direction.
     * @return true if move is within map, false if else.
     */
    private boolean legalMove(Rectangle2D.Double proposedMove) {

        // checks for map border
        if (proposedMove.getX() + proposedMove.getWidth() > bounds.getMaxX() ||
                proposedMove.getY() + proposedMove.getHeight() > bounds.getMaxY() ||
                proposedMove.getX() < bounds.getMinX() ||
                proposedMove.getY() < bounds.getMinY()) {
            return false;
        }

        for (IEnemy enemy : this.map.getEnemies()) {
            if (proposedMove.intersects(enemy.getHitbox())) {
                return false;
            }
        }

        // Check for wall collisions
        for (IStaticObject o : this.map.getStaticObjects()) {
            if (proposedMove.intersects(o.getBounds())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gives a new player position based on proposed direction.
     * 
     * @param dir proposed movement direction.
     * @return a new position for player
     */
    private Rectangle2D.Double possibleMove(Direction dir) {
        int deltaX = 0;
        int deltaY = 0;

        switch (dir) {
            case NORTH:
                deltaY = -PLAYER_MOVE_SPEED;
                break;
            case SOUTH:
                deltaY = PLAYER_MOVE_SPEED;
                break;
            case EAST:
                deltaX = PLAYER_MOVE_SPEED;
                break;
            case WEST:
                deltaX = -PLAYER_MOVE_SPEED;
                break;
            case NORTH_EAST: {
                deltaX = PLAYER_MOVE_SPEED;
                deltaY = -PLAYER_MOVE_SPEED;
                break;
            }
            case NORTH_WEST: {
                deltaX = -PLAYER_MOVE_SPEED;
                deltaY = -PLAYER_MOVE_SPEED;
                break;
            }
            case SOUTH_EAST: {
                deltaX = PLAYER_MOVE_SPEED;
                deltaY = PLAYER_MOVE_SPEED;
                break;
            }
            case SOUTH_WEST: {
                deltaX = -PLAYER_MOVE_SPEED;
                deltaY = PLAYER_MOVE_SPEED;
                break;
            }

            default:
                break;
        }
        return new Rectangle2D.Double(
                this.hitbox.getX() + deltaX,
                this.hitbox.getY() + deltaY,
                hitbox.getWidth(),
                hitbox.getHeight());
    }

    @Override
    public Direction getDirection() {
        return this.currentDirection;
    }

    @Override
    public void setDirection(Direction dir) {
        this.currentDirection = dir;
    }

    @Override
    public int getAnimationIndex() {
        return this.animationIndex;
    }

    @Override
    public double getFacingAngle() {
        return this.aimAngle;
    }

    @Override
    public void setFacingAngle(double angle) {
        this.aimAngle = angle;
    }

    @Override
    public void incrementAnimationIndex() {
        this.animationIndex = (this.animationIndex + 1) % ANIMATION_COUNT;
    }

    @Override
    public GunType gunType() {
        return this.currentGun.type();
    }

    @Override
    public void setGunType(GunType gun) {

        if (this.guns.containsKey(gun)) {
            this.currentGun = this.guns.get(gun);
        }
    }

    @Override
    public int currentAmmunition() {
        return this.currentGun.currentAmmunition();
    }

    @Override
    public int maxAmmunition() {
        return this.currentGun.maxAmmunition();
    }

    @Override
    public int getMaxHP() {
        return MAX_HP;
    }

    @Override
    public int getCurrentHP() {
        return this.currentHP;
    }

    @Override
    public void healHP(int heal) {
        int newHP = this.currentHP + heal;
        if (newHP >  this.getMaxHP()) {
            this.currentHP = MAX_HP;
        } else {
            this.currentHP = newHP;
        }
    }


    @Override
    public void takeDamage(int damage) {
        int newHP = this.currentHP - damage;
        if (newHP < 0) {
            this.currentHP = 0;
        } else {
            this.currentHP = newHP;
        }
    }

    @Override
    public int fireRate() {
        return this.currentGun.fireRate();
    }

    @Override
    public void shoot(MouseEvent e) {

        if (this.currentGun.shoot(this.buffType)) {
            return;
        }

        double x1 = this.hitbox.getCenterX();
        double y1 = this.hitbox.getCenterY();

        double baseAngle = this.aimAngle;

        double inaccuracy = this.currentGun.accuracy();
        double range = this.currentGun.range();
        double spread = (Math.random() * 2 - 1) * inaccuracy;

        ShotDestination hit = raycastShot(x1, y1, baseAngle + spread, range);

        IGunShot shot;
        switch (this.currentGun.type()) {

            case DEAGLE -> {
                shot = new PistolShot(x1, y1, hit.x(), hit.y(), this.map);
            }

            default -> {
                shot = new PistolShot(x1, y1, hit.x(), hit.y(), this.map); // TEMP
                // throw new IllegalStateException("No gun equipped");
            }
        }
        this.map.addShot(shot);

        if (hit.enemy() != null) {
            hit.enemy().takeDamage(this.currentGun.damage(this.buffType));
        }
    }

    // Gippity helped with the math and calculations for the raycast functions - the
    // core idea was my own
    public ShotDestination raycastShot(double startX, double startY, double angle, double range) {
        double endX = startX + Math.cos(angle) * range;
        double endY = startY + Math.sin(angle) * range;
        Line2D.Double line = new Line2D.Double(startX, startY, endX, endY);

        Point2D.Double closestHit = new Point2D.Double(endX, endY);
        double closestDistSq = closestHit.distanceSq(startX, startY);
        IEnemy hitEnemy = null;

        // Check walls
        for (IStaticObject obj : this.map.getStaticObjects()) {
            if (!obj.isWall() || !line.intersects(obj.getBounds())) {
                continue; // Shoots over furniture, skip if not intersecting with object
            }
            Point2D.Double hit = firstIntersection(line, obj.getBounds());
            if (hit != null) {
                double distSq = hit.distanceSq(startX, startY);
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closestHit = hit;
                    hitEnemy = null;
                }
            }
        }

        // Check enemies
        for (IEnemy enemy : this.map.getEnemies()) {
            if (!line.intersects(enemy.getHitbox())) {
                continue; // Skip if not intersecting enemy
            }

            Point2D.Double hit = firstIntersection(line, enemy.getHitbox());
            if (hit != null) {
                double distSq = hit.distanceSq(startX, startY);
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closestHit = hit;
                    hitEnemy = enemy;
                }
            }
        }

        return new ShotDestination(closestHit.x, closestHit.y, hitEnemy);
    }

    private Point2D.Double lineIntersection(Line2D.Double a, Line2D.Double b) {
        double x1 = a.x1;
        double y1 = a.y1;
        double x2 = a.x2;
        double y2 = a.y2;
        double x3 = b.x1;
        double y3 = b.y1;
        double x4 = b.x2;
        double y4 = b.y2;

        double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (Math.abs(denom) < 0.000001) {
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denom;
        double u = ((x1 - x3) * (y1 - y2) - (y1 - y3) * (x1 - x2)) / denom;

        if (t < 0 || t > 1 || u < 0 || u > 1) {
            return null;
        }

        double px = x1 + t * (x2 - x1);
        double py = y1 + t * (y2 - y1);

        return new Point2D.Double(px, py);
    }

    private Point2D.Double firstIntersection(Line2D.Double ray, Rectangle2D rect) {
        Line2D.Double top = new Line2D.Double(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMinY());
        Line2D.Double bottom = new Line2D.Double(rect.getMinX(), rect.getMaxY(), rect.getMaxX(), rect.getMaxY());
        Line2D.Double left = new Line2D.Double(rect.getMinX(), rect.getMinY(), rect.getMinX(), rect.getMaxY());
        Line2D.Double right = new Line2D.Double(rect.getMaxX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());

        Point2D.Double closest = null;
        double bestDistSq = Double.POSITIVE_INFINITY;

        Point2D.Double[] hits = {
                lineIntersection(ray, top),
                lineIntersection(ray, bottom),
                lineIntersection(ray, left),
                lineIntersection(ray, right)
        };

        double sx = ray.x1;
        double sy = ray.y1;

        for (Point2D.Double hit : hits) {
            if (hit != null) {
                double distSq = hit.distanceSq(sx, sy);
                if (distSq < bestDistSq) {
                    bestDistSq = distSq;
                    closest = hit;
                }
            }
        }

        return closest;
    }

    @Override
    public BuffType buffType() {
        return this.buffType;
    }

    @Override
    public void setBuff(BuffType type, SoundHandler handler) {

        this.buffType = type;

        if (type == BuffType.NONE) {
            return;
        }

        if (type == BuffType.RAINBOW) {
            this.buffCounter = 40; // 40 seconds * 0.6
        }

        handler.playBuffMusic(type);

    }

    @Override
    public int buffCountDown() {
        return this.buffCounter;
    }

    @Override
    public void decrementBuff(SoundHandler handler) {
        if (this.buffCounter <= 0) {
            return;
        }

        this.buffCounter--;
        if (this.buffCounter <= 0) {
            this.buffType = BuffType.NONE;
            handler.resumeMusic();
        }
    }
}
