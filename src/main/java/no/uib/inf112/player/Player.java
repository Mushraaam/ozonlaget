package no.uib.inf112.player;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.controller.DirectionHandler;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IGun;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IViewablePlayer;
import no.uib.inf112.player.guns.DEagle;

public class Player implements IControllablePlayer, IViewablePlayer {
    public static final int PLAYER_MOVE_SPEED = Config.getInt("playerMoveSpeed");
    public static final int ANIMATION_COUNT = 8;

    private final DirectionHandler dirHandler;

    private Rectangle2D.Double hitbox;
    private Rectangle2D.Double bounds;
    private Direction currentDirection;
    private double aimAngle;
    private int animationIndex;
    private IMap map;

    private IGun currentGun;
    private HashMap<GunType, IGun> guns;

    public Player(Rectangle2D.Double hitbox, Rectangle2D.Double bounds, IMap map) {
        this.hitbox = hitbox;
        this.bounds = bounds;
        this.dirHandler = new DirectionHandler();
        this.animationIndex = 0;
        this.map = map;
        this.aimAngle = 0;
        setDirection(Direction.WEST);

        this.currentGun = new DEagle();
        this.guns = new HashMap<>();
        this.guns.put(this.currentGun.type(), this.currentGun);
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
            default -> {/* No legal move - do nothing */}
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
        this.currentGun = this.guns.get(gun);
    }

    @Override
    public int currentAmmunition() {
        return this.currentGun.currentAmmunition();
    }

    @Override
    public int maxAmmunition() {
        return this.currentGun.maxAmmunition();
    }

}
