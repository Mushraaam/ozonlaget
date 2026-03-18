package no.uib.inf112.map.items.factory;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;

public class ItemFactory {
    private ItemSpawnPoint itemSpawnPoint;
    private int counter;

    private static final int HEALTH_RATE = Config.getInt("healthBox");
    private static final int ARMOR_RATE = Config.getInt("armor");
    private static final int AMMO_RATE = Config.getInt("ammoBox");
    private static final int SPEED_RATE = Config.getInt("powerup_Speed");
    private static final int DAMAGE_RATE = Config.getInt("powerup_Damage");
    private static final int RAINBOW_RATE = Config.getInt("powerup_Rainbow");




    public ItemFactory(IMap map){
        this.itemSpawnPoint = new ItemSpawnPoint(map, map.getItemSpawnPoints());
        this.counter = 0;
    }

    public void increment(){
        this.counter = (this.counter + 1) % 100000;

        if (this.counter % HEALTH_RATE == 0){
            this.itemSpawnPoint.spawnItem(CollectableType.HEALTH);
        }

        if (this.counter % ARMOR_RATE == 0){
            this.itemSpawnPoint.spawnItem(CollectableType.ARMOR);
        }
        if (this.counter % RAINBOW_RATE == 0){
            this.itemSpawnPoint.spawnItem(CollectableType.POWERUP_RAINBOW);
        }
        /*

        if (this.counter % SPEED_RATE == 0){
            this.itemSpawnPoint.spawnItem(CollectableType.POWERUP_SPEED);
        }

        if (this.counter % DAMAGE_RATE == 0){
            this.itemSpawnPoint.spawnItem(CollectableType.POWERUP_DAMAGE);
        }*/
    }
}