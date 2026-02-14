package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IStaticDrawableObject;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IViewablePlayer;
import no.uib.inf112.interfaces.IWall;
import no.uib.inf112.utility.ImageHandler;

public class GameScreen implements IDrawer {

    private IMap map;
    private IGrid grid;
    private IViewablePlayer player;
    private ImageHandler handler;

    private BufferedImage playerSprite;

    public GameScreen(IMap map, ImageHandler handler) {
        this.map = map;
        this.player = (IViewablePlayer) map.getPlayer();
        this.handler = handler;
        this.grid = map.getGrid();

        // Bør skaleres kun en gang, dette flyttes senere til ny klasse
        this.playerSprite = this.handler.getPlayerSprite(this.player.getDirection(), this.player.getAnimationIndex());
    }

    @Override
    public void draw(Graphics2D graphic) {

        /* Order matters(tror jeg) */
        centerCamera(graphic);
        drawBackground(graphic);
        drawStaticObjects(graphic);
        drawPlayer(graphic);
        drawEnemies(graphic);
    }

    private void drawStaticObjects(Graphics2D graphic) {
        for (IStaticObject o : map.getStaticObjects()) {

            // Optimize later
            if (!(o instanceof IStaticDrawableObject)) {
                throw new IllegalArgumentException("Object should be instance of IStaticDrawableObject");
            }

            IStaticDrawableObject obj = (IStaticDrawableObject) o;

            if (obj instanceof IWall) {
                IWall wall = (IWall) obj;
                BufferedImage image = handler.getWallImage(wall.wallType(), wall.getWallDirection());
                drawImage(graphic, image, wall.getBounds());
            }
        }
    }

    /* Sentrerer kamera på player, holder seg innenfor bounds */
    private void centerCamera(Graphics2D graphic) {

        Rectangle2D.Double playerHitbox = this.player.getHitbox();
        Rectangle2D.Double mapBounds = this.map.getBounds();

        double screenWidth = graphic.getClipBounds().getWidth();
        double screenHeight = graphic.getClipBounds().getHeight();
        double playerCenterX = playerHitbox.getCenterX();
        double playerCenterY = playerHitbox.getCenterY();

        double translatedX = screenWidth / 2 - playerCenterX;
        double translatedY = screenHeight / 2 - playerCenterY;

        double minTranslateX = screenWidth - mapBounds.getWidth();
        double minTranslateY = screenHeight - mapBounds.getHeight();

        translatedX = Math.min(0, Math.max(translatedX, minTranslateX));
        translatedY = Math.min(0, Math.max(translatedY, minTranslateY));

        graphic.translate(translatedX, translatedY);
    }

    private void drawBackground(Graphics2D graphic) {
        drawCellsInView(graphic, this.grid, this.handler);
    }

    private void drawPlayer(Graphics2D graphic) {
        this.playerSprite = this.handler.getPlayerSprite(this.player.getDirection(), this.player.getAnimationIndex());
        Rectangle2D.Double hitbox = this.player.getHitbox();
        drawImage(graphic, this.playerSprite, hitbox);
    }

    private void drawEnemies(Graphics2D graphic) {
        ArrayList<IEnemy> enemies = this.map.getEnemies();
        for (IEnemy enemy : enemies) {
            if (isVisible(graphic, enemy.getHitbox())) {
                drawImage(graphic, handler.getEnemyImage(
                        enemy.getEnemyType(),
                        enemy.getAnimationIndex()),
                        enemy.getHitbox());
            }
        }
    }

}
