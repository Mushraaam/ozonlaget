package no.uib.inf112.map.levels;

import java.util.ArrayList;

import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IStaticObject;

public class Level1 implements ILevel{

    private ArrayList<IStaticObject> staticObjects;
    public Level1(){
        this.staticObjects = new ArrayList<>();
    }


    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }
    
}
