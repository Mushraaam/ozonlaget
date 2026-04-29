package no.uib.inf112.model.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.model.Model;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

class ItemFactoryTest {
    ItemFactory factory;
    MockedConstruction<SoundHandler> mockedSoundHandler;
    Model model;

    @BeforeEach
    void setUp() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model(mock(LoadStatus.class));

        factory = new ItemFactory(model);
    }

    @AfterEach
    void tearDown() {
        if (mockedSoundHandler != null) {
            mockedSoundHandler.close();
        }
    }

    @Test
    void rollDropFromTableTest() {

        int size = model.getActiveItems().size();
        Rectangle2D.Double bounds = new Rectangle2D.Double(100, 100, 10, 10);

        while (model.getActiveItems().size() <= size) {
            factory.rollDropFromTable(bounds);
            // Must be done in a while loop, because it spawns on a percentage chance
        }

        assertFalse(model.getActiveItems().isEmpty());

        assertEquals(size + 1, model.getActiveItems().size());

        assertEquals(model.getActiveItems().getLast().getHitbox(), bounds);

    }

    @Test
    void dropSpecificItemTest() {
        for (ICollectable col : model.getActiveItems()) {
            col.pickUp();
        }
        Rectangle2D.Double bounds = new Rectangle2D.Double(100, 100, 10, 10);
        for (CollectableType type : CollectableType.values()) {

            if (type == CollectableType.NONE){
                continue; //this type makes nuffin
            }
            factory.dropSpecificItem(type, bounds);

            ICollectable collectable = model.getActiveItems().getLast();

            assertEquals(collectable.getType(), type);

        }
    }

    boolean checkIfContainsITemOfType(CollectableType type, List<ICollectable> col){
        
        for (ICollectable c : col){
            if (c.getType() == type){
                return true;
            }
        }
        return false;
    }

    @Test
    void incrementTest(){

        for (int i = 0; i < 1000; i++){
            factory.increment();
        }

        ArrayList<CollectableType> types = new ArrayList<CollectableType>(List.of(
            CollectableType.HEALTH, CollectableType.ARMOR, CollectableType.POWERUP_RAINBOW,
            CollectableType.POWERUP_DAMAGE, CollectableType.POWERUP_SPEED
        ));
        for (CollectableType c : types){
            if (c == CollectableType.NONE){
                continue;
            }

            assertTrue(checkIfContainsITemOfType(c, model.getActiveItems()));

        }

    }
}
