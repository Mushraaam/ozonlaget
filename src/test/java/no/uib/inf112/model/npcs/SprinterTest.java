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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SprinterTest {

    private Sprinter sprinter;
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

        sprinter = new Sprinter(new Rectangle2D.Double(100, 100, 40, 40), map);
    }

    @Test
    void constructorSetsCorrectTypeAndSize() {
        assertEquals(EnemyType.SPRINTER, sprinter.getEnemyType());
        assertEquals(EnemySize.SMALL, sprinter.size());
        assertFalse(sprinter.hasRangedAmmo);
    }

    @Test
    void attackDamagesPlayer() {
        sprinter.aggroed = true;

        for (int i = 0; i < 50; i++) {
            sprinter.move(grid);
        }

        verify(player).takeDamage(2);
    }

    @Test
    void attackSwitchesToWalkWhenAnimationIndexIsZeroAndPlayerIsOutOfRange() {
        sprinter.setAction(EnemyAction.ATTACK);

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        sprinter.attack(player.getHitbox());

        assertEquals(EnemyAction.WALK, sprinter.currentAction());
    }

    @Test
    void rangedAttackDoesNothing() {
        sprinter.rangedAttack(new Rectangle2D.Double(130, 100, 40, 40));

        verify(player, never()).takeDamage(anyInt());
        verify(map, never()).addProjectile(any());
    }

    @Test
    void deathAnimationUsesSprinterIndexes() {
        sprinter.deathDelay = 200;
        sprinter.setDeathAnimationIndex();
        assertEquals(0, sprinter.getAnimationIndex());

        sprinter.deathDelay = 188;
        sprinter.setDeathAnimationIndex();
        assertEquals(1, sprinter.getAnimationIndex());

        sprinter.deathDelay = 186;
        sprinter.setDeathAnimationIndex();
        assertEquals(2, sprinter.getAnimationIndex());

        sprinter.deathDelay = 183;
        sprinter.setDeathAnimationIndex();
        assertEquals(3, sprinter.getAnimationIndex());

        sprinter.deathDelay = 181;
        sprinter.setDeathAnimationIndex();
        assertEquals(4, sprinter.getAnimationIndex());

        sprinter.deathDelay = 173;
        sprinter.setDeathAnimationIndex();
        assertEquals(5, sprinter.getAnimationIndex());

        sprinter.deathDelay = 166;
        sprinter.setDeathAnimationIndex();
        assertEquals(6, sprinter.getAnimationIndex());

        sprinter.deathDelay = 158;
        sprinter.setDeathAnimationIndex();
        assertEquals(7, sprinter.getAnimationIndex());

        sprinter.deathDelay = 151;
        sprinter.setDeathAnimationIndex();
        assertEquals(8, sprinter.getAnimationIndex());

        sprinter.deathDelay = 100;
        sprinter.setDeathAnimationIndex();
        assertEquals(9, sprinter.getAnimationIndex());
    }
}