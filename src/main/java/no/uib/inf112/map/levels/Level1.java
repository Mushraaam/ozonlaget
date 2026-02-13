package no.uib.inf112.map.levels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import no.uib.inf112.config.Config;
import no.uib.inf112.enums.walls.WallType;
import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.player.Player;
import no.uib.inf112.static_objects.WoodWall;

public class Level1 implements ILevel{

    private IPlayer player;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;

    //Player
    private static final int STARTROW = 1030;//2 * Config.getInt("cellWidth"); //Starts in row 2 now
    private static final int STARTCOL = 1030;//2 * Config.getInt("cellHeight");; //Same for 2nd col.
    private static final int PLAYERWIDTH = Config.getInt("playerWidth");
    private static final int PLAYERHEIGHT = Config.getInt("playerHeight");

    //Map
    private static final int MAPX = 0;
    private static final int MAPY = 0;
    private static final int MAPWIDTH = Config.getInt("mapWidth");
    private static final int MAPHEIGHT = Config.getInt("mapHeight");

    private IMap map;

    public Level1(IMap map){

        this.map = map;
        this.staticObjects = new ArrayList<>();

        this.bounds = new Rectangle2D.Double(MAPX, MAPY, MAPWIDTH, MAPHEIGHT);
        this.player = new Player(new Rectangle2D.Double(STARTROW, STARTCOL, PLAYERWIDTH, PLAYERHEIGHT), this.bounds);


        generateStaticObjects();
    }


    private void generateStaticObjects() {

        //start box testing
        staticObjects.add(new WoodWall(new Rectangle2D.Double(900, 900, 15, 300), WallType.LONG_WOODEN_WALL));         //left wall
        staticObjects.add(new WoodWall(new Rectangle2D.Double(900, 900+300, 300, 15), WallType.LONG_WOODEN_WALL));         //bottom wallp
        staticObjects.add(new WoodWall(new Rectangle2D.Double(900, 900, 300, 15), WallType.LONG_WOODEN_WALL));         //top wall
        staticObjects.add(new WoodWall(new Rectangle2D.Double(900+300, 900, 15, 70), WallType.WOODEN_WALL));         //right top
        staticObjects.add(new WoodWall(new Rectangle2D.Double(900+300, 900+300-70, 15, 85), WallType.WOODEN_WALL));     //right bot



    }


    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }


    @Override
    public IPlayer getPlayer() {
        return this.player;
    }


    @Override
    public Rectangle2D.Double getBounds() {
        return this.bounds;
    }
    
    
}
