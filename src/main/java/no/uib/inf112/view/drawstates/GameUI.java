package no.uib.inf112.view.drawstates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IViewablePlayer;
import no.uib.inf112.utility.ImageHandler;

public class GameUI implements IDrawer {

    private static final String ARIAL = "Arial";

    private static final double UI_HEIGHT = Config.getInt("uiSize");
    private static final int GUN_WIDTH = Config.getInt("uiGunWidth");
    private static final int GUN_HEIGHT = Config.getInt("uiGunHeight");
    private static final Font AMMO_FONT = new Font(ARIAL, Font.BOLD, 46);
    private static final Color AMMO_COLOR = new Color(50, 201, 23); // neon green
    // healthBar
    private static final int HP_BAR_WIDTH = 350;
    private static final int HP_BAR_HEIGHT = 70;
    private static final Color HP_BACK = new Color(25, 25, 25, 180);
    private static final Color HP_BORDER = new Color(200, 200, 200, 180);
    private static final Color GREEN = new Color(50, 201, 23);
    private static final Color ORANGE = new Color(255, 215, 0);
    private static final Color RED = new Color(255, 70, 70);
    private static final Font HP_TEXT_FONT = new Font(ARIAL, Font.BOLD, 30);
    private static final Color HP_TEXT_COLOR = Color.BLACK;
    // inventory
    private static final int INV_WIDTH = 80;
    private static final int SLOT_SIZE = 60;
    private static final Color INV_BG = new Color(0, 0, 0, 150);
    private ImageHandler handler;
    private BufferedImage uiBar;
    private IModel map;
    // objectives TAB
    private static final Font OBJECTIVE_TITLE_FONT = new Font(ARIAL, Font.BOLD, 18);
    private static final Font OBJECTIVE_TEXT_FONT = new Font(ARIAL, Font.BOLD, 15);
    private static final Color OBJECTIVE_BG_COLOR = new Color(0, 0, 0, 170);
    private static final Color OBJECTIVE_INCOMPLETE = Color.WHITE;
    private static final Color OBJECTIVE_COMPLETE = new Color(50, 201, 23);
    private static final int OBJECTIVE_WIDTH = 300;
    private static final int OBJECTIVE_LINE_HEIGHT = 22;


    public GameUI(IModel map, ImageHandler handler) {
        this.map = map;
        this.handler = handler;
        this.uiBar = handler.uiBar();
    }

    @Override
    public void draw(Graphics2D graphic) {
        IViewablePlayer player = (IViewablePlayer) this.map.getPlayer();
        Rectangle2D bounds = graphic.getClipBounds().getBounds2D();
        double x1 = bounds.getMinX();
        double width = bounds.getMaxX() - x1;
        double y2 = bounds.getMaxY();
        double y1 = y2 - UI_HEIGHT;

        graphic.setColor(Color.DARK_GRAY);
        drawImage(graphic, uiBar, new Rectangle2D.Double(x1, y1, width, UI_HEIGHT));

        // Gun
        drawImage(graphic, this.handler.getGunImage(player.gunType()),
                new Rectangle2D.Double(x1 + 330, y1 + 40, GUN_WIDTH, GUN_HEIGHT));

        // Ammunition
        graphic.setColor(AMMO_COLOR);
        int currentAmmo = player.currentAmmunition();
        int maxAmmo = player.maxAmmunition();

        graphic.setFont(AMMO_FONT);
        graphic.drawString(String.format("%s/%s", currentAmmo, maxAmmo), (int) x1 + 100, (int) y1 + 98);

        // Buff
        int buffTimer = player.buffCountDown();
        graphic.drawString(String.format("%s", buffTimer), (int) x1 + 610, (int) y1 + 98);

        // HealthBar
        drawHealthBar(graphic, (int) x1 + 755, (int) y1 + 45);
        // Objectives TAB
        drawObjectivesTab(graphic);

        drawPlayerInventory(graphic);
    }

    private void drawPlayerInventory(Graphics2D g) {
        IViewablePlayer player = (IViewablePlayer) map.getPlayer();
        if (!player.getInventory().isVisible()) {
            drawInventoryTooltip(g);
            return;
        }
        int itemSlots = player.getInventory().getItems().size();
        int dynamicHeight = (itemSlots > 0) ? (itemSlots * (SLOT_SIZE + 20)) + 20 : 60;

        Rectangle2D bounds = g.getClipBounds().getBounds2D();
        int x = (int) bounds.getMaxX() - INV_WIDTH - 10;
        int y = (int) bounds.getMinY() + 120;
        // bg
        g.setColor(INV_BG);
        g.fillRoundRect(x, y, INV_WIDTH, dynamicHeight, 15, 15);

        int currentY = y + 30;

        for (Map.Entry<CollectableType, Integer> entry : player.getInventory().getItems()) {
            CollectableType type = entry.getKey();
            int count = entry.getValue();

            drawSlot(g, x + 10, currentY, type, count);

            // mv the next slot down
            currentY += SLOT_SIZE + 20;
        }
    }

    private void drawInventoryTooltip(Graphics2D g) {
        Rectangle2D bounds = g.getClipBounds().getBounds2D();
        int tooltipWidth = 100;
        int tooltipHeight = 30;

        int x = (int) bounds.getMaxX() - tooltipWidth - 10;
        int y = (int) bounds.getMinY() + 120;
        g.setColor(INV_BG);
        g.fillRoundRect(x, y - 5, tooltipWidth, tooltipHeight + 5, 10, 10);

        g.setColor(Color.WHITE);
        g.setFont(new Font(ARIAL, Font.BOLD, 12));
        String text = "(B)ackpack";
        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (tooltipWidth - metrics.stringWidth(text)) / 2;
        int textY = y + ((tooltipHeight - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, textX, textY);
    }

    private void drawObjectivesTab(Graphics2D g) {
        IViewablePlayer player = (IViewablePlayer) map.getPlayer();
        Rectangle2D bounds = g.getClipBounds().getBounds2D();

        int gasCollected = player.getAmountInInventory(CollectableType.GASCAN);
        int chopperkeyCollected = player.getAmountInInventory(CollectableType.CHOPPERKEY);
        int gatekeyCollected = player.getAmountInInventory(CollectableType.GATEKEY);

        boolean gasDone = gasCollected >= 6;
        boolean chopperkeyDone = chopperkeyCollected >= 1;
        boolean gatekeyDone = gatekeyCollected >= 1;

        boolean allDone = gasDone && chopperkeyDone && gatekeyDone;

        int x = (int) bounds.getMaxX() - OBJECTIVE_WIDTH - 10;
        int y = (int) bounds.getMinY();
        int height = 120;

        g.setColor(OBJECTIVE_BG_COLOR);
        g.fillRoundRect(x, y, OBJECTIVE_WIDTH, height, 15, 15);

        g.setFont(OBJECTIVE_TITLE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Objectives", x + 15, y + 22);

        g.setFont(OBJECTIVE_TEXT_FONT);

        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT * 3, allDone, allDone ? "Ready to escape!" : "find all required items to escape!");
        drawObjectiveLine(g, x + 15, y + 50, gasDone, String.format("Gas cans: %d/6", Math.min(gasCollected, 6)));
        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT, chopperkeyDone, String.format("Chopper key: %d/1", Math.min(chopperkeyCollected, 1)));
        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT * 2, gatekeyDone, String.format("Gate key: %d/1", Math.min(gatekeyCollected, 1)));
        
    }

    private void drawObjectiveLine(Graphics2D g, int x, int y, boolean completed, String text) {
        g.setColor(completed ? OBJECTIVE_COMPLETE : OBJECTIVE_INCOMPLETE);
        String prefix = completed ? "[X] " : "[  ] ";
        g.drawString(prefix + text, x, y);
        
    }

    private void drawSlot(Graphics2D g, int x, int y, CollectableType item, int count) {
        g.setColor(new Color(255, 255, 255, 40));
        g.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 10, 10);

        BufferedImage img = handler.getCollectableImage(item);
        if (img != null) {
            double imgW = img.getWidth();
            double imgH = img.getHeight();

            // dont fill entirely
            double maxSize = SLOT_SIZE * 0.8;

            // keep ratio
            double scale = Math.min(maxSize / imgW, maxSize / imgH);

            int drawW = (int) (imgW * scale);
            int drawH = (int) (imgH * scale);

            // center
            int drawX = x + (SLOT_SIZE - drawW) / 2;
            int drawY = y + (SLOT_SIZE - drawH) / 2;

            g.drawImage(img, drawX, drawY, drawW, drawH, null);
        }
        if (count >= 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(ARIAL, Font.BOLD, 16));
            String text = String.valueOf(count);
            g.drawString(text, x + SLOT_SIZE - 20, y + SLOT_SIZE - 5);
        }
    }

    private void drawHealthBar(Graphics2D g, int x1, int y1) {
        IViewablePlayer player = (IViewablePlayer) this.map.getPlayer();
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

        int innerWidth = (int) ((HP_BAR_WIDTH - 3 * 2) * percentHP);
        g.setColor(fill);
        g.fillRoundRect(x1 + 3, y1 + 3, innerWidth, HP_BAR_HEIGHT - 3 * 2, 8, 8);

        // border
        g.setColor(HP_BORDER);
        g.drawRoundRect(x1, y1, HP_BAR_WIDTH, HP_BAR_HEIGHT, 10, 10);

        // armor
        int armor = player.getArmor();
        if (armor > 0 && currentHP > 0) {
            g.setColor(Color.blue);
            g.setStroke(new BasicStroke((float) armor + 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawRoundRect(x1 + 3, y1 + 3, innerWidth, HP_BAR_HEIGHT - 3 * 2, 8, 8);
        }

        g.setFont(HP_TEXT_FONT);
        String hpText = String.format("%d/%d", currentHP, maxHp);
        g.setColor(HP_TEXT_COLOR);
        g.drawString(hpText, x1 + 110, y1 + 45);

    }
}
