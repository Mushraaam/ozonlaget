package no.uib.inf112.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.utility.ImageReader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class LoadScreen extends JPanel implements IDrawer{

    private ImageIcon gif;
    private BufferedImage cage;
    private static final Rectangle2D.Double CAGE = new Rectangle2D.Double(300, 300, 100, 100);

    public LoadScreen() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.BLACK);
        this.gif = new ImageIcon(getClass().getResource("/no/uib/inf112/loadingicon.gif"));

        setLayout(new BorderLayout());
        JLabel giflabel = new JLabel(gif);
        giflabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(giflabel, BorderLayout.CENTER);

        this.cage = ImageReader.fetchImage("/no/uib/inf112/loadingCage.png");

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw((Graphics2D)g);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.WHITE);
        g.drawString("Please wait...", 30, 40);

        drawImage(g, cage, CAGE);
    }
}
