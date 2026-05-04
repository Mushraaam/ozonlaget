package no.uib.inf112.view.drawstates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.interfaces.IPlayer;
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
    private static final Font HP_TEXT_FONT_BOSS = new Font(ARIAL, Font.BOLD, 20);
    private static final Color HP_TEXT_COLOR = Color.BLACK;
    // inventory
    private static final int INV_WIDTH = 80;
    private static final int SLOT_SIZE = 60;
    private static final Color INV_BG = new Color(0, 0, 0, 150);
    private ImageHandler handler;
    private BufferedImage uiBar;
    private IModel map;
    private IViewablePlayer player;
    // objectives TAB
    private static final Font OBJECTIVE_TITLE_FONT = new Font(ARIAL, Font.BOLD, 18);
    private static final Font OBJECTIVE_TEXT_FONT = new Font(ARIAL, Font.BOLD, 15);
    private static final Color OBJECTIVE_BG_COLOR = new Color(0, 0, 0, 170);
    private static final Color OBJECTIVE_INCOMPLETE = Color.WHITE;
    private static final Color OBJECTIVE_COMPLETE = new Color(50, 201, 23);
    private static final int OBJECTIVE_WIDTH = 300;
    private static final int OBJECTIVE_LINE_HEIGHT = 22;

    // killcount
    private BufferedImage killCountIcon;

    /**
     * Draws the games UI
     * @param map
     * @param handler
     */
    public GameUI(IModel map, ImageHandler handler) {
        this.map = map;
        this.handler = handler;
        this.uiBar = handler.uiBar();
        this.killCountIcon = handler.getKillCountIcon();
        IPlayer playerCand = this.map.getPlayer();
        if (playerCand instanceof IViewablePlayer){
            this.player = (IViewablePlayer)playerCand;
        }else{
            throw new IllegalArgumentException("Player must be of type IViewablePlayer");
        }

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
        drawObjectives(graphic);
        drawBossBarIfVisible(graphic);

        drawPlayerInventory(graphic);
        drawKillCountWindow(graphic);
    }

    private void drawPlayerInventory(Graphics2D g) {
        if (!player.getInventory().isVisible()) {
            drawInventoryTooltip(g);
            return;
        }
        int itemSlots = player.getInventory().getItems().size();
        int dynamicHeight = (itemSlots > 0) ? (itemSlots * (SLOT_SIZE + 20)) + 20 : 60;

        Rectangle2D bounds = g.getClipBounds().getBounds2D();
        int x = (int) bounds.getMaxX() - INV_WIDTH - 10;
        int y = (int) bounds.getMinY() + 120;

        if (!player.objectivesVisible()) {
            y -= 90; // move up backpack to align with a folded objectives tab
        }
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

    private void drawKillCountWindow(Graphics2D g) {
        updateKillNotifications();

        Rectangle2D b = g.getClipBounds().getBounds2D();
        int x = (int) b.getMinX() + 10;
        int y = (int) b.getMinY() + 10;
        int w = 140;
        int h = 45;

        g.setColor(INV_BG);
        g.fillRoundRect(x, y, w, h, 20, 20);
        if (killCountIcon != null) {
            g.drawImage(killCountIcon, x + 5, y + 5, h - 10, h - 10, null);
        }
        drawCenteredString(g, String.valueOf(map.getPlayer().getKillCount()),
                x + h, y, w - h, h);
        for (KillNotify n : activeNotifications) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, n.alpha));

            g.setColor(Color.RED);
            g.setFont(new Font("Impact", Font.PLAIN, 18));

            int notifyX = x + w + 2 + (int) n.x; // Anchor + Width + Padding + Physics X
            int notifyY = y + h + (int) n.y; // Anchor + Center Y + Physics Y

            g.drawString("+1", notifyX, notifyY);
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Boilerplate helper to draw centered text within a specific bounding box
     */
    private void drawCenteredString(Graphics2D g, String text, int x, int y, int w, int h) {
        g.setColor(new Color(150, 0, 0));
        g.setFont(new Font("Serif", Font.BOLD, 22));

        FontMetrics fm = g.getFontMetrics();
        int tx = x + (w - fm.stringWidth(text)) / 2;
        int ty = y + ((h - fm.getHeight()) / 2) + fm.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(text, tx + 1, ty + 1);

        g.setColor(new Color(180, 0, 0));
        g.drawString(text, tx, ty);
    }

    private void drawInventoryTooltip(Graphics2D g) {
        Rectangle2D bounds = g.getClipBounds().getBounds2D();
        int tooltipWidth = 100;
        int tooltipHeight = 30;

        int x = (int) bounds.getMaxX() - tooltipWidth - 10;
        int y = (int) bounds.getMinY() + 130;

        if (!player.objectivesVisible()) {
            y -= 90; // move up backpack to align with a folded objectives tab
        }

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

    private void drawObjectivesTooltip(Graphics2D g) {
        Rectangle2D bounds = g.getClipBounds().getBounds2D();
        int tooltipWidth = 100;
        int tooltipHeight = 30;

        int x = (int) bounds.getMaxX() - tooltipWidth - 10;
        int y = (int) bounds.getMinY() + 10;
        g.setColor(OBJECTIVE_BG_COLOR);
        g.fillRoundRect(x, y - 5, tooltipWidth, tooltipHeight + 5, 10, 10);

        g.setColor(Color.WHITE);
        g.setFont(new Font(ARIAL, Font.BOLD, 12));
        String text = "(O)bjectives";
        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (tooltipWidth - metrics.stringWidth(text)) / 2;
        int textY = y + ((tooltipHeight - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, textX, textY);
    }

    private void drawObjectivesTab(Graphics2D g) {
        Rectangle2D bounds = g.getClipBounds().getBounds2D();

        int gasCollected = player.getInventory().getCollectedGasCans();
        int chopperKeycardCollected = player.getAmountInInventory(CollectableType.CHOPPER_KEYCARD);
        int gatekeyCollected = player.getAmountInInventory(CollectableType.GATE_KEY);

        boolean gasDone = gasCollected >= 6;
        boolean chopperKeycardDone = chopperKeycardCollected >= 1;
        boolean gatekeyDone = gatekeyCollected >= 1;

        boolean allDone = gasDone && chopperKeycardDone && gatekeyDone;

        int x = (int) bounds.getMaxX() - OBJECTIVE_WIDTH - 10;
        int y = (int) bounds.getMinY();
        int height = 120;

        g.setColor(OBJECTIVE_BG_COLOR);
        g.fillRoundRect(x, y, OBJECTIVE_WIDTH, height, 15, 15);

        g.setFont(OBJECTIVE_TITLE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Objectives", x + 15, y + 22);

        g.setFont(OBJECTIVE_TEXT_FONT);

        drawObjectiveLine(g, x + 15, y + 50, allDone,
                allDone ? "Ready to escape!" : "Find all required items to escape!");
        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT, gasDone,
                String.format("Gas cans: %d/6", Math.min(gasCollected, 6)));
        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT * 2, chopperKeycardDone,
                String.format("Chopper keycard: %d/1", Math.min(chopperKeycardCollected, 1)));
        drawObjectiveLine(g, x + 15, y + 50 + OBJECTIVE_LINE_HEIGHT * 3, gatekeyDone,
                gatekeyDone ? "Gate is now open" : String.format("Gate key: %d/1", Math.min(gatekeyCollected, 1)));

    }

    private void drawObjectiveLine(Graphics2D g, int x, int y, boolean completed, String text) {
        g.setColor(completed ? OBJECTIVE_COMPLETE : OBJECTIVE_INCOMPLETE);
        String prefix = completed ? "[X] " : "[  ] ";
        g.drawString(prefix + text, x, y);

    }

    private void drawObjectives(Graphics2D g) {
        if (!player.objectivesVisible()) {
            drawObjectivesTooltip(g);
            return;
        }
        drawObjectivesTab(g);
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

    private void drawBossBarIfVisible(Graphics2D graphic) {
        Rectangle2D cameraBounds = graphic.getClipBounds().getBounds2D();

        for (IEnemy enemy : map.getEnemies()) {
            if (enemy.getEnemyType() == no.uib.inf112.enums.EnemyType.MEGABOSS && enemy.isAlive()) {
                if (cameraBounds.intersects(enemy.getHitbox())) {

                    int bossBarWidth = 500;

                    int x = (int) (cameraBounds.getMinX() + (cameraBounds.getWidth() - bossBarWidth) / 2);
                    int y = (int) cameraBounds.getMinY() + 20;

                    drawHealthBarBoss(graphic, x, y, enemy, bossBarWidth);
                }
            }
        }
    }

    private void drawHealthBarBoss(Graphics2D g, int x, int y, IEnemy boss, int barWidth) {
        int maxHp = boss.getMaxHealth();
        int currentHP = boss.getHealth();
        int barHeight = 25;

        // Background
        g.setColor(HP_BACK);
        g.fillRoundRect(x, y, barWidth, barHeight, 50, 50);

        // Red Filling
        double percentHP = Math.max(0, currentHP / (double) maxHp);
        int innerWidth = (int) ((barWidth - 6) * percentHP);

        if (innerWidth > 0) {
            g.setColor(Color.red);
            g.fillRoundRect(x + 3, y + 3, innerWidth, barHeight - 6, 10, 10);
        }
        // Border
        g.setColor(HP_BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, barWidth, barHeight, 15, 15);

        // Centered Text
        g.setFont(HP_TEXT_FONT_BOSS);
        String hpText = String.format("GIGACHAD: %d / %d", currentHP, maxHp);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (barWidth - fm.stringWidth(hpText)) / 2;
        int textY = y + ((barHeight - fm.getHeight()) / 2) + fm.getAscent();

        // Drop shadow for readability against the red bar
        g.setColor(Color.BLACK);
        g.drawString(hpText, textX + 2, textY + 2);
        g.setColor(Color.WHITE);
        g.drawString(hpText, textX, textY);
    }

    private ArrayList<KillNotify> activeNotifications = new ArrayList<>();
    private int lastKillCount = -1;

    private void updateKillNotifications() {
        int currentKills = map.getPlayer().getKillCount();
        if (lastKillCount == -1) {
            lastKillCount = currentKills;
        }
        // If kills increased, add a new +1
        if (currentKills > lastKillCount) {
            activeNotifications.add(new KillNotify(activeNotifications.size()));
            lastKillCount = currentKills;
        }

        // Remove finished animations
        activeNotifications.removeIf(n -> !n.update());
    }

    private static class KillNotify {
        float x, y, vx, vy;
        float alpha = 1.0f;

        KillNotify(int activeCount) {
            float pressure = 1.0f + (activeCount * 0.5f);
            this.vx = 0.2f + (float) (Math.random() * pressure);
            // upward thrust //(giggity)
            this.vy = -0.2f - (pressure);
        }

        boolean update() {
            x += vx; // Sideways spread
            y += vy; // Vertical ascent
            vx *= 0.95f;
            alpha -= 0.015f;
            return alpha > 0;
        }
    }
}
