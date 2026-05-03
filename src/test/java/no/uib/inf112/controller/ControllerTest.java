package no.uib.inf112.controller;

import no.uib.inf112.enums.*;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.GameDrawer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ControllerTest {

    private Controller controller;
    private IModel map;
    private IControllablePlayer player;
    private IGrid grid;
    private GameDrawer view;
    private Camera camera;
    private SoundHandler soundHandler;
    private Factory factory;
    private ItemFactory itemFactory;
    private Component source;

    @BeforeEach
    void setUp() {
        map = mock(IModel.class);
        player = mock(IControllablePlayer.class);
        grid = mock(IGrid.class);
        view = mock(GameDrawer.class);
        camera = mock(Camera.class);
        soundHandler = mock(SoundHandler.class);
        factory = mock(Factory.class);
        itemFactory = mock(ItemFactory.class);
        source = new Canvas();

        when(map.getPlayer()).thenReturn(player);
        when(map.getGrid()).thenReturn(grid);
        when(map.getCamera()).thenReturn(camera);
        when(map.getSoundHandler()).thenReturn(soundHandler);
        when(map.getFactory()).thenReturn(factory);
        when(map.getItemFactory()).thenReturn(itemFactory);

        when(map.getGameState()).thenReturn(GameState.MAIN_MENU);

        when(map.getEnemies()).thenReturn(new ArrayList<>());
        when(map.getAOEPuddles()).thenReturn(new ArrayList<>());
        when(map.getProjectiles()).thenReturn(new ArrayList<>());
        when(map.gunShots()).thenReturn(new ArrayList<>());
        when(map.getVehicles()).thenReturn(new ArrayList<>());

        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(100, 100, 40, 40));
        when(player.isAlive()).thenReturn(true);

        when(map.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 1000, 1000));
        when(camera.screenToWorld(anyDouble(), anyDouble())).thenReturn(new Point2D.Double(200, 300));

        controller = new Controller(map, view);
    }

    private KeyEvent keyPressed(int keyCode) {
        return new KeyEvent(
                source,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED
        );
    }

    private KeyEvent keyReleased(int keyCode) {
        return new KeyEvent(
                source,
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED
        );
    }

    private MouseEvent mouseMoved(int x, int y) {
        return new MouseEvent(
                source,
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                x,
                y,
                1,
                false
        );
    }

    @Test
    void constructorAddsListenersToView() {
        verify(view).addKeyListener(controller);
        verify(view).addMouseMotionListener(controller);
        verify(view).addMouseListener(controller);
        verify(view).setFocusable(true);
    }

    @Test
    void pressingEnterInMainMenuStartsGame() {
        when(map.getGameState()).thenReturn(GameState.MAIN_MENU);

        controller.keyPressed(keyPressed(KeyEvent.VK_ENTER));

        verify(map).setGameState(GameState.ACTIVE_GAME);
    }

    @Test
    void activeGameWasdKeysPressMove() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_W));
        verify(player).pressMove(Direction.NORTH);

        controller.keyPressed(keyPressed(KeyEvent.VK_S));
        verify(player).pressMove(Direction.SOUTH);

        controller.keyPressed(keyPressed(KeyEvent.VK_A));
        verify(player).pressMove(Direction.WEST);

        controller.keyPressed(keyPressed(KeyEvent.VK_D));
        verify(player).pressMove(Direction.EAST);
    }

    @Test
    void activeGameWasdKeysReleaseMove() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyReleased(keyReleased(KeyEvent.VK_W));
        verify(player).releaseMove(Direction.NORTH);

        controller.keyReleased(keyReleased(KeyEvent.VK_S));
        verify(player).releaseMove(Direction.SOUTH);

        controller.keyReleased(keyReleased(KeyEvent.VK_A));
        verify(player).releaseMove(Direction.WEST);

        controller.keyReleased(keyReleased(KeyEvent.VK_D));
        verify(player).releaseMove(Direction.EAST);
    }

    @Test
    void arrowKeysAlsoPressMove() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_UP));
        verify(player).pressMove(Direction.NORTH);

        controller.keyPressed(keyPressed(KeyEvent.VK_DOWN));
        verify(player).pressMove(Direction.SOUTH);

        controller.keyPressed(keyPressed(KeyEvent.VK_LEFT));
        verify(player).pressMove(Direction.WEST);

        controller.keyPressed(keyPressed(KeyEvent.VK_RIGHT));
        verify(player).pressMove(Direction.EAST);
    }

    @Test
    void arrowKeysAlsoReleaseMove() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyReleased(keyReleased(KeyEvent.VK_UP));
        verify(player).releaseMove(Direction.NORTH);

        controller.keyReleased(keyReleased(KeyEvent.VK_DOWN));
        verify(player).releaseMove(Direction.SOUTH);

        controller.keyReleased(keyReleased(KeyEvent.VK_LEFT));
        verify(player).releaseMove(Direction.WEST);

        controller.keyReleased(keyReleased(KeyEvent.VK_RIGHT));
        verify(player).releaseMove(Direction.EAST);
    }

    @Test
    void activeGameNumberKeysChangeWeapons() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_1));
        verify(player).setGunType(GunType.DEAGLE);

        controller.keyPressed(keyPressed(KeyEvent.VK_2));
        verify(player).setGunType(GunType.MP5);

        controller.keyPressed(keyPressed(KeyEvent.VK_3));
        verify(player).setGunType(GunType.SHOTGUN);
    }

    @Test
    void debugDamageOnlyHappensWhenDebugModeIsOn() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);
        when(map.debugMode()).thenReturn(true);

        controller.keyPressed(keyPressed(KeyEvent.VK_I));

        verify(player).takeDamage(10);
    }

    @Test
    void debugDamageDoesNotHappenWhenDebugModeIsOff() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);
        when(map.debugMode()).thenReturn(false);

        controller.keyPressed(keyPressed(KeyEvent.VK_I));

        verify(player, never()).takeDamage(10);
    }

    @Test
    void pressingNTalliesKillCountFiftyTimes() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_N));

        verify(player, times(50)).increaseKillCount();
    }

    @Test
    void pressingPTurnsDebugOnWhenDebugIsOff() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);
        when(map.debugMode()).thenReturn(false);

        controller.keyPressed(keyPressed(KeyEvent.VK_P));

        verify(map).debugOn();
    }

    @Test
    void pressingPTurnsDebugOffWhenDebugIsOn() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);
        when(map.debugMode()).thenReturn(true);

        controller.keyPressed(keyPressed(KeyEvent.VK_P));

        verify(map).debugOff();
    }

    @Test
    void pressingBOpensInventory() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_B));

        verify(player).openCloseInventory();
    }

    @Test
    void pressingOOpensObjectives() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_O));

        verify(player).openCloseObjectives();
    }

    @Test
    void pressingLInActiveGameSetsVictory() {
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyPressed(KeyEvent.VK_L));

        verify(map).setGameState(GameState.VICTORY);
    }

    @Test
    void pressingRInGameOverResetsMapAndView() {
        when(map.getGameState()).thenReturn(GameState.GAME_OVER);

        controller.keyPressed(keyPressed(KeyEvent.VK_R));

        verify(map).setLevel(1);
        verify(map).resetMap();
        verify(view).setScreens();
        verify(view, atLeastOnce()).repaint();
    }

    @Test
    void pressingRInVictoryResetsMapAndView() {
        when(map.getGameState()).thenReturn(GameState.VICTORY);

        controller.keyPressed(keyPressed(KeyEvent.VK_R));

        verify(map).setLevel(1);
        verify(map).resetMap();
        verify(view).setScreens();
        verify(view, atLeastOnce()).repaint();
    }

    @Test
    void mouseMovedUpdatesPlayerAimWhenPlayerIsAlive() {
        when(player.isAlive()).thenReturn(true);

        controller.mouseMoved(mouseMoved(50, 60));

        verify(camera).update(
                any(Rectangle2D.Double.class),
                anyDouble(),
                anyDouble(),
                any(Rectangle2D.Double.class)
        );

        verify(player).aimAtWorldPosition(200.0, 300.0);
    }

    @Test
    void mouseMovedDoesNotUpdateAimWhenPlayerIsDead() {
        when(player.isAlive()).thenReturn(false);

        controller.mouseMoved(mouseMoved(50, 60));

        verify(player, never()).aimAtWorldPosition(anyDouble(), anyDouble());
    }

    @Test
    void mouseDraggedUsesMouseMovedLogic() {
        when(player.isAlive()).thenReturn(true);

        controller.mouseDragged(mouseMoved(50, 60));

        verify(player).aimAtWorldPosition(200.0, 300.0);
    }

    @Test
    void unimplementedMouseAndKeyMethodsDoNotThrow() {
        assertDoesNotThrow(() -> controller.keyTyped(keyPressed(KeyEvent.VK_A)));
        assertDoesNotThrow(() -> controller.mouseClicked(mouseMoved(1, 1)));
        assertDoesNotThrow(() -> controller.mouseEntered(mouseMoved(1, 1)));
        assertDoesNotThrow(() -> controller.mouseExited(mouseMoved(1, 1)));
    }
}