package no.uib.inf112.player;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IViewablePlayer;

public class Player implements IControllablePlayer, IViewablePlayer{
    public static final int PLAYER_MOVE_SPEED = 5;
    //public static final int PLAYER_HEIGHT = 100;
    //public static final int PLAYER_WIDTH = 100;
        
    private Rectangle2D.Double hitbox;
    private Rectangle2D.Double bounds;
    private Direction currentDirection;

    public Player(Rectangle2D.Double hitbox, Rectangle2D.Double bounds){
        this.hitbox = hitbox;   
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
                    case UP:
                        deltaY = -PLAYER_MOVE_SPEED;
                        break;
                    case DOWN:
                        deltaY = PLAYER_MOVE_SPEED;
                        break;
                    case RIGHT:
                        deltaX = PLAYER_MOVE_SPEED;
                        break;
                    case LEFT:
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
        if(possibleMove.getX() + possibleMove.getWidth() > bounds.getMaxX() ||
            possibleMove.getY() + possibleMove.getHeight() > bounds.getMaxY()||
            possibleMove.getX() < bounds.getMinX()||
            possibleMove.getY() < bounds.getMinY()){
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
            case UP:
                deltaY = -PLAYER_MOVE_SPEED;
                break;
            case DOWN:
                deltaY = PLAYER_MOVE_SPEED;
                break;
            case RIGHT:
                deltaX = PLAYER_MOVE_SPEED;
                break;
            case LEFT:
                deltaX = -PLAYER_MOVE_SPEED;
                break;
            
            default:
                break;
        }
        return new Rectangle2D.Double(
            this.hitbox.getX() + deltaX,
            this.hitbox.getY() + deltaY,
            hitbox.getWidth(),
            hitbox.getHeight()
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
