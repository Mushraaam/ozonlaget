package no.uib.inf112.model.npcs.factory;

import java.util.ArrayList;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.npcs.GigachadLV5;

public class Factory {
    private ArrayList<SpawnPoint> spawnPoints;
    private Random random;
    private int counter;
    private IModel map;

    private static final int GHOUL_INTENSITY = Config.getInt("ghoulIntensity");
    private static final int SPRINTER_INTENSITY = Config.getInt("sprinterIntensity");
    private static final int MEGABOSS_INTENSITY = Config.getInt("gigachadIntensity");
    private static final int MEGABOSS_CRITERIA = Config.getInt("gigachadSpawnCriteria");

    public Factory(IModel map){
        this.spawnPoints = map.getSpawnPoints();
        this.counter = 0;
        this.random = new Random();
        this.map = map;
    }

    /**
     * Increments counter - spawns enemy when counter reaches threshold
     */
    public void increment(){
        this.counter =(this.counter + 1) % 100000;
        
        if (this.counter % GHOUL_INTENSITY == 0){

            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            boolean successfullSpawn = false;
            int count = 0;
            while (!(successfullSpawn) && count < 10){
                successfullSpawn = point.spawnEnemy(EnemyType.GHOUL);
                point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
                count++;
            }
        }

        if (this.counter % SPRINTER_INTENSITY == 0){

            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            boolean successfullSpawn = false;
            int count = 0;
            while (!(successfullSpawn) && count < 10){
                successfullSpawn = point.spawnEnemy(EnemyType.SPRINTER);
                point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
                count++;
            }
        }

        if (this.counter % MEGABOSS_INTENSITY == 0 && map.getPlayer().getKillCount() >= MEGABOSS_CRITERIA) {
            boolean bossExists = map.getEnemies().stream()
                    .anyMatch(e -> e instanceof GigachadLV5);

            if (!bossExists) {
                if (!spawnPoints.isEmpty()) {
                    spawnPoints.getFirst().spawnEnemy(EnemyType.MEGABOSS);
                }
            }
        }

    }
}
