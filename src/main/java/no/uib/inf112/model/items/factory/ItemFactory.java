package no.uib.inf112.model.items.factory;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IModel;
import org.w3c.dom.css.Rect;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Random;

public class ItemFactory {
    private ItemSpawnPoint buffItemSpawnPoint;
    private ItemSpawnPoint inventoryItemSpawnPoint;
    private int counter;

    private static final int HEALTH_RATE = Config.getInt("healthBox");
    private static final int ARMOR_RATE = Config.getInt("armor");
    private static final int AMMO_RATE = Config.getInt("ammoBox");
    private static final int SPEED_RATE = Config.getInt("powerup_Speed");
    private static final int DAMAGE_RATE = Config.getInt("powerup_Damage");
    private static final int RAINBOW_RATE = Config.getInt("powerup_Rainbow");
    private static final Random RANDOM = new Random();


    private HashMap<CollectableType, Integer> dropTable = new HashMap<>();

    public void setDropTable() {
        dropTable.put(CollectableType.HEALTH, 3);
        dropTable.put(CollectableType.ARMOR, 2);
        dropTable.put(CollectableType.AMMO_PISTOL, 8);
        dropTable.put(CollectableType.AMMO_RIFLE, 8);
        dropTable.put(CollectableType.AMMO_SHOTGUN, 8);
        dropTable.put(CollectableType.NONE, 71);
    }

    public ItemFactory(IModel map){
        this.buffItemSpawnPoint = new ItemSpawnPoint(map, map.getBuffItemSpawnpoint());
        this.inventoryItemSpawnPoint = new ItemSpawnPoint(map, map.getInventoryItemSpawnpoint());
        this.counter = 0;
        setDropTable();
    }

    public void rollDropFromTable(Rectangle2D.Double targetLocation){
        int totweight = 0;
        for(int weight : dropTable.values()){
            totweight+=weight;
        }
        int roll = RANDOM.nextInt(totweight)+1;
        for (HashMap.Entry<CollectableType, Integer> item : dropTable.entrySet()) {
            roll -= item.getValue();
            if (roll <= 0) {
                if (item.getKey() != CollectableType.NONE) {
                    this.buffItemSpawnPoint.dropLoot(item.getKey(), targetLocation);
                }
                break;
            }
        }
    }

    public void dropSpecificItem(CollectableType type, Rectangle2D.Double targetLocation){
        this.inventoryItemSpawnPoint.dropEssentialItem(type, targetLocation);
    }

    public void increment(){
        this.counter = (this.counter + 1) % 100000;

        if (this.counter % HEALTH_RATE == 0){
            this.buffItemSpawnPoint.spawnBuffItem(CollectableType.HEALTH);
        }

        if (this.counter % ARMOR_RATE == 0){
            this.buffItemSpawnPoint.spawnBuffItem(CollectableType.ARMOR);
        }
        if (this.counter % RAINBOW_RATE == 0){
            this.buffItemSpawnPoint.spawnBuffItem(CollectableType.POWERUP_RAINBOW);
        }
        if (this.counter % DAMAGE_RATE == 0) {
            this.buffItemSpawnPoint.spawnBuffItem(CollectableType.POWERUP_DAMAGE);
        }
        if (this.counter % SPEED_RATE == 0){
            this.buffItemSpawnPoint.spawnBuffItem(CollectableType.POWERUP_SPEED);
        }
    }

    public void spawnInventoryItems(

    ) {
        this.inventoryItemSpawnPoint.spawnInventoryItems();
    }
}