package no.uib.inf112.map.items.factory;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.items.ArmorBox;
import no.uib.inf112.map.items.HealthBox;

public class ItemSpawnPoint {
    private IMap map;
    private Random random;

    private Map<Rectangle2D.Double, ICollectable> spawnPoints;

    private Map<CollectableType, Integer> maxLimits;

    public ItemSpawnPoint(IMap map, List<Rectangle2D.Double> predefinedSpots) {
        this.map = map;
        this.random = new Random();


        this.spawnPoints = new HashMap<>();
        for (Rectangle2D.Double spot : predefinedSpots) {
            this.spawnPoints.put(spot, null);
        }

        this.maxLimits = new EnumMap<>(CollectableType.class);
        this.maxLimits.put(CollectableType.HEALTH, Config.getInt("healthBoxCap"));
        this.maxLimits.put(CollectableType.ARMOR, Config.getInt("armor"));
        this.maxLimits.put(CollectableType.POWERUP_SPEED, Config.getInt("powerup_SpeedCap"));
        this.maxLimits.put(CollectableType.POWERUP_DAMAGE, Config.getInt("powerup_DamageCap"));
        this.maxLimits.put(CollectableType.POWERUP_RAINBOW, Config.getInt("powerup_RainbowCap"));

    }

    public boolean spawnItem(CollectableType type) {
        refreshNodes();

        if (getCurrentCount(type) >= maxLimits.getOrDefault(type, 0)) {
            System.out.println("max lim reached");
            return false; // Limit reached, abort!
        }

        // Find empty spawn points
        List<Rectangle2D.Double> emptySpots = new ArrayList<>();
        for (Map.Entry<Rectangle2D.Double, ICollectable> entry : spawnPoints.entrySet()) {
            if (entry.getValue() == null) {
                emptySpots.add(entry.getKey());
            }
        }

        // Wont spawn if there's no spots left on the map
        if (emptySpots.isEmpty()) {
            System.out.println("no spots");
            return false;
        }

        Rectangle2D.Double chosenSpot = emptySpots.get(random.nextInt(emptySpots.size()));


        ICollectable newItem = createItem(type, chosenSpot);
        this.map.addToActiveItems(newItem);
        this.spawnPoints.put(chosenSpot, newItem);

        System.out.println("spawned item");
        return true;
    }

    /**
     * Checks if the items in the points have been picked up.
     * If they aren't in the map's active item list anymore, they are probably gone...
     */
    private void refreshNodes() {
        List<ICollectable> activeItems = map.getActiveItems();
        for (Map.Entry<Rectangle2D.Double, ICollectable> entry : spawnPoints.entrySet()) {
            ICollectable item = entry.getValue();
            if (item != null && !activeItems.contains(item)) {
                spawnPoints.put(entry.getKey(), null);
            }
        }
    }

    private int getCurrentCount(CollectableType type) {
        int count = 0;
        for (ICollectable item : map.getActiveItems()) {
            if (item.getType() == type) {
                count++;
            }
        }
        return count;
    }
    private ICollectable createItem(CollectableType type, Rectangle2D.Double hitBox) {
        switch (type) {
            case HEALTH -> {
                return new HealthBox(hitBox, CollectableType.HEALTH, map);
            }
            case ARMOR -> {
                return new ArmorBox(hitBox, CollectableType.ARMOR, map);
            }
            default -> throw new IllegalArgumentException("Unknown Item Type");
        }
    }
}