package no.uib.inf112.map.levels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.enums.walls.WallType;
import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.player.Player;
import no.uib.inf112.static_objects.TestWall;

public class Level1 implements ILevel{

    private IPlayer player;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;

    //Player
    private static final int STARTX = 1250;
    private static final int STARTY = 1250;
    private static final int PLAYERWIDTH = 50;
    private static final int PLAYERHEIGHT = 50;

    //Map
    private static final int MAPX = 0;
    private static final int MAPY = 0;
    private static final int MAPWIDTH = 2500;
    private static final int MAPHEIGHT = 2500;

    private IMap map;

    public Level1(IMap map){

        this.map = map;
        this.staticObjects = new ArrayList<>();

        this.bounds = new Rectangle2D.Double(MAPX, MAPY, MAPWIDTH, MAPHEIGHT);
        this.player = new Player(new Rectangle2D.Double(STARTX, STARTY, PLAYERWIDTH, PLAYERHEIGHT), this.bounds);


        generateStaticObjects();
    }


    private void generateStaticObjects() {

        //start box testing
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900, 15, 300), WallType.WOODEN_WALL));         //left wall
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900+300, 300, 15), WallType.WOODEN_WALL));         //bottom wallp
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900, 300, 15), WallType.WOODEN_WALL));         //top wall
        staticObjects.add(new TestWall(new Rectangle2D.Double(900+300, 900, 15, 70), WallType.WOODEN_WALL));         //right top
        staticObjects.add(new TestWall(new Rectangle2D.Double(900+300, 900+300-70, 15, 85), WallType.WOODEN_WALL));     //right bot



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
