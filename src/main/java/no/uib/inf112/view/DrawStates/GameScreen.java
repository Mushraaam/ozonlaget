package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.config.Config;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IStaticDrawableObject;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IViewablePlayer;
import no.uib.inf112.interfaces.IWall;
import no.uib.inf112.utility.ImageHandler;
import no.uib.inf112.utility.Camera;

public class GameScreen implements IDrawer {

    private IMap map;
    private IGrid tiles;
    private IViewablePlayer player;
    private ImageHandler handler;
    private Camera camera;
    private static final Rectangle2D.Double MAPDIMENSION = new Rectangle2D.Double(0, 0, Config.getInt("mapHeight"),Config.getInt("mapWidth"));
    private GameUI ui;

    // private BufferedImage playerSprite;

    public GameScreen(IMap map, ImageHandler handler, Camera camera) {


        this.map = map;
        this.ui = new GameUI(this.map, handler);
        this.player = (IViewablePlayer) map.getPlayer();
        this.handler = handler;
        this.tiles = map.getTiles();
        this.camera = camera;
    }

    @Override
    public void draw(Graphics2D graphic) {

        /* Order matters(tror jeg) */
        centerCamera(graphic);
        drawBackground(graphic);
        drawStaticObjects(graphic);
        drawPlayer(graphic);
        drawEnemies(graphic);
        drawGunShots(graphic);
        
        this.ui.draw(graphic);
    }

    private void drawGunShots(Graphics2D graphic) {
        graphic.setColor(Color.YELLOW);
        graphic.setStroke(new BasicStroke(2));
        for (IGunShot shot : this.map.gunShots()){
            // graphic.fill(shot.bounds());
            graphic.draw(shot.bounds());
        }
    }

    private void drawStaticObjects(Graphics2D graphic) {
        for (IStaticObject o : map.getStaticObjects()) {

            // Optimize later
            if (!(o instanceof IStaticDrawableObject)) {
                throw new IllegalArgumentException("Object should be instance of IStaticDrawableObject");
            }

            IStaticDrawableObject obj = (IStaticDrawableObject) o;

            if (obj.isWall()) {
                IWall wall = (IWall) obj;
                BufferedImage image = handler.getWallImage(wall.getType(), wall.getWallDirection());
                drawImage(graphic, image, wall.getBounds());
            }
            else {
                BufferedImage image = handler.getStaticObjectImage(obj.getType());
                drawImage(graphic, image, obj.getBounds());
            }
        }
    }

    /* Sentrerer kamera på player, holder seg innenfor bounds */
    private void centerCamera(Graphics2D graphic) {

        Rectangle2D.Double playerHitbox = player.getHitbox();
        Rectangle2D.Double mapBounds = map.getBounds();

        double screenWidth = graphic.getClipBounds().getWidth();
        double screenHeight = graphic.getClipBounds().getHeight();

        camera.update(playerHitbox, screenWidth, screenHeight, mapBounds);
        camera.apply(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        drawImage(graphic, this.handler.getBackground(this.map.level()), MAPDIMENSION);
        drawCellsInView(graphic, this.tiles, this.handler);
    }

    private void drawPlayer(Graphics2D graphic) {
        drawRotated(
                graphic,
                handler.getPlayerSprite(player.getDirection(), player.getAnimationIndex()), player.getHitbox(), player.getFacingAngle()
        );
    }

    private void drawEnemies(Graphics2D graphic) {
        for (IEnemy e : map.getEnemies()) {
            if(isVisible(graphic, e.getHitbox())){
            drawRotated(
                    graphic,
                    handler.getEnemySprites(e.getEnemyType(), e.getAnimationIndex()),
                    e.getHitbox(),
                    e.getFacingAngle()
            );}
        }

    }

    private void drawRotated(Graphics2D g2, BufferedImage img, Rectangle2D.Double hb, double angle) {
        var old = g2.getTransform();
        g2.translate(hb.getCenterX(), hb.getCenterY());
        g2.rotate(angle);
        g2.drawImage(img, (int)-hb.width/2, (int)-hb.height/2, (int)hb.width, (int)hb.height, null);

        g2.setTransform(old);
    }


}
