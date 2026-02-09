package no.uib.inf112.core;

import java.awt.geom.Rectangle2D;

import javax.imageio.ImageReader;
import javax.swing.JFrame;

import no.uib.inf112.map.Map;
import no.uib.inf112.player.Player;
import no.uib.inf112.controller.Controller;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.view.GameDrawer;

public class Main {
    

    public static void main(String[] args) {
        IMap map = new Map();
        new Controller(map);
        GameDrawer view = new GameDrawer(map);
        JFrame frame = new JFrame("yeahboi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
