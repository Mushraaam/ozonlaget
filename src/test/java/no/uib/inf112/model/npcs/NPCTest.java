package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class NPCTest {

    private TestNPC npc;
    private IModel map;
    private IPlayer player;
    private IGrid grid;
    private Pathfinder pathfinder;
    private ItemFactory itemFactory;
    private ArrayList<IEnemy> enemies;
    private ArrayList<IStaticObject> staticObjects;

    @BeforeEach
    void setUp() {
        map = mock(IModel.class);
        player = mock(IPlayer.class);
        grid = mock(IGrid.class);
        pathfinder = mock(Pathfinder.class);
        itemFactory = mock(ItemFactory.class);

        enemies = new ArrayList<>();
        staticObjects = new ArrayList<>();

        when(map.getPlayer()).thenReturn(player);
        when(map.getGrid()).thenReturn(grid);
        when(map.getPathfinder()).thenReturn(pathfinder);
        when(map.getItemFactory()).thenReturn(itemFactory);
        when(map.getEnemies()).thenReturn(enemies);
        when(map.getStaticObjects()).thenReturn(staticObjects);
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        ICell cell = mock(ICell.class);
        when(grid.getCellFromPos(any(Rectangle2D.Double.class))).thenReturn(cell);
        when(pathfinder.canEnter(any(ICell.class), any())).thenReturn(true);

        npc = new TestNPC(new Rectangle2D.Double(100, 100, 40, 40), map, 100);
        npc.setSpeed(5);
        npc.setSize(EnemySize.MEDIUM);
        npc.setEnemyType(EnemyType.GHOUL);
        npc.setAnimationCount(8);
        npc.setAggroRange(500);

        enemies.add(npc);
    
    }

    @Test
    void constructorInitializesCorrectly() {
        assertEquals(100, npc.getHealth());
        assertEquals(100, npc.getMaxHealth());
        assertTrue(npc.isAlive());
        assertEquals(EnemyAction.WALK, npc.currentAction());
    }

    @Test
    void setActionChangeState() {
        npc.setAction(EnemyAction.ATTACK);
        assertEquals(EnemyAction.ATTACK, npc.currentAction());
    }



    private static class TestNPC extends NPC {

        int attackCalls = 0;
        int rangedAttackCalls = 0;

        TestNPC(Rectangle2D.Double pos, IModel map, int health) {
            super(pos, map, health);
        }

        @Override
        public void attack(Double target) {
            attackCalls++;
        }

        @Override
        protected void rangedAttack(Double attackTarget2) {
            rangedAttackCalls++;
        }
    }
}
