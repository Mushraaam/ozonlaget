package no.uib.inf112.view.DrawStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

import no.uib.inf112.config.Config;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.utility.ImageHandler;

public class GameUI implements IDrawer {

    private IMap map;
    private IPlayer player;
    private ImageHandler handler;
    private BufferedImage uiBar;

    private static final double UI_HEIGHT = Config.getInt("uiSize");
    private static final int GUN_SIZE = Config.getInt("uiGunSize");
    
    public GameUI(IMap map, ImageHandler handler){
        this.map = map;
        this.player = map.getPlayer();
        this.handler = handler;
        this.uiBar = handler.uiBar();
    }


    @Override
    public void draw(Graphics2D graphic) {
        
        Rectangle2D bounds = graphic.getClipBounds().getBounds2D();
        double x1 = bounds.getMinX();
        double width = bounds.getMaxX() - x1;
        double y2 = bounds.getMaxY();
        double y1 = y2 - UI_HEIGHT;

        graphic.setColor(Color.DARK_GRAY);
        drawImage(graphic, uiBar, new Rectangle2D.Double(x1, y1, width, UI_HEIGHT));

        //Gun
        drawImage(graphic, this.handler.getGunImage(this.player.gun()), new Rectangle2D.Double(x1 + 300, y1 + 10, GUN_SIZE, UI_HEIGHT - 30));

    }
    
}
