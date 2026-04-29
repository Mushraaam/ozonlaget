package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.*;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KamikazeBugTest {

    private KamikazeBug bug;
    private IModel map;
    private IPlayer player;
    private IGrid grid;
    private Pathfinder pathfinder;

    @BeforeEach
    void setUp() {
        map = mock(IModel.class);
        player = mock(IPlayer.class);
        grid = mock(IGrid.class);
        pathfinder = mock(Pathfinder.class);

        when(map.getPlayer()).thenReturn(player);
        when(map.getGrid()).thenReturn(grid);
        when(map.getPathfinder()).thenReturn(pathfinder);
        when(map.getEnemies()).thenReturn(new ArrayList<>());
        when(map.getStaticObjects()).thenReturn(new ArrayList<>());
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        ICell cell = mock(ICell.class);
        when(grid.getCellFromPos(any(Rectangle2D.Double.class))).thenReturn(cell);
        when(pathfinder.canEnter(any(ICell.class), any(EnemySize.class))).thenReturn(true);

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        bug = new KamikazeBug(new Rectangle2D.Double(100, 100, 40, 40), map);
    }

    @Test
    void constructorSetsCorrectTypeSizeAndAggro() {
        assertEquals(EnemyType.BUG, bug.getEnemyType());
        assertEquals(EnemySize.SMALL, bug.size());
        assertTrue(bug.aggroed);
    }

    @Test
    void attackCreatesExplosionAndRemovesEnemy() {
        bug.attack(new Rectangle2D.Double(100, 100, 40, 40));

        verify(map).addAOEPuddle(any());
        verify(map).removeEnemy(bug);
        assertFalse(bug.isAlive());
    }

    @Test
    void moveExplodesWhenTouchingPlayer() {
        bug.aggroed = false;
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(100, 100, 40, 40));

        bug.move(grid);

        verify(map).addAOEPuddle(any());
        assertFalse(bug.isAlive());
    }

    @Test
    void moveDoesNotExplodeWhenNotTouchingPlayer() {
        bug.aggroed = false;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        bug.move(grid);

        verify(map, never()).addAOEPuddle(any());
        assertTrue(bug.isAlive());
    }

    @Test
    void rangedAttackDoesNothing() {
        bug.rangedAttack(new Rectangle2D.Double(100, 100, 40, 40));

        verify(map, never()).addProjectile(any());
        verify(map, never()).addAOEPuddle(any());
    }
}