package no.uib.inf112.model.npcs.factory;

import java.util.ArrayList;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IModel;

public class Factory {

    private static final int ENEMY_CAP = Config.getInt("enemyCap");
    private ArrayList<SpawnPoint> spawnPoints;
    private Random random;
    private int counter;
    private IModel model;
    private boolean bossAlreadySpawned = false;

    private static final int GHOUL_INTENSITY = Config.getInt("ghoulIntensity");
    private static final int SPRINTER_INTENSITY = Config.getInt("sprinterIntensity");
    private static final int BIGHANDS_INTENSITY = Config.getInt("bighandsIntensity");
    private static final int MEGABOSS_INTENSITY = Config.getInt("gigachadIntensity");
    private static final int MEGABOSS_CRITERIA = Config.getInt("gigachadSpawnCriteria");

    public Factory(IModel model) {
        this.model = model;
        this.spawnPoints = model.getSpawnPoints();
        this.counter = 0;
        this.random = new Random();
    }

    /**
     * Increments counter - spawns enemy when counter reaches threshold
     */
    public void increment() {

        double difficulty = (double)this.model.getDifficulty() + 1; //add 1 to avoid zero division

        if (this.model.getEnemies().size() >= ENEMY_CAP) {
            return;
        }
        
        this.counter = (this.counter + 1) % 100000;

        if (this.counter % Math.floor(GHOUL_INTENSITY / difficulty) == 0) {

            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            boolean successfullSpawn = false;
            int count = 0;
            while (!(successfullSpawn) && count < 10) {
                successfullSpawn = point.spawnEnemy(EnemyType.GHOUL);
                point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
                count++;
            }
        }

        if (this.counter % Math.floor(SPRINTER_INTENSITY / difficulty) == 0) {

            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            boolean successfullSpawn = false;
            int count = 0;
            while (!(successfullSpawn) && count < 10) {
                successfullSpawn = point.spawnEnemy(EnemyType.SPRINTER);
                point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
                count++;
            }
        }

        if (this.counter % Math.floor(BIGHANDS_INTENSITY / difficulty) == 0) {

            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            boolean successfullSpawn = false;
            int count = 0;
            while (!(successfullSpawn) && count < 10) {
                successfullSpawn = point.spawnEnemy(EnemyType.BIGHANDS);
                point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
                count++;
            }
        }

        if ((!this.bossAlreadySpawned) 
            && this.counter % MEGABOSS_INTENSITY == 0 
            && model.getPlayer().getKillCount() >= MEGABOSS_CRITERIA 
            && !spawnPoints.isEmpty() 
            && spawnPoints.getFirst().spawnEnemy(EnemyType.MEGABOSS)) {
            this.bossAlreadySpawned = true;
        }
    }
}
