package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.GameState;
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

public class BigHandsTest {

    private BigHands bigHands;
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

        bigHands = new BigHands(new Rectangle2D.Double(100, 100, 40, 40), map);
    }

    @Test
    void constructorSetsCorrectTypeAndSize() {
        assertEquals(EnemyType.BIGHANDS, bigHands.getEnemyType());
        assertEquals(EnemySize.MEDIUM, bigHands.size());
    }

    @Test
    void attackDamagesPlayerWhenAnimationHits() {
        bigHands.aggroed = true;

        for (int i = 0; i < 50; i++) {
            bigHands.move(grid);
        }

        verify(player).takeDamage(4);
    }

    @Test
    void rangedAttackDoesNothing() {
        Rectangle2D.Double target = new Rectangle2D.Double(130, 100, 40, 40);

        bigHands.rangedAttack(target);

        verify(player, never()).takeDamage(anyInt());
        verify(map, never()).addProjectile(any());
    }
}