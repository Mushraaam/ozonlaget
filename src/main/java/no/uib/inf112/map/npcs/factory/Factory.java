package no.uib.inf112.map.npcs.factory;

import java.util.ArrayList;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IMap;

public class Factory {
    private IMap map;
    private ArrayList<SpawnPoint> spawnPoints;
    private Random random;
    private int counter;

    private static final int GHOUL_INTENSITY = Config.getInt("ghoulIntensity");

    public Factory(IMap map){
        this.map = map;
        this.spawnPoints = map.getSpawnPoints();
        this.counter = 0;
        this.random = new Random();
    }

    /**
     * Increments counter - spawns enemy when counter reaches threshold
     */
    public void increment(){
        this.counter = this.counter % 100000;
        
        if (this.counter % GHOUL_INTENSITY == 0){
            SpawnPoint point = this.spawnPoints.get(this.random.nextInt(0, this.spawnPoints.size()));
            point.spawnEnemy(EnemyType.GHOUL);
        }

    }
}
