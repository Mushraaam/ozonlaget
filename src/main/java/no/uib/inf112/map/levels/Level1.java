package no.uib.inf112.map.levels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.static_objects.TestWall;

public class Level1 implements ILevel{

    private ArrayList<IStaticObject> staticObjects;
    public Level1(){
        this.staticObjects = new ArrayList<>();

        generateStaticObjects();
    }


    private void generateStaticObjects() {

        //start box testing
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900, 15, 300)));         //left wall
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900+300, 300, 15)));         //bottom wallp
        staticObjects.add(new TestWall(new Rectangle2D.Double(900, 900, 300, 15)));         //top wall
        staticObjects.add(new TestWall(new Rectangle2D.Double(900+300, 900, 15, 70)));         //right top
        staticObjects.add(new TestWall(new Rectangle2D.Double(900+300, 900+300-70, 15, 85)));     //right bot



    }


    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }
    
}
