package no.uib.inf112.controller;

import java.util.EnumSet;
import java.util.Set;

import no.uib.inf112.enums.Direction;

public class DirectionHandler {
    
    private Set<Direction> currentDirections;
    public DirectionHandler(){
        this.currentDirections = EnumSet.noneOf(Direction.class);
    }

    public Direction getDirection(){
        boolean north = currentDirections.contains(Direction.NORTH) ||
                        currentDirections.contains(Direction.NORTH_EAST) ||
                        currentDirections.contains(Direction.NORTH_WEST);

        boolean south = currentDirections.contains(Direction.SOUTH) ||
                        currentDirections.contains(Direction.SOUTH_EAST) ||
                        currentDirections.contains(Direction.SOUTH_WEST);

        boolean east  = currentDirections.contains(Direction.EAST)  ||
                        currentDirections.contains(Direction.NORTH_EAST) ||
                        currentDirections.contains(Direction.SOUTH_EAST);

        boolean west  = currentDirections.contains(Direction.WEST)  ||
                        currentDirections.contains(Direction.NORTH_WEST) ||
                        currentDirections.contains(Direction.SOUTH_WEST);
        if (north && south){
            north = false; south = false;
        }
        if (east && west){
            east = false; west = false;
        }
        if (north){
            if (east){return Direction.NORTH_EAST;}
            if (west){return Direction.NORTH_WEST;}
            return Direction.NORTH;
        }
        if (south){
            if (east){return Direction.SOUTH_EAST;}
            if (west){return Direction.SOUTH_WEST;}
            return Direction.SOUTH;
        }
        if (east){
            return Direction.EAST;
        }
        if (west){
            return Direction.WEST;
        }
        return null; //This should never happen
        

    }

    public Set<Direction> getAllDirections(){
        return this.currentDirections;
    }

    public void add(Direction dir){
        this.currentDirections.add(dir);
    }

    public void remove(Direction dir){
        this.currentDirections.remove(dir);
    }

    public boolean isMoving(){
        return !this.currentDirections.isEmpty();
    }







}
