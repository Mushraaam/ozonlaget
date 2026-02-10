package no.uib.inf112.player;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IViewablePlayer;

public class Player implements IControllablePlayer, IViewablePlayer{

    private Rectangle2D.Double pos;
    public Player(Rectangle2D.Double pos){
        this.pos = pos;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return this.pos;
    }
    
}
