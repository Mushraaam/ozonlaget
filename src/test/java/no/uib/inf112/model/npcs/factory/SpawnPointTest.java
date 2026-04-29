package no.uib.inf112.model.npcs.factory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.model.Model;
import no.uib.inf112.model.levels.Level1;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

public class SpawnPointTest {
    SpawnPoint spawnPoint;

    private MockedConstruction<SoundHandler> mockedSoundHandler;
    private MockedConstruction<Level1> mockedLevel1;
    private Model model;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model(mock(LoadStatus.class));
        spawnPoint = new SpawnPoint(model,
                new Rectangle2D.Double(10000, 10000, 100000, 100000)); // spawnpoint far away from walls etc for
                                                                       // consistency
    }

    boolean containsEnemyOfType(EnemyType type, List<IEnemy> list) {
        for (IEnemy enemy : list) {
            if (enemy.getEnemyType() == type) {
                return true;
            }
        }
        return false;
    }

    @Test
    void spawnEnemiesTest() {
        for (EnemyType type : EnemyType.values()) {
            if (type == EnemyType.BUG) {
                continue; // spawnpoint does not spawn bugs
            }
            assertTrue(spawnPoint.spawnEnemy(type));
            assertTrue(containsEnemyOfType(type, model.getEnemies()));

        }
    }

}
