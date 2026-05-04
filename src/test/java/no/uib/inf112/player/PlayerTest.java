package no.uib.inf112.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.model.Model;
import no.uib.inf112.model.items.buffs.HealthBox;
import no.uib.inf112.model.items.buffs.RainbowBuff;
import no.uib.inf112.model.levels.Level1;
import no.uib.inf112.model.npcs.Ghoul;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

class PlayerTest {
    private MockedConstruction<SoundHandler> mockedSoundHandler;
    private Model model;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model(mock(LoadStatus.class));
    }

    @AfterEach
    void tearDown() {
        if (mockedSoundHandler != null) {
            mockedSoundHandler.close();
        }
    }

    @Test
    void testPlayerInitialPos() {

        assertTrue(model.getPlayer() instanceof Player);

        Player player = (Player) model.getPlayer();
        Level1 level = new Level1(new Model(mock(LoadStatus.class)));

        assertEquals(level.getPlayer().getHitbox(), player.getHitbox());

        assertEquals(0, player.getKillCount());
        player.increaseKillCount();
        assertEquals(1, player.getKillCount());
    }

    MouseEvent generateMouseClick(int x, int y) {
        return new MouseEvent(
                mock(java.awt.Component.class),
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                x, y, x, y,
                1,
                false,
                MouseEvent.BUTTON1);
    }

    int countGunShots(Iterable<IGunShot> shot) {
        int i = 0;
        for (IGunShot g : shot) {
            i++;
        }
        return i;
    }

    @Test
    void gunTest() {

        MouseEvent event = generateMouseClick(0, 0);
        Player player = (Player) model.getPlayer();
        assertEquals(GunType.DEAGLE, player.gunType());
        assertEquals(100, player.currentAmmunition());
        assertEquals(100, player.maxAmmunition());
        player.shoot(event);
        assertEquals(1, countGunShots(model.gunShots()));
        assertEquals(99, player.currentAmmunition());
        assertEquals(100, player.maxAmmunition());
        player.setGunType(GunType.MP5);
        assertEquals(GunType.MP5, player.gunType());
        assertEquals(300, player.currentAmmunition());
        assertEquals(300, player.maxAmmunition());
        player.shoot(event);
        assertEquals(299, player.currentAmmunition());
        assertEquals(300, player.maxAmmunition());
        assertEquals(2, countGunShots(model.gunShots()));
        player.setGunType(GunType.SHOTGUN);
        assertEquals(GunType.SHOTGUN, player.gunType());
        assertEquals(20, player.currentAmmunition());
        assertEquals(20, player.maxAmmunition());
        player.shoot(event);
        assertEquals(19, player.currentAmmunition());
        assertEquals(20, player.maxAmmunition());
        assertEquals(12, countGunShots(model.gunShots()));
    }

    @Test
    void playerShootDamageEnemy() {
        Player player = (Player) model.getPlayer();
        Rectangle2D.Double PHB = player.getHitbox();

        Rectangle2D.Double EHB = new Rectangle2D.Double(PHB.getX(), PHB.getY(), PHB.getWidth() + 50, PHB.getHeight());

        Ghoul ghoul = new Ghoul(EHB, model);
        model.addEnemy(ghoul);
        assertEquals(100, ghoul.getHealth());
        player.shoot(generateMouseClick((int) EHB.getCenterX(), (int) EHB.getCenterY()));
        assertEquals(50, ghoul.getHealth());

        for (int i = 0; i<100; i++){
            player.reload();
        }

        player.shoot(generateMouseClick((int) EHB.getCenterX(), (int) EHB.getCenterY()));
        assertEquals(0, ghoul.getHealth());
        assertFalse(ghoul.isAlive());

        assertEquals(1, player.getKillCount());
    }

    @Test
    void takeDamageTest(){
        Player player = (Player) model.getPlayer();
        model.setGameState(GameState.ACTIVE_GAME);
        assertEquals(player.getMaxHP(), player.getCurrentHP());

        player.takeDamage(5);

        assertEquals(player.getMaxHP()-5, player.getCurrentHP());

        assertThrows(IllegalArgumentException.class, () -> player.takeDamage(-1));

        player.takeDamage(1000);

        assertEquals(0, player.getCurrentHP());
        assertFalse(player.isAlive());
        assertEquals(GameState.GAME_OVER, model.getGameState());
    }

    @Test
    void playerHealTest(){

        Player player = (Player) model.getPlayer();
        model.setGameState(GameState.ACTIVE_GAME);
        HealthBox hpbox = new HealthBox(player.getHitbox(), CollectableType.HEALTH, model);
        
        player.takeDamage(90);
        assertEquals(10, player.getCurrentHP());

        player.healHP(10);
        assertEquals(20, player.getCurrentHP());

        hpbox.affectPlayer();
        assertEquals(55, player.getCurrentHP());

        assertThrows(IllegalArgumentException.class, () -> player.healHP(-10));
    }

    @Test
    void playerBuffTest(){
        Player player = (Player) model.getPlayer();
        model.setGameState(GameState.ACTIVE_GAME);

        RainbowBuff buff = new RainbowBuff(player.getHitbox(), CollectableType.POWERUP_RAINBOW, model);
        buff.affectPlayer();

        int timeRemaining = player.buffCountDown();

        player.decrementBuff(mock(SoundHandler.class));

        assertEquals(timeRemaining - 1, player.buffCountDown());
        
    }

    @Test
    void openCloseInventoryTest(){
        Player player = (Player) model.getPlayer();
        model.setGameState(GameState.ACTIVE_GAME);

        assertFalse(player.objectivesVisible());
        player.openCloseObjectives();
        assertTrue(player.objectivesVisible());
        player.openCloseObjectives();
        assertFalse(player.objectivesVisible());

        assertFalse(player.getInventory().isVisible());
        player.openCloseInventory();
        assertTrue(player.getInventory().isVisible());
        player.openCloseInventory();
        assertFalse(player.getInventory().isVisible());
    }
}
