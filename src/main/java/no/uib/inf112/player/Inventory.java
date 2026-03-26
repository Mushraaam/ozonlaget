package no.uib.inf112.player;

import no.uib.inf112.enums.CollectableType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Inventory {
    private boolean visible = false;
    private HashMap<CollectableType, Integer> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
    }

    public void toggleVisible() {
        this.visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Check how many of a given item is in the inventory
     * 
     * @param item
     * @return an int of how many of this item the player holds.
     */
    public int getAmountInInventory(CollectableType item) {
        return inventory.getOrDefault(item, 0);
    }

    /**
     * @return A read-only set of all items and their quantities.
     */
    public Set<Map.Entry<CollectableType, Integer>> getItems() {
        return Collections.unmodifiableSet(inventory.entrySet());
    }

    /**
     * Adds a collectable to the player's inventory.
     * 
     * @param item
     */
    public void addToInventory(CollectableType item) {
        inventory.put(item, inventory.getOrDefault(item, 0) + 1);
    }

    public boolean useItemFromInventory(CollectableType item) {
        if (!inventory.containsKey(item))
            return false;

        int count = inventory.get(item);
        if (count > 1) {
            inventory.put(item, count - 1);
        } else {
            inventory.remove(item);
        }
        return true;
    }
}
