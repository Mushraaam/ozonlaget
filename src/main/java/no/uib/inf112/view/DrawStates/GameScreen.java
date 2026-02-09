package no.uib.inf112.view.DrawStates;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.utility.ImageReader;

public class GameScreen implements IDrawer{

    private IMap map;
    private IPlayer player;
    

    private BufferedImage tempDuck;

    public GameScreen(IMap map){
        this.map = map;
        this.player = map.getPlayer();


        //Bør skaleres kun en gang, dette flyttes senere til ny klasse
        this.tempDuck = ImageReader.fetcImage("src\\main\\java\\no\\resources\\tempduck.png");


    }
    @Override
    public void draw(Graphics2D graphic) {
        
        Rectangle2D.Double bounds = this.player.getBounds();
 
        graphic.drawImage(this.tempDuck,
            (int)bounds.getX(), 
            (int)bounds.getY(),
            (int)bounds.getWidth(), 
            (int)bounds.getHeight(),
            null);
    }
    
}
