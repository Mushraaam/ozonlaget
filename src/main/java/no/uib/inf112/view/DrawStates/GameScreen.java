package no.uib.inf112.view.DrawStates;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.utility.ImageReader;

public class GameScreen implements IDrawer {

    private IMap map;
    private IPlayer player;

    private BufferedImage tempDuck;
    private BufferedImage tempBackground;

    public GameScreen(IMap map) {
        this.map = map;
        this.player = map.getPlayer();

        // Bør skaleres kun en gang, dette flyttes senere til ny klasse
        this.tempDuck = ImageReader.fetcImage("src\\main\\java\\no\\resources\\tempduck.png");
        this.tempBackground = ImageReader.fetcImage("src\\main\\java\\no\\resources\\parkbackground.png");
    }

    @Override
    public void draw(Graphics2D graphic) {

        /* Order matters(tror jeg) */
        centerCamera(graphic);
        drawBackground(graphic);
        drawPlayer(graphic);
        drawEnemies(graphic);
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
        Rectangle2D.Double bounds = this.map.getBounds();
        drawImage(graphic, this.tempBackground, bounds);
    }

    private void drawPlayer(Graphics2D graphic) {
        Rectangle2D.Double hitbox = this.player.getHitbox();
        drawImage(graphic, this.tempDuck, hitbox);
    }

    private void drawEnemies(Graphics2D graphic){
        ArrayList<IEnemy> enemies = this.map.getEnemies();
        for(IEnemy enemy : enemies){
            Rectangle2D.Double bounds = enemy.getBounds();
            drawImage(graphic, enemy.getImg(), bounds );
        }
    }

}
