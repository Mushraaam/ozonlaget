package no.uib.inf112.core;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import no.uib.inf112.map.Map;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.controller.Controller;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;

public class Main {

    public static void main(String[] args) {
        IMap map = new Map();
        GameDrawer view = new GameDrawer(map);
        JFrame frame = new JFrame("Ozonlaget");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        new Controller(map, view);
        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
