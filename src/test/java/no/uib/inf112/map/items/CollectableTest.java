package no.uib.inf112.map.items;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectableTest {

    private IMap mockMap;
    private Player mockPlayer;
    private Rectangle2D.Double dummyHitbox;
    private DummyCollectable collectable;
    private static class DummyCollectable extends Collectable {
        public boolean affectPlayerCalled = false;

        protected DummyCollectable(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
            super(hitbox, type, map);
        }

        @Override
        public void affectPlayer() {
            this.affectPlayerCalled = true;
        }
    }

    @BeforeEach
    public void setup() {
        mockMap = mock(IMap.class);
        mockPlayer = mock(Player.class);
        when(mockMap.getPlayer()).thenReturn(mockPlayer);

        dummyHitbox = new Rectangle2D.Double(10, 10, 20, 20);

        collectable = new DummyCollectable(dummyHitbox, CollectableType.AMMO_PISTOL, mockMap);
    }

    @Test
    public void testInitialization() {
        // sanity
        assertEquals(dummyHitbox, collectable.getHitbox());
        assertEquals(CollectableType.AMMO_PISTOL, collectable.getType());
        assertEquals(CollectableType.AMMO_PISTOL.getQuantity(), collectable.getAmount());
        assertEquals(CollectableType.AMMO_PISTOL.buffType(), collectable.getBuffType());
    }

    @Test
    public void testPickUpRemovesItemFromMapAndAffectsPlayer() {
        collectable.pickUp();
        //check if item correctly made map remove it
        verify(mockMap, times(1)).removeActiveItem(collectable);

        //check if correct abstract method is used
        assertTrue(collectable.affectPlayerCalled, "affectPlayer() should be called when picked up");
    }

    @Test
    public void testPickUpCannotBeCalledTwice() {
        //try double tappin it
        collectable.pickUp();
        collectable.pickUp();

        // make sure map only tries to remove once
        verify(mockMap, times(1)).removeActiveItem(collectable);
    }

    @Test
    public void testPickUpDecreasesDroppedLootIfWasDropped() {
        collectable.isItemDroppedLoot(true); //its dropped by an enemy
        collectable.pickUp();

        // make sure map is told to decrease dropped loot counter
        verify(mockMap, times(1)).decreaseDroppedLoot();
    }

    @Test
    public void testPickUpDoesNotDecreaseDroppedLootIfSpawnedNaturally() {
        collectable.isItemDroppedLoot(false); //natural spawn point item
        collectable.pickUp();

        //make sure map isnt told to touch loot counter
        verify(mockMap, never()).decreaseDroppedLoot();
    }
}