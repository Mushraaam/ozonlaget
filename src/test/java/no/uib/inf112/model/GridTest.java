package no.uib.inf112.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import no.uib.inf112.interfaces.IEnemy;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.model.npcs.Ghoul;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

class GridTest {

    int width;
    int height;
    Grid grid;
    TileGrid tileGrid;
    int cellWidth;
    int cellHeight;
    Model testmap;

    MockedConstruction<SoundHandler> mockedSoundHandler;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        testmap = new Model(mock(LoadStatus.class));
    }

    @AfterEach
    void closeMockedSoundHandler() {
        mockedSoundHandler.close();
    }

    @BeforeEach
    void makeGrid() {

        width = Config.getInt("mapWidth");
        height = Config.getInt("mapHeight");
        grid = (Grid) testmap.getGrid();
        tileGrid = (TileGrid) testmap.getTiles();
        cellWidth = grid.getCellWidth();
        cellHeight = grid.getCellHeight();

    }

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
    void getNearbyCellsTest(){
        ICell cell = grid.getCell(5, 5);

        ArrayList<ICell> nearbyCells = grid.getNearbyCells(cell.getBounds(), cellWidth * 3);
        
        int count = 0;

        for (int i = 2; i <= 8; i++){
            for (int j = 2; j <= 8; j++){
                assertEquals(i, nearbyCells.get(count).row());
                assertEquals(j, nearbyCells.get(count).col());
                count++;
            }
        }
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
    void getNeighboursTest() {

        ICell cell = grid.getCell(0, 0);

        assertEquals(3, grid.getNeighbours(cell).size()); // Expect diagonals now, down, left and down right.

        cell = grid.getCell(2, 2);

        List<ICell> cells = grid.getNeighbours(cell);
        assertEquals(8, cells.size()); // All directions should be open -> 8

        assertFalse(cells.contains(grid.getCell(4, 4)));
        assertTrue(cells.contains(grid.getCell(3, 3)));

        // Implement logic for "correct" neighbours also
    }

    @Test
    void iteratorTest() {
        int expectedCount = (int) (height / cellHeight) * (width / cellWidth);

        int count = 0;
        for (ICell cell : grid) {
            count++;
        }

        assertEquals(expectedCount, count);

        /* expand this testing */
    }

    @Test
    void getCellFromXYTest() {

        int maxX = (int) width / grid.getCellWidth() - 1; // Adjust for 0-indexing
        int maxY = (int) height / grid.getCellHeight() - 1; // Adjust for 0-indexing
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

    @Test
    void distanceTest() {

        ICell botRight = grid.getCellFromXY(width, height);
        ICell topLeft = grid.getCellFromXY(0, 0);
        ICell botLeft = grid.getCellFromXY(0, height);
        ICell topRight = grid.getCellFromXY(width, 0);

        // Check equality
        assertEquals(grid.distance(botLeft, topLeft), grid.distance(botRight, topRight));
        assertEquals(grid.distance(botLeft, botRight), grid.distance(topLeft, topRight));
        assertEquals(grid.distance(botLeft, topRight), grid.distance(botRight, topLeft));
        assertEquals(grid.distance(botRight, topLeft), grid.distance(botLeft, topRight));

        // Check mirroring
        assertEquals(grid.distance(botRight, topLeft), grid.distance(topLeft, botRight));

        // Check values
        assertEquals(0, grid.distance(botLeft, botLeft));

        double hypotenuse = Math.hypot(
                (width - 1) / cellWidth,
                (height - 1) / cellHeight); // Subtract 1 since distance is measured from centerXY
        assertEquals(hypotenuse, grid.distance(botLeft, topRight));

    }

    @Test
    void gettersTest() {

        double expectedColCount = width / cellWidth;
        double expectedRowCount = height / cellHeight;
        assertEquals(expectedColCount, grid.getColCount());
        assertEquals(expectedRowCount, grid.getRowCount());

        assertEquals(width, grid.getCellWidth() * grid.getColCount());
        assertEquals(height, grid.getCellHeight() * grid.getRowCount());
        assertEquals(cellWidth, grid.getCellWidth());
        assertEquals(cellHeight, grid.getCellHeight());

    }

    @Test
    void getCellsInViewTest() {
        Graphics2D g = mock(Graphics2D.class);
        when(g.getClipBounds()).thenReturn(new Rectangle(0, 0, this.width / 2, this.height / 2));

        List<ICell> viewableCells = grid.getCellsInView(g);
        HashSet<ICell> cellsSet = new HashSet<>(viewableCells);

        int expectedCols = grid.getColCount() / 2;
        int expectedRows = grid.getRowCount() / 2;
        for (int i = 0; i < expectedRows; i++) {
            for (int j = 0; j < expectedCols; j++) {

                // Contains cells in view
                assertTrue(cellsSet.contains(grid.getCell(i, j)));

                // Does not contain cells not in view
                assertFalse(cellsSet.contains(
                        grid.getCell(grid.getRowCount() - i - 1,
                                grid.getColCount() - j - 1))); // Subtract 1 for 0-indexing
            }
        }
    }

    @Test
    void getNeighboursAtDepthTest() {

        ICell cell = grid.getCell(10, 10);

        List<ICell> depth0 = grid.getNeighboursAtDepth(cell, 0);
        List<ICell> depth1 = grid.getNeighboursAtDepth(cell, 1);
        List<ICell> depth2 = grid.getNeighboursAtDepth(cell, 2);

        assertTrue(depth0.isEmpty());

        assertEquals(8, depth1.size());
        assertEquals(24, depth2.size());

        assertEquals(new HashSet<>(depth1).size(), depth1.size());
        assertEquals(new HashSet<>(depth2).size(), depth2.size());

        assertTrue(depth2.containsAll(depth1));

        assertAllWithinDepth(cell, depth1, 1);
        assertAllWithinDepth(cell, depth2, 2);

    }

    private void assertAllWithinDepth(ICell center, List<ICell> neighbours, int depth) {
        for (ICell neighbour : neighbours) {
            int rowDiff = Math.abs(neighbour.row() - center.row());
            int colDiff = Math.abs(neighbour.col() - center.col());

            int chebyshevDistance = Math.max(rowDiff, colDiff);

            assertTrue(chebyshevDistance >= 1 && chebyshevDistance <= depth);
        }
    }

    @Test
    void gatherOccupiedCellsTest() {
        IEnemy ghoul = new Ghoul(new Rectangle2D.Double(0, 0, cellWidth * 2, cellHeight * 2), testmap);
        testmap.addEnemy(ghoul);
        grid.gatherOccupiedCells();

        for (int i = 0; i < 5; i++) { // is expanded by size * 1.54 and rounded up
            for (int j = 0; j < 5; j++) { // therefore we set expected = Math.ceil(cellWidth * 2 * 1.54)
                ICell cell = grid.getCell(i, j);
                assertTrue(cell.isOccupied(ghoul.size()));
                assertEquals(1, cell.occupiedCount(ghoul.size()));
            }
        }

        for (int i = 5; i < grid.getRowCount(); i++) {
            for (int j = 5; j < grid.getColCount(); j++) {
                ICell cell = grid.getCell(i, j);
                assertFalse(cell.isOccupied(ghoul.size()));
            }
        }

        grid.resetOccupied();

        ghoul.takeDamage(10000); //kill

        grid.gatherOccupiedCells();

        for (ICell cell : grid){
            assertFalse(cell.isOccupied(ghoul.size()));
        }
    }

    @Test
    void resetOccupiedTest() {
        IEnemy ghoul = new Ghoul(new Rectangle2D.Double(0, 0, cellWidth * 2, cellHeight * 2), testmap);
        testmap.addEnemy(ghoul);
        grid.gatherOccupiedCells();
        boolean hasTested = false;
        if (hasOccupiedCells(ghoul)) {
            grid.resetOccupied();

            for (ICell cell : grid) {
                assertFalse(cell.isOccupied(ghoul.size()));
                hasTested = true;
            }
        }
        assertTrue(hasTested, "Failed to run test");
    }

    private boolean hasOccupiedCells(IEnemy enemy) {
        for (ICell cell : grid) {
            if (cell.isOccupied(enemy.size())) {
                return true;
            }
        }
        return false;
    }

    @Test
    void cellTest() {
        // This tests the remaining cell functions that have not been passively tested
        // above

        ICell cell = grid.getCell(1, 1);
        ICell cell2 = grid.getCell(1, 1);
        ICell cell3 = grid.getCell(1, 2);
        assertSame(cell, cell2);
        assertEquals(cell, cell2);
        assertNotSame(cell, cell3);
        assertNotSame(cell2, cell3);
        assertNotEquals(cell, cell3);
        assertNotEquals(cell2, cell3);

        int row = cell.row();
        int col = cell.col();
        Rectangle2D.Double bounds = cell.getBounds();
        boolean blocked = false;

        String expectedOutput = String.format("Row: %s, Col: %s, Bounds: %s, Blocked: %s",
                row,
                col,
                bounds,
                blocked);

        assertEquals(expectedOutput, cell.toString());

        assertSame(FloorType.NONE, cell.floorType());

    }

    // essentially same tests only for TileGrid
@Test
void getCellFromXYTileGridTest() {
    ICell topLeft = tileGrid.getCellFromXY(0, 0);
    assertEquals(0, topLeft.row());
    assertEquals(0, topLeft.col());

    ICell bottomRight = tileGrid.getCellFromXY(width, height);
    assertEquals(tileGrid.getRowCount() - 1, bottomRight.row());
    assertEquals(tileGrid.getColCount() - 1, bottomRight.col());

    ICell middle = tileGrid.getCellFromXY(80, 120);
    assertEquals(3, middle.row());
    assertEquals(2, middle.col());
}

@Test
void getCellFromPosTileGridTest() {
    ICell cell = tileGrid.getCell(2, 3);
    Rectangle2D.Double bounds = cell.getBounds();

    ICell found = tileGrid.getCellFromPos(bounds);

    assertSame(cell, found);
}

@Test
void getCellTileGridTest() {
    ICell cell = tileGrid.getCell(0, 0);
    assertEquals(0, cell.row());
    assertEquals(0, cell.col());
    assertEquals(new Rectangle2D.Double(0, 0, tileGrid.getCellWidth(), tileGrid.getCellHeight()), cell.getBounds());

    ICell other = tileGrid.getCell(4, 4);
    assertEquals(4, other.row());
    assertEquals(4, other.col());
}

@Test
void getNeighboursTileGridTest() {
    ICell cell = tileGrid.getCell(1, 1);
    assertEquals(null, tileGrid.getNeighbours(cell));
}

@Test
void distanceTileGridTest() {
    ICell a = tileGrid.getCell(0, 0);
    ICell b = tileGrid.getCell(10, 10);

    assertEquals(0, tileGrid.distance(a, b));
    assertEquals(0, tileGrid.distance(a, a));
}

@Test
void gatherOccupiedCellsTileGridTest() {
    tileGrid.gatherOccupiedCells();

    ICell cell = tileGrid.getCell(0, 0);
    assertFalse(cell.isOccupied(null));
}

@Test
void resetOccupiedTileGridTest() {
    tileGrid.resetOccupied();

    ICell cell = tileGrid.getCell(0, 0);
    assertFalse(cell.isOccupied(null));
}

@Test
void getNearbyCellsTileGridTest() {
    Rectangle2D.Double pos = new Rectangle2D.Double(50, 50, 10, 10);
    assertEquals(null, tileGrid.getNearbyCells(pos, 20));
}

@Test
void getNeighboursAtDepthTileGridTest() {
    ICell cell = tileGrid.getCell(10, 10);

    List<ICell> depth0 = tileGrid.getNeighboursAtDepth(cell, 0);
    List<ICell> depth1 = tileGrid.getNeighboursAtDepth(cell, 1);

    assertTrue(depth0.isEmpty());
    assertTrue(depth1.isEmpty());
}

@Test
void getCellWidthTileGridTest() {
    assertEquals(40, tileGrid.getCellWidth());
}

@Test
void getCellHeightTileGridTest() {
    assertEquals(40, tileGrid.getCellHeight());
}
}
