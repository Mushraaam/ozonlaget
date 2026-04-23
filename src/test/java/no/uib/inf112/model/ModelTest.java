package no.uib.inf112.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IFloor;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IProjectile;
import no.uib.inf112.interfaces.IPuddle;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.Ghoul;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.model.npcs.factory.SpawnPoint;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;
import no.uib.inf112.model.npcs.projectiles.puddles.AcidPuddle;
import no.uib.inf112.player.Helicopter;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;

public class ModelTest {
    private MockedConstruction<SoundHandler> mockedSoundHandler;
    private Model model;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model();
    }

    @AfterEach
    void closeMockedSoundHandler() {
        mockedSoundHandler.close();
    }

    @Test
    void isHandlerMocked() {
        SoundHandler handlerCandidate = model.getSoundHandler();
        SoundHandler mockedHandler = mockedSoundHandler.constructed().get(0);
        assertSame(mockedHandler, handlerCandidate);
    }

    @Test
    void EnemiesTest() {
        ArrayList<IEnemy> enemies = model.getEnemies();
        assertNotNull(enemies);
        assertTrue(enemies.isEmpty()); // Should be empty at start

        assertEquals(enemies.size(), model.getEnemyCount());

        Ghoul enemy = new Ghoul(new Rectangle2D.Double(0, 0, 10, 10), model);
        model.addEnemy(enemy);
        enemies = model.getEnemies();
        assertFalse(enemies.isEmpty());

        assertEquals(enemies.size(), model.getEnemyCount());
        assertEquals(1, model.getEnemyCount());

        model.removeEnemy(enemy);

        enemies = model.getEnemies();
        assertNotNull(enemies);
        assertTrue(enemies.isEmpty());

    }

    @Test
    void PuddlesTest() {

        ArrayList<IPuddle> puddles = model.getAOEPuddles();
        assertNotNull(puddles);
        assertTrue(puddles.isEmpty());
        assertEquals(0, puddles.size());

        IPuddle puddle = new AcidPuddle(new Rectangle2D.Double(0, 0, 10, 10), model);
        model.addAOEPuddle(puddle);
        puddles = model.getAOEPuddles();
        assertNotNull(puddles);
        assertFalse(puddles.isEmpty());
        assertEquals(1, puddles.size());

        model.removeAOEPuddle(puddle);
        puddles = model.getAOEPuddles();
        assertNotNull(puddles);
        assertTrue(puddles.isEmpty());
        assertEquals(0, puddles.size());

    }

    @Test
    void getObjectsTest() {
        ArrayList<IStaticObject> staticObjects = model.getStaticObjects();
        assertNotNull(staticObjects);
        assertFalse(staticObjects.isEmpty()); // Should be filled at start

    }

    @Test
    void correctLevelTest() {
        int expectedLevel = 1;
        assertEquals(expectedLevel, model.level());
    }

    @Test
    void getGridTest() {
        int expectedWidth = Config.getInt("mapWidth");
        int expectedHeight = Config.getInt("mapHeight");

        IGrid grid = model.getGrid();
        int expectedCols = expectedWidth / Config.getInt("cellWidth");
        int expectedRows = expectedHeight / Config.getInt("cellHeight");

        assertEquals(expectedCols, grid.getColCount());
        assertEquals(expectedRows, grid.getRowCount());
    }

    @Test
    void getTileGridTest() {
        int expectedWidth = Config.getInt("mapWidth");
        int expectedHeight = Config.getInt("mapHeight");

        IGrid grid = model.getTiles();
        int expectedCols = expectedWidth / Config.getInt("tileWidth");
        int expectedRows = expectedHeight / Config.getInt("tileHeight");

        assertEquals(expectedCols, grid.getColCount());
        assertEquals(expectedRows, grid.getRowCount());
    }

    @Test
    void boundsTest() {

        int expectedWidth = Config.getInt("mapWidth");
        int expectedHeight = Config.getInt("mapHeight");
        Rectangle2D.Double expectedBounds = new Rectangle2D.Double(0, 0, expectedWidth, expectedHeight);
        assertEquals(expectedBounds, model.getBounds());

    }

    @Test
    void getItemsTest() {
        ArrayList<ICollectable> activeITems = model.getActiveItems();
        assertNotNull(activeITems);
        assertFalse(activeITems.isEmpty());

        assertEquals(0, model.getTotalDroppedLoot());

        model.increaseDroppedLoot();
        assertEquals(1, model.getTotalDroppedLoot());

        model.decreaseDroppedLoot();
        assertEquals(0, model.getTotalDroppedLoot());

        model.decreaseDroppedLoot();
        assertEquals(0, model.getTotalDroppedLoot());

    }

    @Test
    void getPathfinderTest() {
        Pathfinder pathfinder = model.getPathfinder();
        assertNotNull(pathfinder);
        assertTrue(pathfinder instanceof Pathfinder);
    }

    @Test
    void getPlayerTest() {
        IPlayer player = model.getPlayer();
        assertNotNull(player);
        assertTrue(player instanceof IPlayer);
        int expectedWidth = Config.getInt("playerWidth");
        int expectedHeight = Config.getInt("playerHeight");
        assertEquals(expectedWidth, player.getHitbox().width);
        assertEquals(expectedHeight, player.getHitbox().height);
    }

    @Test
    void gameStateTest() {

        assertEquals(GameState.MAIN_MENU, model.getGameState());
        for (GameState state : GameState.values()) {
            model.setGameState(state);
            assertEquals(state, model.getGameState());
        }
    }

    @Test
    void debugTest() {
        assertFalse(model.debugMode());
        model.debugOn();
        assertTrue(model.debugMode());
        model.debugOn();
        assertTrue(model.debugMode());
        model.debugOff();
        assertFalse(model.debugMode());
        model.debugOff();
        assertFalse(model.debugMode());

    }

    @Test
    void getVehicleTest() {
        assertNotNull(model.getVehicles());
        assertEquals(1, model.getVehicles().size()); // for now there is only 1 vehicle

        assertNotNull(model.getHelicopter());
        assertTrue(model.getHelicopter() instanceof Helicopter);

    }

    @Test
    void gatherOccupiedCellsTest() {
        Ghoul ghoul = new Ghoul(new Rectangle2D.Double(0, 0, 10, 10), model);
        model.addEnemy(ghoul);
        model.gatherOccupiedCells();

        IGrid grid = model.getGrid();

        ICell cell = grid.getCellFromPos(new Rectangle2D.Double(0, 0, 10, 10));

        assertTrue(cell.occupiedBy(ghoul));

        model.resetOccupied();

        assertFalse(cell.occupiedBy(ghoul));

    }

    @Test
    void floorsTest() {
        ArrayList<IFloor> floors = model.getFloors();
        assertNotNull(floors);
        assertFalse(floors.isEmpty());

        int initialSize = floors.size();

        IFloor mockFloor = floors.get(0);
        model.addFloor(mockFloor);

        assertEquals(initialSize + 1, model.getFloors().size());
    }

    // Time to do some big time mocking

    @Test
    void gunShotTest() {
        assertNotNull(model.gunShots());
        assertFalse(model.gunShots().iterator().hasNext());

        IGunShot shot = mock(IGunShot.class);
        model.addShot(shot);

        Iterable<IGunShot> shots = model.gunShots();
        assertTrue(shots.iterator().hasNext());

        model.removeShot(shot);
        assertFalse(model.gunShots().iterator().hasNext());
    }

    @Test
    void cameraTest() {
        assertNotNull(model.getCamera());
        assertTrue(model.getCamera() instanceof Camera);
    }

    @Test
    void projectileTest() {
        assertNotNull(model.getProjectiles());
        assertTrue(model.getProjectiles().isEmpty());

        IProjectile projectile = mock(IProjectile.class);
        model.addProjectile(projectile);

        assertEquals(1, model.getProjectiles().size());

        model.removeProjectile(projectile);
        assertTrue(model.getProjectiles().isEmpty());
    }

    @Test
    void spawnPointTest() {
        assertNotNull(model.getSpawnPoints());
        assertFalse(model.getSpawnPoints().isEmpty());

        SpawnPoint point = mock(SpawnPoint.class);
        int size = model.getSpawnPoints().size();
        model.addSpawnPoint(point);

        assertEquals(size + 1, model.getSpawnPoints().size());
    }

    @Test
    void resetMapTest() {
        model.addEnemy(new Ghoul(new Rectangle2D.Double(0, 0, 10, 10), model));

        IGrid grid = model.getGrid();
        IGrid tileGrid = model.getTiles();
        Pathfinder pathfinder = model.getPathfinder();

        model.resetMap();

        assertEquals(0, model.getEnemyCount());
        assertNotSame(grid, model.getGrid());
        assertNotSame(tileGrid, model.getTiles());
        assertNotSame(pathfinder, model.getPathfinder());
    }

    @Test
    void setLevelTest() {
        assertEquals(1, model.level());

        model.setLevel(2);
        assertEquals(2, model.level());

        model.setLevel(1);
        assertEquals(1, model.level());
    }

    @Test
    void activeItemsTest() {
        ArrayList<ICollectable> items = model.getActiveItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());

        int initialSize = items.size();

        ICollectable item = mock(ICollectable.class);
        model.addToActiveItems(item);
        assertEquals(initialSize + 1, model.getActiveItems().size());

        model.removeActiveItem(item);
        assertEquals(initialSize, model.getActiveItems().size());
    }


    @Test
    void itemSpawnPointsTest() {
        ArrayList<Rectangle2D.Double> buffSpawnPoints = new ArrayList<>();
        ArrayList<Rectangle2D.Double> inventorySpawnPoints = new ArrayList<>();

        buffSpawnPoints.add(new Rectangle2D.Double(0, 0, 10, 10));
        inventorySpawnPoints.add(new Rectangle2D.Double(0, 0, 10, 10));

        model.setItemSpawnPoints(buffSpawnPoints, inventorySpawnPoints);

        assertSame(buffSpawnPoints, model.getBuffItemSpawnpoint());
        assertSame(inventorySpawnPoints, model.getInventoryItemSpawnpoint());
    }

    @Test
    void setItemSpawnPointsTest() {
        ArrayList<Rectangle2D.Double> buff = new ArrayList<>();
        ArrayList<Rectangle2D.Double> inv = new ArrayList<>();

        model.setItemSpawnPoints(buff, inv);

        assertSame(buff, model.getBuffItemSpawnpoint());
        assertSame(inv, model.getInventoryItemSpawnpoint());
    }

    @Test
    void getBuffItemSpawnpointTest() {
        ArrayList<Rectangle2D.Double> buff = new ArrayList<>();
        model.setItemSpawnPoints(buff, new ArrayList<>());

        assertSame(buff, model.getBuffItemSpawnpoint());
    }

    @Test
    void getInventoryItemSpawnpointTest() {
        ArrayList<Rectangle2D.Double> inv = new ArrayList<>();
        model.setItemSpawnPoints(new ArrayList<>(), inv);

        assertSame(inv, model.getInventoryItemSpawnpoint());
    }

    @Test
    void itemFactoryTest() {
        assertNotNull(model.getItemFactory());
        assertTrue(model.getItemFactory() instanceof ItemFactory);
    }

    @Test
    void factoryTest() {
        assertNotNull(model.getFactory());
        assertTrue(model.getFactory() instanceof Factory);
    }
}
