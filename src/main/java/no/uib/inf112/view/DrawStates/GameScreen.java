package no.uib.inf112.view.DrawStates;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;

public class GameScreen implements IDrawer{

    private IMap map;
    private IPlayer player;

    public GameScreen(IMap map){
        this.map = map;
        this.player = map.getPlayer();
    }
    @Override
    public void draw(Graphics2D graphic) {
        
        Rectangle2D.Double bounds = this.player.getBounds();
        graphic.drawRect(
            
            (int)bounds.getCenterX(), 
            (int)bounds.getCenterY(),
            (int)bounds.getWidth(), 
            (int)bounds.getHeight());

        //graphic.drawImage();  // Erstatte med denne når bilder er implementert
    }
    
}
