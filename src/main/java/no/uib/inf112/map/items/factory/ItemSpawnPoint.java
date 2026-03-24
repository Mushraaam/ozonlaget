package no.uib.inf112.map.items.factory;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.items.buffs.*;
import no.uib.inf112.map.items.collectable_obj.InventoryItem;

public class ItemSpawnPoint {
    private IMap map;
    private Random random;

    private Map<Rectangle2D.Double, ICollectable> spawnPoints;

    private Map<CollectableType, Integer> maxLimits;

    private Map<CollectableType, Integer> totalInventoryItemsOnMap;
    int MAX_DROPPED_LOOT = 10;

    public ItemSpawnPoint(IMap map, List<Rectangle2D.Double> preDeterminedSpots) {
        this.map = map;
        this.random = new Random();


        this.spawnPoints = new LinkedHashMap<>();
        for (Rectangle2D.Double spot : preDeterminedSpots) {
            this.spawnPoints.put(spot, null);
        }

        this.maxLimits = new EnumMap<>(CollectableType.class);
        this.maxLimits.put(CollectableType.AMMO_SHOTGUN, Config.getInt("ammoShotgunCap"));
        this.maxLimits.put(CollectableType.AMMO_RIFLE, Config.getInt("ammoRifleCap"));
        this.maxLimits.put(CollectableType.AMMO_PISTOL, Config.getInt("ammoPistolCap"));
        this.maxLimits.put(CollectableType.HEALTH, Config.getInt("healthBoxCap"));
        this.maxLimits.put(CollectableType.ARMOR, Config.getInt("armorCap"));
        this.maxLimits.put(CollectableType.POWERUP_SPEED, Config.getInt("powerup_SpeedCap"));
        this.maxLimits.put(CollectableType.POWERUP_DAMAGE, Config.getInt("powerup_DamageCap"));
        this.maxLimits.put(CollectableType.POWERUP_RAINBOW, Config.getInt("powerup_RainbowCap"));


        //Inventory items
        this.totalInventoryItemsOnMap = new EnumMap<>(CollectableType.class);
        this.totalInventoryItemsOnMap.put(CollectableType.GATEKEY, 1);
        this.totalInventoryItemsOnMap.put(CollectableType.GASCAN, 6);

    }


    public void dropLoot(CollectableType itemType, Rectangle2D.Double targetLocation){
        if(map.getTotalDroppedLoot() >= this.MAX_DROPPED_LOOT){
            System.out.println("too many dropped items");
            return;
        }
        ICollectable newItem = createItem(itemType, targetLocation);
        this.map.addToActiveItems(newItem);
        newItem.isItemDroppedLoot(true);
        map.increaseDroppedLoot();
    }


    public void spawnBuffItem(CollectableType type) {
        refreshNodes();

        if (getCurrentCount(type) >= maxLimits.getOrDefault(type, 0)) {
            System.out.println("max lim reached");
            return; // Limit reached, abort!
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
            return;
        }

        Rectangle2D.Double chosenSpot = emptySpots.get(random.nextInt(emptySpots.size()));


        ICollectable newItem = createItem(type, chosenSpot);
        this.map.addToActiveItems(newItem);
        this.spawnPoints.put(chosenSpot, newItem);

        System.out.println("spawned item");
    }

    public void spawnInventoryItems() {
        List<Rectangle2D.Double> allSpots = new ArrayList<>(spawnPoints.keySet());


        // key gets first spot for consistency
        Rectangle2D.Double gateKeySpot = allSpots.getFirst();
        ICollectable gateKey = createItem(CollectableType.GATEKEY, gateKeySpot);
        this.map.addToActiveItems(gateKey);
        this.spawnPoints.put(gateKeySpot, gateKey);

        allSpots.removeFirst(); //exclude key spot

        Collections.shuffle(allSpots, this.random);

        int gasCansToSpawn = totalInventoryItemsOnMap.getOrDefault(CollectableType.GASCAN, 0);

        for (int i = 0; i < gasCansToSpawn; i++) {
            if (allSpots.isEmpty()) {
                System.out.println("no more spots for gascans");
                break;
            }
            Rectangle2D.Double spot = allSpots.removeFirst();

            ICollectable gasCan = createItem(CollectableType.GASCAN, spot);
            this.map.addToActiveItems(gasCan);
            this.spawnPoints.put(spot, gasCan);
        }

        System.out.println("Spawned all inventory items!");
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
                return new HealthBox(hitBox, type, map);
            }
            case AMMO_PISTOL,AMMO_RIFLE,AMMO_SHOTGUN -> {
                return new Ammo(hitBox, type, map);
            }
            case ARMOR -> {
                return new ArmorBox(hitBox, type, map);
            }
            case POWERUP_RAINBOW -> {
                return new RainbowBuff(hitBox, type, map);
            }
            case POWERUP_DAMAGE -> {
                return new DamageBuff(hitBox, type, map);
            }
            case POWERUP_SPEED -> {
                return new SpeedBuff(hitBox, type, map);
            }
            case GATEKEY, GASCAN -> {
                return new InventoryItem(hitBox, type, map);}

            default -> throw new IllegalArgumentException("Unknown Item Type");
        }
    }
}