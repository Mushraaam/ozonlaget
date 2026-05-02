package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.view.drawstates.MainMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class ControllerTest {

    private Controller controller;
    private IModel map;
    private GameDrawer view;
    private IControllablePlayer player;
    private Camera camera;

    @BeforeEach
    void setUp() {
        map = mock(IModel.class);
        view = mock(GameDrawer.class);
        player = mock(IControllablePlayer.class);
        camera = mock(Camera.class);

        when(map.getPlayer()).thenReturn(player);
        when(map.getCamera()).thenReturn(camera);
        when(map.getSoundHandler()).thenReturn(mock(SoundHandler.class));
        when(map.getFactory()).thenReturn(mock(Factory.class));
        when(map.getItemFactory()).thenReturn(mock(ItemFactory.class));
        when(map.getGrid()).thenReturn(mock(IGrid.class));
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        when(map.getEnemies()).thenReturn(new ArrayList<>());
        when(map.gunShots()).thenReturn(new ArrayList<>());
        when(map.getAOEPuddles()).thenReturn(new ArrayList<>());
        when(map.getProjectiles()).thenReturn(new ArrayList<>());

        controller = new Controller(map, view);
    }

    @Test
    void wKeyPressedTest() {
        KeyEvent keyEventMock = mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_W);
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyPressed(keyEventMock);

        verify(player).pressMove(Direction.NORTH);
    }

    @Test
    void wKeyReleasedTest() {
        KeyEvent keyEventMock = mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_W);
        when(map.getGameState()).thenReturn(GameState.ACTIVE_GAME);

        controller.keyReleased(keyEventMock);

        verify(player).releaseMove(Direction.NORTH);
    }

    @Test
    void mousePressedTest() {
        when(map.getGameState()).thenReturn(GameState.MAIN_MENU);
        
        MainMenu mainMenuMock = mock(MainMenu.class);
        when(view.getMainMenu()).thenReturn(mainMenuMock);

        when(mainMenuMock.getStartButton()).thenReturn(new Rectangle2D.Double(0, 0, 100, 100));
        
        MouseEvent mouseEventMock = mock(MouseEvent.class);
        when(mouseEventMock.getPoint()).thenReturn(new Point(50, 50));
        when(mouseEventMock.getModifiersEx()).thenReturn(InputEvent.BUTTON1_DOWN_MASK); // Marks it as left click for SwingUtilities

        controller.mousePressed(mouseEventMock);

        verify(map).setGameState(GameState.ACTIVE_GAME);
    }

    @Test
    void mouseClickedTest() {
        MouseEvent mouseEventMock = mock(MouseEvent.class);
        assertDoesNotThrow(() -> controller.mouseClicked(mouseEventMock));
    }

    @Test
    void mouseReleasedTest() {
        MouseEvent mouseEventMock = mock(MouseEvent.class);
        assertDoesNotThrow(() -> controller.mouseReleased(mouseEventMock));
    }

    @Test
    void mouseMovedTest() {
        MouseEvent mouseEventMock = mock(MouseEvent.class);
        when(mouseEventMock.getX()).thenReturn(400);
        when(mouseEventMock.getY()).thenReturn(300);

        when(player.isAlive()).thenReturn(true);
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(0, 0, 20, 20));
        when(view.getWidth()).thenReturn(800);
        when(view.getHeight()).thenReturn(600);
        when(map.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 1000, 1000));

        Point2D.Double expectedWorldPoint = new Point2D.Double(150, 150);
        when(camera.screenToWorld(400, 300)).thenReturn(expectedWorldPoint);

        controller.mouseMoved(mouseEventMock);

        verify(camera).update(any(), eq(800.0), eq(600.0), any());
        verify(player).aimAtWorldPosition(150, 150);
    }

    @Test
    void mouseDraggedTest() {
        MouseEvent mouseEventMock = mock(MouseEvent.class);
        when(mouseEventMock.getX()).thenReturn(200);
        when(mouseEventMock.getY()).thenReturn(100);

        when(player.isAlive()).thenReturn(true);
        when(player.getHitbox()).thenReturn(new Rectangle2D.Double(0, 0, 20, 20));
        
        Point2D.Double expectedWorldPoint = new Point2D.Double(50, 25);
        when(camera.screenToWorld(200, 100)).thenReturn(expectedWorldPoint);

        controller.mouseDragged(mouseEventMock);

        verify(player).aimAtWorldPosition(50, 25);
    }
}