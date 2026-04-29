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
import static org.mockito.ArgumentMatchers.eq;
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
        when(pathfinder.canEnter(any(ICell.class), any(EnemySize.class))).thenReturn(true);

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

    @Test
    void requestPathDoesNothingIfCellsAreNull() {
        when(grid.getCellFromPos(any())).thenReturn(null);

        npc.requestPath(grid, pathfinder, new Rectangle2D.Double(), false);

        verify(pathfinder, never()).findPath(any(), any(), any(), any(), any());
    }

    @Test
    void requestPathUpdatesPath() {
        ICell start = mock(ICell.class);
        ICell goal = mock(ICell.class);

        Rectangle2D.Double target = new Rectangle2D.Double(300, 300, 40, 40);

        when(grid.getCellFromPos(npc.getHitbox())).thenReturn(start);
        when(grid.getCellFromPos(target)).thenReturn(goal);

        List<ICell> path = List.of(start, goal);

        when(pathfinder.findPath(eq(npc), eq(start), eq(goal), eq(EnemySize.MEDIUM), any()))
                .thenReturn(path);

        npc.requestPath(grid, pathfinder, target, false);

        assertEquals(path, npc.getCurrentPath());
    }

    @Test
    void moveDoesNothingWhenGameNotActive() {
        when(map.getGameState()).thenReturn(GameState.MAIN_MENU);

        Rectangle2D before = npc.getHitbox();

        npc.move(grid);

        assertEquals(before, npc.getHitbox());
    }

    @Test
    void moveContinuesAttack() {
        npc.setAction(EnemyAction.ATTACK);

        npc.move(grid);

        assertEquals(1, npc.attackCalls);
    }

    @Test
    void moveContinuesRangedAttack() {
        npc.setAction(EnemyAction.RANGED_ATTACK);

        npc.move(grid);

        assertEquals(1, npc.rangedAttackCalls);
    }

    @Test
    void deadNpcEventuallyRemovedAndDropsLoot() {
        npc.takeDamage(100);

        for (int i = 0; i < 200; i++) {
            npc.move(grid);
        }

        verify(map).removeEnemy(npc);
        verify(itemFactory).rollDropFromTable(npc.getHitbox());
    }

    @Test
    void canShootPlayerFalseWithoutAmmo() {
        npc.range = 1000;
        npc.hasRangedAmmo = false;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(150, 100, 40, 40));

        assertFalse(npc.canShootPlayer());
    }

    @Test
    void canShootPlayerTrueWithAmmoAndRange() {
        npc.range = 1000;
        npc.hasRangedAmmo = true;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(150, 100, 40, 40));

        assertTrue(npc.canShootPlayer());
    }

    @Test
    void moveWalksAlongPathWhenPathExists() {
        ICell start = mock(ICell.class);
        ICell goal = mock(ICell.class);

        Rectangle2D.Double target = new Rectangle2D.Double(120, 100, 40, 40);
        when(goal.getBounds()).thenReturn(target);

        when(grid.getCellFromPos(any(Rectangle2D.Double.class)))
                .thenReturn(start)
                .thenReturn(goal);

        List<ICell> path = List.of(start, goal);

        when(pathfinder.findPath(eq(npc), eq(start), eq(goal), eq(EnemySize.MEDIUM), any()))
                .thenReturn(path);

        npc.requestPath(grid, pathfinder, target, false);

        double beforeX = npc.getHitbox().x;

        npc.move(grid);

        assertTrue(npc.getHitbox().x > beforeX);
        assertEquals(EnemyAction.WALK, npc.currentAction());
    }

    @Test
    void moveStartsMeleeAttackWhenAggroedAndPlayerIsClose() {
        npc.aggroed = true;
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(130, 100, 40, 40));

        npc.move(grid);

        assertEquals(EnemyAction.ATTACK, npc.currentAction());
        assertEquals(1, npc.attackCalls);
    }

    @Test
    void moveStartsRangedAttackWhenAggroedAndCanShootPlayer() {
        npc.aggroed = true;
        npc.range = 1000;
        npc.hasRangedAmmo = true;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(300, 100, 40, 40));

        npc.move(grid);

        assertEquals(EnemyAction.RANGED_ATTACK, npc.currentAction());
        assertEquals(1, npc.rangedAttackCalls);
    }

    @Test
    void requestPathDoesNotRecalculateSameStartAndGoal() {
        ICell start = mock(ICell.class);
        ICell goal = mock(ICell.class);

        Rectangle2D.Double target = new Rectangle2D.Double(300, 300, 40, 40);
        List<ICell> path = List.of(start, goal);

        when(grid.getCellFromPos(any(Rectangle2D.Double.class)))
                .thenReturn(start)
                .thenReturn(goal)
                .thenReturn(start)
                .thenReturn(goal);

        when(pathfinder.findPath(eq(npc), eq(start), eq(goal), eq(EnemySize.MEDIUM), any()))
                .thenReturn(path);

        npc.requestPath(grid, pathfinder, target, false);
        npc.requestPath(grid, pathfinder, target, false);

        verify(pathfinder, times(1))
                .findPath(eq(npc), eq(start), eq(goal), eq(EnemySize.MEDIUM), any());
    }

    @Test
    void requestPathFromControllerUsesWanderLogicWhenNotAggroed() {
        ICell current = mock(ICell.class);
        ICell wanderGoal = mock(ICell.class);

        Rectangle2D.Double wanderBounds = new Rectangle2D.Double(250, 250, 40, 40);
        when(wanderGoal.getBounds()).thenReturn(wanderBounds);

        when(grid.getCellFromPos(any(Rectangle2D.Double.class))).thenReturn(current, current, wanderGoal);

        when(grid.getNearbyCells(any(Rectangle2D.Double.class), eq(400.0)))
                .thenReturn(List.of(wanderGoal));

        when(pathfinder.canEnter(eq(wanderGoal), eq(EnemySize.MEDIUM))).thenReturn(true);

        List<ICell> path = List.of(current, wanderGoal);

        when(pathfinder.findPath(eq(npc), any(ICell.class), any(ICell.class), eq(EnemySize.MEDIUM), any()))
                .thenReturn(path);

        npc.requestPath(grid, pathfinder, new Rectangle2D.Double(999, 999, 40, 40), true);

        verify(grid).getNearbyCells(any(Rectangle2D.Double.class), eq(400.0));
        verify(pathfinder, atLeastOnce()).findPath(eq(npc), any(ICell.class), any(ICell.class), eq(EnemySize.MEDIUM), any());
    }

    @Test
    void canShootPlayerFalseWhenPlayerIsOutOfRange() {
        npc.range = 50;
        npc.hasRangedAmmo = true;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(1000, 1000, 40, 40));

        assertFalse(npc.canShootPlayer());
    }

    @Test
    void canShootPlayerFalseWhenWallBlocksLineOfSight() {
        npc.range = 1000;
        npc.hasRangedAmmo = true;

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(300, 100, 40, 40));

        IStaticObject wall = mock(IStaticObject.class);
        when(wall.isWall()).thenReturn(true);
        when(wall.getBounds()).thenReturn(new Rectangle2D.Double(180, 90, 40, 80));

        staticObjects.add(wall);

        assertFalse(npc.canShootPlayer());
    }

    @Test
    void getNextInCellIsInitiallyNull() {
        assertNull(npc.getNextInCell());
    }

    @Test
    void getFacingAngleIsInitiallyZero() {
        assertEquals(0.0, npc.getFacingAngle());
    }

    @Test
    void getEnemyTypeReturnsSetType() {
        assertEquals(EnemyType.GHOUL, npc.getEnemyType());
    }

    @Test
    void getAnimationIndexIsInitiallyZero() {
        assertEquals(0, npc.getAnimationIndex());
    }

    @Test
    void incrementAnimationIndexDoesNothingWhenWalkingAndNotMoving() {
        npc.setAction(EnemyAction.WALK);

        npc.incrementAnimationIndex();

        assertEquals(0, npc.getAnimationIndex());
    }

    @Test
    void incrementAnimationIndexDoesNothingWhenNotMoving() {
        npc.setAction(EnemyAction.ATTACK);

        npc.incrementAnimationIndex();

        assertEquals(0, npc.getAnimationIndex());
    }

    @Test
    void setAttackAnimationCountResetsAnimationIndex() {
        npc.animationIndex = 5;

        npc.setAttackAnimationCount(8);

        assertEquals(0, npc.getAnimationIndex());
    }

    @Test
    void incrementAnimationIndexIncreasesWhenMoving() {
        npc.setAction(EnemyAction.ATTACK);

        npc.move(grid); 

        npc.incrementAnimationIndex();

        assertEquals(1, npc.getAnimationIndex());
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
