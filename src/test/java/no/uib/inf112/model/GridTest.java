package no.uib.inf112.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.model.Grid;

class GridTest {

    int width;
    int height;
    Grid grid;
    int cellWidth;
    int cellHeight;

    @BeforeEach
    void makeGrid() {

        width = 1000;
        height = 1000;
        grid = new Grid(new TestMap(new Rectangle2D.Double(0, 0, width, height)));
        cellWidth = grid.getCellWidth();
        cellHeight = grid.getCellHeight();

    }

    /*
     * yet to test:
     * distance
     * 
     */

    @Test
    void getCellTest() {

        assertThrows(IndexOutOfBoundsException.class, () -> {
            grid.getCell(1, 10000000);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            grid.getCell(-1, 1);
        });

        ICell cell = grid.getCell(0, 0);

        assertEquals(0, cell.col());
        assertEquals(0, cell.row());

        assertEquals(cell.getBounds(), new Rectangle2D.Double(0, 0, cellWidth, cellHeight));

        ICell cell2 = grid.getCell(4, 4);

        assertEquals(4, cell2.col());
        assertEquals(4, cell2.row());

        ICell cell3 = grid.getCell(0, 0);

        assertSame(cell, cell3);
        assertNotSame(cell2, cell3);

    }

    @Test
    void getCellFromPosTest() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                ICell cell = grid.getCell(i, j);
                Rectangle2D.Double pos = cell.getBounds();

                assertSame(
                        cell,
                        grid.getCellFromPos(pos));
            }
        }

        int desiredRow = 2;
        int desiredCol = 3;
        Rectangle2D.Double desiredBounds = new Rectangle2D.Double(
                                                desiredCol * cellWidth,
                                                desiredRow * cellHeight,
                                                cellWidth,
                                                cellHeight);

        ICell cell = grid.getCellFromPos(desiredBounds);

        assertEquals(desiredRow, cell.row());
        assertEquals(desiredCol, cell.col());

    }

    @Test
    void getNeighboursTest(){

        ICell cell = grid.getCell(0, 0);

        assertEquals(3, grid.getNeighbours(cell).size()); //Expect diagonals now, down, left and down right.

        cell = grid.getCell(2, 2);

        ArrayList<ICell> cells =  grid.getNeighbours(cell);
        assertEquals(8, cells.size()); //All directions should be open -> 8

        assertFalse(cells.contains(grid.getCell(4, 4)));
        assertTrue(cells.contains(grid.getCell(3, 3)));
        
        // Implement logic for "correct" neighbours also
    }

    @Test
    void iteratorTest(){
        int expectedCount =(int) (height / cellHeight) * (width / cellWidth);

        int count = 0;
        for (ICell cell : grid){
            count++;
        }

        assertEquals(expectedCount, count);

        /*expand this testing */
    }

    @Test
    void getCellFromXYTest(){

        int maxX = (int) Math.ceil(width / grid.getCellWidth() - 1);       //Adjust for 0-indexing
        int maxY = (int) Math.ceil(height / grid.getCellHeight() - 1);     //Adjust for 0-indexing
        int minX = 0;
        int minY = 0;

        ICell botRight = grid.getCellFromXY(width, height);
        assertEquals(maxY, botRight.row());
        assertEquals(maxX, botRight.col());


        ICell topLeft = grid.getCellFromXY(0, 0);
        assertEquals(minY, topLeft.row());
        assertEquals(minX, topLeft.col());

        ICell botLeft = grid.getCellFromXY(0, height);
        assertEquals(maxY, botLeft.row());
        assertEquals(minX, botLeft.col());

        ICell topRight = grid.getCellFromXY(width, 0);
        assertEquals(minY, topRight.row());
        assertEquals(maxX, topRight.col());

        
    }

}
