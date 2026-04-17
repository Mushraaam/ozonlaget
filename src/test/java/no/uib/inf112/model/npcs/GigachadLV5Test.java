package no.uib.inf112.model.npcs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;
import no.uib.inf112.model.npcs.projectiles.ExplosionProjectile;
import no.uib.inf112.model.npcs.projectiles.MinionProjectile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GigachadLV5Test {

    private GigachadLV5 boss;
    private IModel mockMap;
    private IPlayer mockPlayer;
    private IGrid mockGrid;
    private ItemFactory mockItemFactory;

    private ArrayList<IEnemy> fakeEnemyList;
    private ArrayList<IVehicle> fakeVehicleList;

    @BeforeEach
    public void setUp() {
        mockMap = Mockito.mock(IModel.class);
        mockPlayer = Mockito.mock(IPlayer.class);
        mockGrid = Mockito.mock(IGrid.class);
        mockItemFactory = Mockito.mock(ItemFactory.class);

        Pathfinder mockPathfinder = Mockito.mock(Pathfinder.class);

        fakeEnemyList = new ArrayList<>();
        fakeVehicleList = new ArrayList<>();

        Mockito.when(mockMap.getPlayer()).thenReturn(mockPlayer);
        Mockito.when(mockMap.getGrid()).thenReturn(mockGrid);
        Mockito.when(mockMap.getPathfinder()).thenReturn(mockPathfinder);
        Mockito.when(mockMap.getItemFactory()).thenReturn(mockItemFactory);
        Mockito.when(mockMap.getEnemies()).thenReturn(fakeEnemyList);
        Mockito.when(mockMap.getVehicles()).thenReturn(fakeVehicleList);


        Mockito.when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Double(0, 0, 10, 10));

        Mockito.when(mockPathfinder.canEnter(Mockito.any(), Mockito.any())).thenReturn(true);


        boss = new GigachadLV5(new Rectangle2D.Double(1000, 1000, 50, 50), mockMap);

    }


    @Test
    public void testIsLegalReturnsFalseWhenHittingPlayer() {
        Rectangle2D.Double bossMove = new Rectangle2D.Double(1010, 1000, 50, 50);

        Mockito.when(mockPlayer.getHitbox()).thenReturn(new Rectangle2D.Double(1015, 1005, 10, 10));

        boolean canMove = boss.isLegal(bossMove);

        assertFalse(canMove, "isLegal should return false when the boss hits the player.");
    }

    @Test
    public void testIsLegalSquashesSmallEnemies() {
        IEnemy fakeGhoul = Mockito.mock(Ghoul.class);
        fakeEnemyList.add(fakeGhoul);

        ICell fakeCell = Mockito.mock(ICell.class);
        Mockito.when(mockGrid.getCellFromPos(Mockito.any())).thenReturn(fakeCell);

        Mockito.when(fakeGhoul.isAlive()).thenReturn(true);
        Mockito.when(fakeGhoul.size()).thenReturn(EnemySize.SMALL);

        Rectangle2D.Double bossMove = new Rectangle2D.Double(1010, 1000, 50, 50);
        Mockito.when(fakeGhoul.getHitbox()).thenReturn(new Rectangle2D.Double(1015, 1005, 10, 10));


        boolean canMove = boss.isLegal(bossMove);

        Mockito.verify(fakeGhoul).takeDamage(9999);
        assertTrue(canMove, "Boss shouldnt be blocked by smaller enemies");
    }

    @Test
    public void testIsLegalBlockedByLargeEnemies() {
        IEnemy fakeBigGhoul = Mockito.mock(Ghoul.class);
        fakeEnemyList.add(fakeBigGhoul);

        ICell fakeCell = Mockito.mock(ICell.class);
        Mockito.when(mockGrid.getCellFromPos(Mockito.any())).thenReturn(fakeCell);

        Mockito.when(fakeBigGhoul.isAlive()).thenReturn(true);
        Mockito.when(fakeBigGhoul.size()).thenReturn(EnemySize.LARGE);

        Rectangle2D.Double bossMove = new Rectangle2D.Double(1010, 1000, 50, 50);
        Mockito.when(fakeBigGhoul.getHitbox()).thenReturn(new Rectangle2D.Double(1015, 1005, 10, 10));


        boolean canMove = boss.isLegal(bossMove);

        assertFalse(canMove, "Large enemies should collide");
    }

    @Test
    public void testIsLegalIgnoresBugs() {
        IEnemy fakeBug = Mockito.mock(KamikazeBug.class);
        fakeEnemyList.add(fakeBug);

        ICell fakeCell = Mockito.mock(ICell.class);
        Mockito.when(mockGrid.getCellFromPos(Mockito.any())).thenReturn(fakeCell);
        Mockito.when(fakeBug.getEnemyType()).thenReturn(EnemyType.BUG);

        Mockito.when(fakeBug.isAlive()).thenReturn(true);
        Mockito.when(fakeBug.size()).thenReturn(EnemySize.SMALL);

        Rectangle2D.Double bossMove = new Rectangle2D.Double(1010, 1000, 50, 50);
        Mockito.when(fakeBug.getHitbox()).thenReturn(new Rectangle2D.Double(1015, 1005, 10, 10));


        boolean canMove = boss.isLegal(bossMove);

        Mockito.verify(fakeBug, Mockito.never()).takeDamage(Mockito.anyInt());
        assertTrue(canMove, "Boss steps over bugs");
    }


    @Test
    public void testRangedAttackFiresMinionAtLongRange() {
        Rectangle2D.Double playerHitbox = new Rectangle2D.Double(1600, 1000, 50, 50);
        Mockito.when(mockPlayer.getHitbox()).thenReturn(playerHitbox);

        for (int i = 0; i <= 25; i++) {
            boss.rangedAttack(playerHitbox);
        }

        Mockito.verify(mockMap).addProjectile(Mockito.any(MinionProjectile.class));
    }

    @Test
    public void testRangedAttackFiresExplosionAtMediumRange() {
        Rectangle2D.Double playerHitbox = new Rectangle2D.Double(1400, 1000, 50, 50);
        Mockito.when(mockPlayer.getHitbox()).thenReturn(playerHitbox);

        for (int i = 0; i <= 25; i++) {
            boss.rangedAttack(playerHitbox);
        }

        Mockito.verify(mockMap).addProjectile(Mockito.any(ExplosionProjectile.class));
    }

    @Test
    public void testMoveReducesCooldowns() {
        Rectangle2D.Double playerHitbox = new Rectangle2D.Double(1400, 1000, 50, 50);
        Mockito.when(mockPlayer.getHitbox()).thenReturn(playerHitbox);

        for (int i = 0; i <= 50; i++) {
            boss.rangedAttack(playerHitbox);
        }
        boss.move(mockGrid);
        assertEquals(50, boss.getRange(), "Boss range should shrink to 50 when both ranged attacks are on cooldown");
    }

    @Test
    public void testBossDropsChopperKeycardOnDeath() {
        boss.dropLoot();
        Mockito.verify(mockItemFactory).dropSpecificItem(
                Mockito.eq(CollectableType.CHOPPER_KEYCARD),
                Mockito.any(Rectangle2D.Double.class)
        );
    }
}