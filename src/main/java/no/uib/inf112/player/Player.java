package no.uib.inf112.player;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IViewablePlayer;

public class Player implements IControllablePlayer, IViewablePlayer{
    public static final int PLAYER_MOVE_SPEED = 5;
    public static final int PLAYER_HEIGHT = 50;
    public static final int PLAYER_WIDTH = 50;
        
    private Rectangle2D.Double hitbox;
    private Rectangle2D.Double bounds;
    private Direction currentDirection;

    public Player(Rectangle2D.Double pos, Rectangle2D.Double bounds){
        this.hitbox = new Rectangle2D.Double(
            0,
            0,
            PLAYER_WIDTH,
            PLAYER_HEIGHT
        );      
        this.bounds = bounds;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.hitbox;
    }

    @Override
    public void movePlayer(Direction dir) {
            int deltaX = 0;
            int deltaY = 0;

            if(legalMove(dir)){
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
                    default:
                        break;
                }
            }
            this.hitbox.setFrame(
                this.hitbox.getX() + deltaX,
                this.hitbox.getY() + deltaY,
                this.hitbox.getWidth(),
                this.hitbox.getHeight()
            );
        }

    /**
     * TODO fix javadoc
     * @param dir
     * @return
     */
    private boolean legalMove(Direction dir){
        Rectangle2D possibleMove = testPossibleMove(dir);

        //TODO comment
        if(possibleMove.getX() + PLAYER_WIDTH > bounds.getWidth() ||
            possibleMove.getY() + PLAYER_HEIGHT > bounds.getHeight()||
            possibleMove.getX() + PLAYER_WIDTH < 0||
            possibleMove.getY() + PLAYER_HEIGHT < 0){
            return false;
           }
        
        return true;
    }

    /**
     * TODO fix javadoc
     * @param dir
     * @return
     */
    private Rectangle2D testPossibleMove(Direction dir){
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
            
            default:
                break;
        }
        return new Rectangle2D.Double(
            this.hitbox.getX() + deltaX,
            this.hitbox.getY() + deltaY,
            PLAYER_WIDTH,
            PLAYER_HEIGHT
        );
    }
    @Override
    public Direction getDirection() {
        return this.currentDirection;
    }

    @Override
    public void setDirection(Direction dir) {
        this.currentDirection = dir;   
    }
    


}
