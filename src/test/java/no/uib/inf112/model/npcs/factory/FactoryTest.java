package no.uib.inf112.model.npcs.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.geom.Rectangle2D;
import java.beans.Transient;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IPlayer;

public class FactoryTest {

    private IModel model;
    private IPlayer player;
    private TestSpawnPoint spawnPoint;
    private ArrayList<SpawnPoint> spawnPoints;
    private ArrayList<IEnemy> enemies;
    private Factory factory;

    @BeforeEach
    void createFactory() {
        model = mock(IModel.class);
        player = mock(IPlayer.class);

        spawnPoint = new TestSpawnPoint(model);

        spawnPoints = new ArrayList<>();
        spawnPoints.add(spawnPoint);

        enemies = new ArrayList<>();

        when(model.getSpawnPoints()).thenReturn(spawnPoints);
        when(model.getEnemies()).thenReturn(enemies);
        when(model.getPlayer()).thenReturn(player);
        when(model.getDifficulty()).thenReturn(0);

        factory = new Factory(model);
    }

    @Test
    void enemyCapTest() {
        int enemyCap = Config.getInt("enemyCap");

        for (int i = 0; i < enemyCap; i++) {
            enemies.add(mock(IEnemy.class));
        }

        factory.increment();

        assertEquals(0, spawnPoint.getTotalSpawnAttempts());
    }

    @Test
    void ghoulIntensityTest() {
        int ghoulIntensity = Config.getInt("ghoulIntensity");

        for (int i = 0; i < ghoulIntensity; i++) {
            factory.increment();
        }

        assertTrue(spawnPoint.getGhoulSpawns() > 0);
    }

    @Test
    void sprinterIntensityTest() {
        int sprinterIntensity = Config.getInt("sprinterIntensity");

        for (int i = 0; i < sprinterIntensity; i++) {
            factory.increment();
        }

        assertTrue(spawnPoint.getSprinterSpawns() > 0);
    }

    @Test
    void bigHandsIntensityTest() {
        int bigHandsIntensity = Config.getInt("bighandsIntensity");

        for (int i = 0; i < bigHandsIntensity; i++) {
            factory.increment();
        }

        assertTrue(spawnPoint.getBigHandsSpawns() > 0);
    }

    @Test
    void tankIntensityTest() {
        int tankIntensity = Config.getInt("tankIntensity");

        for (int i = 0; i < tankIntensity; i++) {
            factory.increment();
        }

        assertTrue(spawnPoint.getTankSpawns() > 0);
    }

    @Test
    void retrySpawnTest() {
        spawnPoint.setShouldSpawn(false);

        int ghoulIntensity = Config.getInt("ghoulIntensity");

        for (int i = 0; i < ghoulIntensity; i++) {
            factory.increment();
        }

        assertEquals(10, spawnPoint.getGhoulSpawns());
    }

    @Test
    void killCriteriaIsNotReachedTest() {
        int bossIntensity = Config.getInt("gigachadIntensity");
        int bossCriteria = Config.getInt("gigachadSpawnCriteria");

        when(player.getKillCount()).thenReturn(bossCriteria - 1);

        for (int i = 0; i < bossIntensity; i++) {
            factory.increment();
        }

        assertEquals(0, spawnPoint.getMegaBossSpawns());
    }

    @Test
    void killCriteriaIsReachedTest() {
        int bossIntensity = Config.getInt("gigachadIntensity");
        int bossCriteria = Config.getInt("gigachadSpawnCriteria");

        when(player.getKillCount()).thenReturn(bossCriteria);

        for (int i = 0; i < bossIntensity; i++) {
            factory.increment();
        }

        assertEquals(1, spawnPoint.getMegaBossSpawns());
    }

    @Test
    void bossOnlySpawnsOnceTest() {
        int bossIntensity = Config.getInt("gigachadIntensity");
        int bossCriteria = Config.getInt("gigachadSpawnCriteria");

        when(player.getKillCount()).thenReturn(bossCriteria);

        for (int i = 0; i < bossIntensity; i++) {
            factory.increment();
        }

        for (int i = 0; i < bossIntensity; i++) {
            factory.increment();
        }

        assertEquals(1, spawnPoint.getMegaBossSpawns());
    }

    private static class TestSpawnPoint extends SpawnPoint {

        private int ghoulSpawns;
        private int sprinterSpawns;
        private int bigHandsSpawns;
        private int tankSpawns;
        private int megaBossSpawns;
        private boolean shouldSpawn = true;

        public TestSpawnPoint(IModel model) {
            super(model, new Rectangle2D.Double(0, 0, 100, 100));
        }

        @Override
        public boolean spawnEnemy(EnemyType type) {
            switch (type) {
                case GHOUL -> ghoulSpawns++;
                case SPRINTER -> sprinterSpawns++;
                case BIGHANDS -> bigHandsSpawns++;
                case TANK -> tankSpawns++;
                case MEGABOSS -> megaBossSpawns++;
                default -> {
                }
            }

            return shouldSpawn;
        }

        public void setShouldSpawn(boolean shouldSpawn) {
            this.shouldSpawn = shouldSpawn;
        }

        public int getGhoulSpawns() {
            return ghoulSpawns;
        }

        public int getSprinterSpawns() {
            return sprinterSpawns;
        }

        public int getBigHandsSpawns() {
            return bigHandsSpawns;
        }

        public int getTankSpawns() {
            return tankSpawns;
        }

        public int getMegaBossSpawns() {
            return megaBossSpawns;
        }

        public int getTotalSpawnAttempts() {
            return ghoulSpawns + sprinterSpawns + bigHandsSpawns + tankSpawns + megaBossSpawns;
        }
    }
}
