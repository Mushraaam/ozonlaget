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

public class GhoulTest {

    private Ghoul ghoul;
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

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(130, 100, 40, 40));

        ghoul = new Ghoul(new Rectangle2D.Double(100, 100, 40, 40), map);
    }

    @Test
    void constructorSetsCorrectTypeAndSize() {
        assertEquals(EnemyType.GHOUL, ghoul.getEnemyType());
        assertEquals(EnemySize.MEDIUM, ghoul.size());
        assertTrue(ghoul.hasRangedAmmo);
    }

    @Test
    void attackDamagesPlayer() {
        ghoul.aggroed = true;
        ghoul.hasRangedAmmo = false;

        for (int i = 0; i < 50; i++) {
            ghoul.move(grid);
        }

        verify(player).takeDamage(2);
    }

    @Test
    void rangedAttackCreatesProjectileAndStopsShooting() {
        ghoul.aggroed = true;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(150, 100, 40, 40));

        for (int i = 0; i < 100; i++) {
            ghoul.move(grid);
        }

        verify(map, atLeastOnce()).addProjectile(any());
        assertFalse(ghoul.hasRangedAmmo);
        assertEquals(EnemyAction.WALK, ghoul.currentAction());
    }

    @Test
    void attackSwitchesToWalkWhenAnimationIndexIsZeroAndPlayerIsOutOfRange() {
        ghoul.setAction(EnemyAction.ATTACK);

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        ghoul.attack(player.getHitbox());

        assertEquals(EnemyAction.WALK, ghoul.currentAction());
    }
}