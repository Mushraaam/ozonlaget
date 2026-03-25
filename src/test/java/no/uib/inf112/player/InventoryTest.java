package no.uib.inf112.player;

import no.uib.inf112.enums.CollectableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = new Inventory();
    }

    @Test
    public void testInventoryIsInitiallyHiddenAndEmpty() {
        assertFalse(inventory.isVisible(), "Inventory should be hidden upon creation");
        assertTrue(inventory.getItems().isEmpty(), "Inventory should start completely empty");
        assertEquals(0, inventory.getAmountInInventory(CollectableType.GASCAN));
    }

    @Test
    public void testToggleVisible() {
        inventory.toggleVisible();
        assertTrue(inventory.isVisible(), "Inventory should be visible after one toggle");

        inventory.toggleVisible();
        assertFalse(inventory.isVisible(), "Inventory should be hidden after second toggle");

        inventory.toggleVisible();
        assertTrue(inventory.isVisible(), "Inventory should be visible after third toggle, you get the idea.");


    }

    @Test
    public void testAddToInventoryIncrementsCount() {
        // add first item
        inventory.addToInventory(CollectableType.GASCAN);
        assertEquals(1, inventory.getAmountInInventory(CollectableType.GASCAN));

        // add second of the same item
        inventory.addToInventory(CollectableType.GASCAN);
        assertEquals(2, inventory.getAmountInInventory(CollectableType.GASCAN));

        // make sure other items are still 0
        assertEquals(0, inventory.getAmountInInventory(CollectableType.GATEKEY));
    }

    @Test
    public void testUseItemFromInventoryWithMultipleItems() {
        // give player 2 gas cans
        inventory.addToInventory(CollectableType.GASCAN);
        inventory.addToInventory(CollectableType.GASCAN);

        // use one
        boolean success = inventory.useItemFromInventory(CollectableType.GASCAN);

        assertTrue(success, "Using item should return true when player has it");
        assertEquals(1, inventory.getAmountInInventory(CollectableType.GASCAN), "Count should drop from 2 to 1");
    }

    @Test
    public void testUseItemFromInventoryRemovesLastItem() {
        // give player one gatekey
        inventory.addToInventory(CollectableType.GATEKEY);

        // Use it
        boolean success = inventory.useItemFromInventory(CollectableType.GATEKEY);

        assertTrue(success, "Using item should return true when player has it");
        assertEquals(0, inventory.getAmountInInventory(CollectableType.GATEKEY), "Count should drop to 0");

        // make sure it was actually removed from the HashMap, key and value
        assertFalse(inventory.getItems().stream().anyMatch(entry -> entry.getKey() == CollectableType.GATEKEY),
                "Key should be completely removed from the map when count hits 0");
    }

    @Test
    public void testUseItemFromInventoryFailsIfItemNotOwned() {
        boolean success = inventory.useItemFromInventory(CollectableType.CHOPPERKEY);

        assertFalse(success, "Using an item the player doesn't have should return false");
        assertEquals(0, inventory.getAmountInInventory(CollectableType.CHOPPERKEY));
    }

    @Test
    public void testGetItemsReturnsUnmodifiableSet() {
        inventory.addToInventory(CollectableType.GASCAN);
        Set<Map.Entry<CollectableType, Integer>> items = inventory.getItems();

        // trying to clear or modify this set should throw an exception to protect the inventory's state
        assertThrows(UnsupportedOperationException.class, items::clear, "getItems() must return an unmodifiable set to protect encapsulation");
    }
}