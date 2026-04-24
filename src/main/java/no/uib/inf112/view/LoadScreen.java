package no.uib.inf112.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Graphics;


public class LoadScreen extends JPanel {

    private ImageIcon gif;

    public LoadScreen() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.BLACK);
        this.gif = new ImageIcon(getClass().getResource("/no/uib/inf112/loadingicon.gif"));

        setLayout(new BorderLayout());
        JLabel giflabel = new JLabel(gif);
        giflabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(giflabel, BorderLayout.CENTER);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.WHITE);
        g.drawString("Please wait...", 30, 40);

    }
}
