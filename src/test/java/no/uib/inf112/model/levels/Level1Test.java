package no.uib.inf112.model.levels;

import no.uib.inf112.interfaces.IModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Level1Test {

    private Level1 level;
    private IModel mockMap;

    @BeforeEach
    public void setUp() {
        mockMap = Mockito.mock(IModel.class);


        ArrayList<Rectangle2D.Double> fakeSpawnList = new ArrayList<>();
        fakeSpawnList.add(new Rectangle2D.Double(0, 0, 10, 10));
        Mockito.when(mockMap.getInventoryItemSpawnpoint()).thenReturn(fakeSpawnList);


        Mockito.when(mockMap.getBuffItemSpawnpoint()).thenReturn(fakeSpawnList);

        level = new Level1(mockMap);
    }

    @Test
    public void testLevelInitializationCreatesPlayer() {
        assertNotNull(level.getPlayer(), "Level 1 should successfully create a player.");
    }

    @Test
    public void testLevelPopulatesStaticObjects() {
        assertFalse(level.getStaticObjects().isEmpty(), "Level 1 should populate the static objects list.");
        assertTrue(level.getStaticObjects().size() > 50, "Level 1 should generate a large number of objects/walls.");
    }

    @Test
    public void testLevelPopulatesFloorsAndWater() {
        assertFalse(level.getFloor().isEmpty(), "Level 1 should populate the floors list.");
    }

    @Test
    public void testLevelNumberIsCorrect() {
        assertEquals(1, level.levelNumber(), "Level number should be exactly 1.");
    }

    @Test
    public void testFactoriesAreInitialized() {
        assertNotNull(level.getFactory(), "NPC Factory should be initialized.");
        assertNotNull(level.getItemFactory(), "Item Factory should be initialized.");
    }
}