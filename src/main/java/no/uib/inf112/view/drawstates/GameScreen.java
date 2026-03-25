package no.uib.inf112.view.drawstates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.config.Config;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.utility.ImageHandler;
import no.uib.inf112.utility.Camera;

public class GameScreen implements IDrawer {

    private IMap map;
    private IGrid tiles;
    private ImageHandler handler;
    private Camera camera;
    private static final Rectangle2D.Double MAPDIMENSION = new Rectangle2D.Double(0, 0, Config.getInt("mapHeight"),
            Config.getInt("mapWidth"));
    private GameUI ui;
    private DarknessOverlay darkness;

    public GameScreen(IMap map, ImageHandler handler, Camera camera) {

        this.map = map;
        this.ui = new GameUI(this.map, handler);
        this.darkness = new DarknessOverlay(this.map);
        this.handler = handler;
        this.tiles = map.getTiles();
        this.camera = camera;
    }

    @Override
    public void draw(Graphics2D graphic) {

        /* Order matters(tror jeg) */
        centerCamera(graphic);
        drawBackground(graphic);
        drawPuddles(graphic);
        drawProjectiles(graphic);
        drawStaticObjects(graphic);
        drawWalls(graphic);
        drawEnemies(graphic);
        drawGunShots(graphic);
        drawActiveItems(graphic);
        drawPlayer(graphic);
        drawDarkness(graphic);

        this.ui.draw(graphic);
    }

    private void drawDarkness(Graphics2D graphic) {
        this.darkness.draw(graphic);
    }

    private void drawProjectiles(Graphics2D graphic) {
        for (IProjectile projectile : this.map.getProjectiles()) {
            drawRotated(graphic, this.handler.getProjectile(projectile.getType()), projectile.getBounds(),
                    projectile.angle());
        }
    }

    private void drawPuddles(Graphics2D graphic) {
        for (IPuddle puddle : this.map.getAOEPuddles()) {
            Rectangle2D.Double bounds = puddle.getBounds();
            drawImage(graphic,
                    this.handler.getPuddleImage(puddle.getType(), puddle.getAnimationIndex(), puddle.lifeTime()),
                    bounds);

        }
    }

    private void drawGunShots(Graphics2D graphic) {
        graphic.setColor(Color.YELLOW);
        graphic.setStroke(new BasicStroke(1));
        for (IGunShot shot : this.map.gunShots()) {
            graphic.draw(shot.bounds());
        }
    }

    private void drawWalls(Graphics2D graphic) {
        for (IStaticObject o : map.getStaticObjects()) {

            // Optimize later
            if (!(o instanceof IStaticDrawableObject)) {
                throw new IllegalArgumentException("Object should be instance of IStaticDrawableObject");
            }

            if (isVisible(graphic, o.getBounds())) {

                IStaticDrawableObject obj = (IStaticDrawableObject) o;

                if (obj.isWall()) {
                    IWall wall = (IWall) obj;
                    BufferedImage image = handler.getWallImage(wall.getType(), wall.getWallDirection());
                    drawImage(graphic, image, wall.getBounds());
                }
            }
        }
    }

    private void drawStaticObjects(Graphics2D graphic) {
        for (IStaticObject o : map.getStaticObjects()) {

            // Optimize later
            if (!(o instanceof IStaticDrawableObject)) {
                throw new IllegalArgumentException("Object should be instance of IStaticDrawableObject");
            }

            if (isVisible(graphic, o.getBounds())) {

                IStaticDrawableObject obj = (IStaticDrawableObject) o;

                if (!obj.isWall()) {
                    BufferedImage image = handler.getStaticObjectImage(obj.getType());
                    drawImage(graphic, image, obj.getBounds());
                }
            }
        }
    }

    /* Sentrerer kamera på player, holder seg innenfor bounds */
    private void centerCamera(Graphics2D graphic) {

        Rectangle2D.Double playerHitbox = this.map.getPlayer().getHitbox();
        Rectangle2D.Double mapBounds = map.getBounds();

        double screenWidth = graphic.getClipBounds().getWidth();
        double screenHeight = graphic.getClipBounds().getHeight();

        camera.update(playerHitbox, screenWidth, screenHeight, mapBounds);
        camera.apply(graphic);
    }

    private void drawBackground(Graphics2D graphic) {
        drawImage(graphic, this.handler.getBackground(this.map.level()), MAPDIMENSION);
        drawCellsInView(graphic, this.tiles, this.handler, false);
    }

    private void drawPlayer(Graphics2D graphic) {
        IViewablePlayer player = (IViewablePlayer) this.map.getPlayer();

        BufferedImage feet = handler.getPlayerFeetSprite(player.getAnimationIndex());
        BufferedImage body = handler.getPlayerBodySprite(player.getAnimationIndex());

        Rectangle2D.Double feetBounds = new Rectangle2D.Double(
                player.getHitbox().getX() + player.getHitbox().getWidth() * 0.15,
                player.getHitbox().getY() + player.getHitbox().getHeight() * 0.10,
                player.getHitbox().getWidth() * 0.7,
                player.getHitbox().getHeight() * 0.7);

        drawRotated(graphic, feet, feetBounds, player.getFacingAngle());
        drawRotated(graphic, body, player.getHitbox(), player.getFacingAngle());
    }

    private void drawActiveItems(Graphics2D graphic) {
        for (ICollectable item : map.getActiveItems()) {
            if (isVisible(graphic, item.getHitbox())) {
                BufferedImage image = handler.getCollectableImage(item.getType());
                double centerX = item.getHitbox().getCenterX();
                double centerY = item.getHitbox().getCenterY();
                int imgW = image.getWidth();
                int imgH = image.getHeight();
                int drawX = (int) (centerX - (imgW / 2.0));
                int drawY = (int) (centerY - (imgH / 2.0));
                graphic.drawImage(image, drawX, drawY, imgW, imgH, null);
            }
        }
    }

    private void drawEnemies(Graphics2D graphic) {
        for (IEnemy e : map.getEnemies()) {
            if (isVisible(graphic, e.getHitbox())) {

                Rectangle2D.Double hitbox = e.getHitbox();

                drawRotated(
                        graphic,
                        handler.getEnemySprites(e.getEnemyType(), e.currentAction(), e.getAnimationIndex()),
                        new Rectangle2D.Double(hitbox.getX() - 0.25 * hitbox.width,
                                hitbox.getY() - 0.25 * hitbox.height,
                                hitbox.width * 1.5,
                                hitbox.height * 1.5),
                        e.getFacingAngle());
            }
        }

    }

    private void drawRotated(Graphics2D g2, BufferedImage img, Rectangle2D.Double hb, double angle) {
        var old = g2.getTransform();
        g2.translate(hb.getCenterX(), hb.getCenterY());
        g2.rotate(angle);
        g2.drawImage(img, (int) -hb.width / 2, (int) -hb.height / 2, (int) hb.width, (int) hb.height, null);

        g2.setTransform(old);
    }

}
