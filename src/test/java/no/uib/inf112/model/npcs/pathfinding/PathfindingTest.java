package no.uib.inf112.model.npcs.pathfinding;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PathfindingTest {

    private IModel mapMock;
    private Grid realGrid;
    private Pathfinder pathfinder;
    private IEnemy enemyMock;

    @BeforeEach
    void setUp() {
        mapMock = mock(IModel.class);

        when(mapMock.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 1000, 1000));
        when(mapMock.getStaticObjects()).thenReturn(new ArrayList<>());
        when(mapMock.getVehicles()).thenReturn(new ArrayList<>());

        realGrid = new Grid(mapMock);

        when(mapMock.getGrid()).thenReturn(realGrid);
        pathfinder = new Pathfinder(mapMock);

        enemyMock = mock(IEnemy.class);
        when(enemyMock.size()).thenReturn(EnemySize.SMALL);
    }

    @Test
    void testFindPath_DirectRoute_NoObstacles() {
        // Start at (0,0), Goal at (0,3)
        ICell start = realGrid.getCell(0, 0);
        ICell goal = realGrid.getCell(0, 3);

        List<ICell> path = pathfinder.findPath(enemyMock, start, goal, EnemySize.SMALL, null);

        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be empty");
        assertEquals(goal, path.get(path.size() - 1), "The last cell in the path should be the goal");
    }

    @Test
    void testFindPath_RoutesAroundObstacle() {
        ICell start = realGrid.getCell(0, 0);
        ICell goal = realGrid.getCell(0, 2);

        // Block the direct middle path at (0, 1)
        ICell obstacle = realGrid.getCell(0, 1);
        obstacle.setPathType(PathType.BLOCKED);

        List<ICell> path = pathfinder.findPath(enemyMock, start, goal, EnemySize.SMALL, null);

        assertFalse(path.contains(obstacle), "The path must not contain the BLOCKED cell");
        assertEquals(goal, path.get(path.size() - 1), "The algorithm should still find a way to the goal");
    }

    @Test
    void testFindPath_MediumEnemy_CannotPassMediumBlock() {
        // Start (0,0), Goal (0,2)
        ICell start = realGrid.getCell(0, 0);
        ICell goal = realGrid.getCell(0, 2);

        ICell restrictedCell = realGrid.getCell(0, 1);
        restrictedCell.setPathType(PathType.BLOCKED_FOR_MEDIUM);

        realGrid.getCell(1, 0).setPathType(PathType.BLOCKED);
        realGrid.getCell(1, 1).setPathType(PathType.BLOCKED);

        when(enemyMock.size()).thenReturn(EnemySize.MEDIUM);

        List<ICell> path = pathfinder.findPath(enemyMock, start, goal, EnemySize.MEDIUM, null);
        assertNull(path, "Path should be null when completely blocked and no currentPath is provided");
    }

    @Test
    void testFindPath_SmallEnemy_AvoidsTrafficPenalty() {
        // Start (0,0), Goal (0,2)
        ICell start = realGrid.getCell(0, 0);
        ICell goal = realGrid.getCell(0, 2);

        ICell crowdedCell = realGrid.getCell(0, 1);
        IEnemy idleEnemy = mock(IEnemy.class);
        crowdedCell.setOccupant(idleEnemy, EnemySize.SMALL); // triggers the traffic penalty

        when(enemyMock.size()).thenReturn(EnemySize.SMALL);

        List<ICell> path = pathfinder.findPath(enemyMock, start, goal, EnemySize.SMALL, null);

        assertFalse(path.contains(crowdedCell), "Small enemy should detour around the crowded cell to avoid traffic");
        assertEquals(goal, path.get(path.size() - 1), "Path should still successfully reach the goal");
    }

    @Test
    void testFindPath_LargeEnemy_BulldozesThroughTraffic() {
        ICell start = realGrid.getCell(0, 0);
        ICell goal = realGrid.getCell(0, 2);

        //traffic jam at (0,1)
        ICell crowdedCell = realGrid.getCell(0, 1);
        IEnemy idleEnemy = mock(IEnemy.class);
        crowdedCell.setOccupant(idleEnemy, EnemySize.SMALL);

        when(enemyMock.size()).thenReturn(EnemySize.LARGE);

        List<ICell> path = pathfinder.findPath(enemyMock, start, goal, EnemySize.LARGE, null);

        //Bulldozer does bulldozer things
        assertTrue(path.contains(crowdedCell), "Large boss should bulldoze straight through the crowded cell");
        assertEquals(goal, path.get(path.size() - 1), "Path should successfully reach the goal");
    }

    @Test
    void testCanEnter_LargeEnemy_RequiresUnblockedPath() {
        ICell cell = realGrid.getCell(0, 0);
        cell.setPathType(PathType.BLOCKED_FOR_MEDIUM);
        assertFalse(pathfinder.canEnter(cell, EnemySize.LARGE), "Large enemy cannot enter BLOCKED_FOR_MEDIUM");

        cell.setPathType(PathType.UNBLOCKED);
        assertTrue(pathfinder.canEnter(cell, EnemySize.LARGE), "Large enemy can enter UNBLOCKED");
    }
}