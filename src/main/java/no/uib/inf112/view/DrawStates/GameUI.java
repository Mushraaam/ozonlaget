package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.config.Config;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.utility.ImageHandler;

public class GameUI implements IDrawer {

    private IPlayer player;
    private ImageHandler handler;
    private BufferedImage uiBar;

    private static final double UI_HEIGHT = Config.getInt("uiSize");
    private static final int GUN_WIDTH = Config.getInt("uiGunWidth");
    private static final int GUN_HEIGHT = Config.getInt("uiGunHeight");

    private static final Font AMMO_FONT = new Font("Arial", Font.BOLD, 46);
    private static final Color AMMO_COLOR = new Color(57, 255, 20); // neon green

    // healthBar
    private static final int HP_BAR_WIDTH = 350;
    private static final int HP_BAR_HEIGHT = 70;
    private static final Color HP_BACK = new Color(25, 25, 25, 180);
    private static final Color HP_BORDER = new Color(200, 200, 200, 180);
    private static final Color GREEN = new Color(57, 255, 20);
    private static final Color ORANGE = new Color(255, 215, 0);
    private static final Color RED = new Color(255, 70, 70);
    private static final Font HP_TEXT_FONT = new Font("Arial", Font.BOLD, 30);
    private static final Color HP_TEXT_COLOR = Color.BLACK;

    public GameUI(IMap map, ImageHandler handler) {
        this.player = map.getPlayer();
        this.handler = handler;
        this.uiBar = handler.uiBar();
    }

    @Override
    public void draw(Graphics2D graphic) {

        Rectangle2D bounds = graphic.getClipBounds().getBounds2D();
        double x1 = bounds.getMinX();
        double width = bounds.getMaxX() - x1;
        double y2 = bounds.getMaxY();
        double y1 = y2 - UI_HEIGHT;

        graphic.setColor(Color.DARK_GRAY);
        drawImage(graphic, uiBar, new Rectangle2D.Double(x1, y1, width, UI_HEIGHT));

        // Gun
        drawImage(graphic, this.handler.getGunImage(this.player.gunType()),
                new Rectangle2D.Double(x1 + 330, y1 + 40, GUN_WIDTH, GUN_HEIGHT));

        // Ammunition
        graphic.setColor(AMMO_COLOR);
        int currentAmmo = this.player.currentAmmunition();
        int maxAmmo = this.player.maxAmmunition();

        graphic.setFont(AMMO_FONT);
        graphic.drawString(String.format("%s/%s", currentAmmo, maxAmmo), (int) x1 + 100, (int) y1 + 98);

        
        // Buff
        int buffTimer = this.player.buffCountDown();
        graphic.drawString(String.format("%s", buffTimer), (int) x1 + 610, (int) y1 + 98);



        // HealthBar
        drawHealthBar(graphic, (int) x1 + 755, (int) y1 + 45);
    }

    private void drawHealthBar(Graphics2D g, int x1, int y1) {

        int maxHp = player.getMaxHP();
        int currentHP = player.getCurrentHP();

        // background
        g.setColor(HP_BACK);
        g.fillRoundRect(x1, y1, HP_BAR_WIDTH, HP_BAR_HEIGHT, 10, 10);

        // filling
        double percentHP = currentHP / (double) maxHp;
        Color fill;
        if (percentHP >= 0.7) {
            fill = GREEN;
        } else if (percentHP >= 0.4) {
            fill = ORANGE;
        } else {
            fill = RED;
        }

        int innerWidth = (int)((HP_BAR_WIDTH - 3 * 2) * percentHP);
        g.setColor(fill);
        g.fillRoundRect(x1 + 3, y1 + 3, innerWidth, HP_BAR_HEIGHT - 3 * 2, 8, 8);

        // border
        g.setColor(HP_BORDER);
        g.drawRoundRect(x1, y1, HP_BAR_WIDTH, HP_BAR_HEIGHT, 10, 10);

        //armor
        int armor = player.getArmor();
        if(armor > 0 && currentHP > 0 ){
            g.setColor(Color.blue);
            g.setStroke(new BasicStroke(armor+2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawRoundRect(x1 + 3, y1 + 3, innerWidth, HP_BAR_HEIGHT - 3 * 2, 8, 8);
        }

        g.setFont(HP_TEXT_FONT);
        String hpText = String.format("%d/%d", currentHP, maxHp);
        g.setColor(HP_TEXT_COLOR);
        g.drawString(hpText, x1 + 110, y1 + 45);

    }
}
