package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DirectionHandlerTest {

    private DirectionHandler directionHandler;

    @BeforeEach
    void setUp() {
        directionHandler = new DirectionHandler();
    }

    @Test
    void getDirectionTest() {
        //directionHandler works for singular direction
        directionHandler.add(Direction.NORTH);
        
        assertEquals(Direction.NORTH, directionHandler.getDirection(), "");
    }

    @Test
    void getDiagonalDirectionTest() {
        //directionsHandler works for diagonal direction
        directionHandler.add(Direction.NORTH);
        directionHandler.add(Direction.EAST);
        
        assertEquals(Direction.NORTH_EAST, directionHandler.getDirection());
    }

    @Test
    void getAllDirectionsTest() {
        directionHandler.add(Direction.SOUTH);
        Set<Direction> dirs = directionHandler.getAllDirections();
        
        assertTrue(dirs.contains(Direction.SOUTH));
        assertEquals(1, dirs.size());
    }

    @Test
    void addTest() {
        directionHandler.add(Direction.WEST);
        
        assertTrue(directionHandler.getAllDirections().contains(Direction.WEST));
    }

    @Test
    void removeTest() {
        directionHandler.add(Direction.NORTH);
        directionHandler.remove(Direction.NORTH);
        
        assertFalse(directionHandler.getAllDirections().contains(Direction.NORTH));
    }

    @Test
    void isMovingTest() {
        assertFalse(directionHandler.isMoving());
        
        directionHandler.add(Direction.EAST);
        assertTrue(directionHandler.isMoving());
    }
}
    

