package no.uib.inf112.view.DrawStates;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
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
    }

    /* Sentrerer kamera på player */
    private void centerCamera(Graphics2D graphic) {

        Rectangle2D.Double bounds = this.player.getBounds();
        int screenX = graphic.getClipBounds().width / 2;
        int screenY = graphic.getClipBounds().height / 2;
        graphic.translate(
                screenX - (bounds.getX() + bounds.getWidth() / 2),
                screenY - (bounds.getY() + bounds.getHeight() / 2));
    }

    private void drawBackground(Graphics2D graphic) {
        Rectangle2D.Double bounds = this.map.getBounds();
        drawImage(graphic, this.tempBackground, bounds);
    }

    private void drawPlayer(Graphics2D graphic) {
        Rectangle2D.Double bounds = this.player.getBounds();
        drawImage(graphic, this.tempDuck, bounds);
    }

}
