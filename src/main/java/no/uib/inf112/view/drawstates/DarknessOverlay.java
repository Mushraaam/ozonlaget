package no.uib.inf112.view.drawstates;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;

public class DarknessOverlay implements IDrawer {

    private static final float LIGHT_RADIUS = 600f;
    private static final Color INNER_COLOR_DEFAULT = new Color(0, 0, 0, 255);
    private static final Color INNER_COLOR_DAMAGE = new Color(0, 0, 0, 255);

    private static final Color INNER_COLOR = new Color(0, 0, 0, 255);
    private static final Color OUTER_COLOR = new Color(0, 0, 0, 0);

    private final IMap map;
    private BufferedImage overlay;

    /* https://www.youtube.com/watch?v=GMaterkzOSk */
    public DarknessOverlay(IMap map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics2D graphic) {
        Paint oldPaint = graphic.getPaint();
        Composite oldComposite = graphic.getComposite();
        Object oldAA = graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        Rectangle2D clip = graphic.getClipBounds();
        if (clip == null) {
            return;
        }

        int width = Math.max(1, (int) Math.ceil(clip.getWidth()));
        int height = Math.max(1, (int) Math.ceil(clip.getHeight()));
        if (overlay == null || overlay.getWidth() != width || overlay.getHeight() != height) {
            overlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.setComposite(AlphaComposite.SrcOver);

        Rectangle2D.Double hitbox = map.getPlayer().getHitbox();
        double lightX = hitbox.getCenterX();
        double lightY = hitbox.getCenterY();

        Graphics2D overlayGraphics = overlay.createGraphics();
        overlayGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        overlayGraphics.setComposite(AlphaComposite.Clear);
        overlayGraphics.fillRect(0, 0, width, height);

        overlayGraphics.setComposite(AlphaComposite.SrcOver);
        int redness = map.getPlayer().buffType() == BuffType.DAMAGE ? map.getPlayer().buffCountDown() * 4 : 0;
        int greenness = map.getPlayer().buffType() == BuffType.SPEED ? map.getPlayer().buffCountDown() * 4 : 0;
        overlayGraphics.setColor(new Color(redness, greenness, 0, 220));
        overlayGraphics.fillRect(0, 0, width, height);

        drawLightGlow(
                overlayGraphics,
                lightX - clip.getX(),
                lightY - clip.getY());
        overlayGraphics.dispose();

        graphic.drawImage(overlay, (int) clip.getX(), (int) clip.getY(), null);

        graphic.setPaint(oldPaint);
        graphic.setComposite(oldComposite);
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }

    private void drawLightGlow(Graphics2D g, double centerX, double centerY) {
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point2D.Double(centerX, centerY),
                LIGHT_RADIUS,
                new float[] { 0f, 1f },
                new Color[] {
                        INNER_COLOR, OUTER_COLOR
                });

        Paint oldPaint = g.getPaint();
        Composite oldComposite = g.getComposite();

        g.setComposite(AlphaComposite.DstOut);
        g.setPaint(rgp);
        g.fill(new Ellipse2D.Double(
                centerX - LIGHT_RADIUS,
                centerY - LIGHT_RADIUS,
                LIGHT_RADIUS * 2.0,
                LIGHT_RADIUS * 2.0));

        g.setPaint(oldPaint);
        g.setComposite(oldComposite);
    }
}