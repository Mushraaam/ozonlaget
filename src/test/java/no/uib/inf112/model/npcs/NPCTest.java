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

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void takeDamageReducesHealth() {
        npc.takeDamage(30);
        assertEquals(70, npc.getHealth());
    }

    @Test
    void takeDamageKillsNpc() {
        boolean died = npc.takeDamage(100);

        assertTrue(died);
        assertFalse(npc.isAlive());
        assertEquals(0, npc.getHealth());
        assertEquals(EnemyAction.DEAD, npc.currentAction());
    }

    @Test
    void takeDamageCannotGoBelowZero() {
        npc.takeDamage(999);
        assertEquals(0, npc.getHealth());
    }

     @Test
    void distanceCalculatesCorrectly() {
        Rectangle2D.Double a = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double b = new Rectangle2D.Double(3, 4, 10, 10);

        assertEquals(5.0, npc.distance(a, b));
    }

    @Test
    void inMeleeRangeTrueWhenClose() {
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(130, 100, 40, 40));
        assertTrue(npc.inMeleeRange());
    }

    @Test
    void inMeleeRangeFalseWhenFar() {
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));
        assertFalse(npc.inMeleeRange());
    }

    @Test
    void isLegalFalseWhenCollidingWithPlayer() {
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(105, 105, 20, 20));

        Rectangle2D.Double candidate = new Rectangle2D.Double(100, 100, 40, 40);

        assertFalse(npc.isLegal(candidate));
    }

    @Test
    void isLegalFalseWhenCellIsNull() {
        when(grid.getCellFromPos(any())).thenReturn(null);

        assertFalse(npc.isLegal(new Rectangle2D.Double(200, 200, 40, 40)));
    }

    @Test
    void isLegalFalseWhenBlockedByPathfinder() {
        ICell cell = mock(ICell.class);
        when(grid.getCellFromPos(any())).thenReturn(cell);
        when(pathfinder.canEnter(cell, EnemySize.MEDIUM)).thenReturn(false);

        assertFalse(npc.isLegal(new Rectangle2D.Double(200, 200, 40, 40)));
    }

    @Test
    void isLegalTrueWhenFree() {
        ICell cell = mock(ICell.class);
        when(grid.getCellFromPos(any())).thenReturn(cell);
        when(pathfinder.canEnter(cell, EnemySize.MEDIUM)).thenReturn(true);

        assertTrue(npc.isLegal(new Rectangle2D.Double(200, 200, 40, 40)));
    }

    @Test
    void isLegalFalseWhenCollidingWithAliveEnemy() {
        IEnemy other = mock(IEnemy.class);
        when(other.isAlive()).thenReturn(true);
        when(other.getHitbox()).thenReturn(new Rectangle2D.Double(205, 205, 40, 40));

        enemies.add(other);

        assertFalse(npc.isLegal(new Rectangle2D.Double(200, 200, 40, 40)));
    }

    @Test
    void isLegalIgnoresDeadEnemy() {
        IEnemy dead = mock(IEnemy.class);
        when(dead.isAlive()).thenReturn(false);
        when(dead.getHitbox()).thenReturn(new Rectangle2D.Double(205, 205, 40, 40));

        enemies.add(dead);

        assertTrue(npc.isLegal(new Rectangle2D.Double(200, 200, 40, 40)));
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
