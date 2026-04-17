package no.uib.inf112.model.levels;

import no.uib.inf112.interfaces.IModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Level2Test {

    private Level2 level;
    private IModel mockMap;

    @BeforeEach
    public void setUp() {
        mockMap = Mockito.mock(IModel.class);


        ArrayList<Rectangle2D.Double> fakeSpawnList = new ArrayList<>();
        fakeSpawnList.add(new Rectangle2D.Double(0, 0, 10, 10));
        Mockito.when(mockMap.getInventoryItemSpawnpoint()).thenReturn(fakeSpawnList);


        Mockito.when(mockMap.getBuffItemSpawnpoint()).thenReturn(fakeSpawnList);

        level = new Level2(mockMap);
    }

    @Test
    public void testLevelInitializationCreatesPlayer() {
        assertNotNull(level.getPlayer(), "Level 2 should successfully create a player.");
    }

    @Test
    public void testLevelPopulatesStaticObjects() {
        assertFalse(level.getStaticObjects().isEmpty(), "Level 2 should populate the static objects list.");
        assertTrue(level.getStaticObjects().size() > 0, "Level 2 should generate a number of objects/walls.");
    }

    @Test
    public void testLevelPopulatesFloorsAndWater() {
        assertFalse(level.getFloor().isEmpty(), "Level 2 should populate the floors list.");
    }

    @Test
    public void testLevelNumberIsCorrect() {
        assertEquals(2, level.levelNumber(), "Level number should be exactly 2.");
    }

    @Test
    public void testFactoriesAreInitialized() {
        assertNotNull(level.getFactory(), "NPC Factory should be initialized.");
        assertNotNull(level.getItemFactory(), "Item Factory should be initialized.");
    }
}