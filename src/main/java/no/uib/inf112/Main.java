package no.uib.inf112;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import no.uib.inf112.controller.Controller;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.Model;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.view.LoadScreen;
import no.uib.inf112.view.LoadStatus;

public class Main {

    public static void main(String[] args) {

        // Load screen
        LoadStatus status = new LoadStatus();
        JFrame loadingFrame = new JFrame("Loading Model");
        LoadScreen loadScreen = new LoadScreen(status);
        loadingFrame.setContentPane(loadScreen);
        loadingFrame.pack();
        loadingFrame.setResizable(false);
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
        
        // Start loading the game
        IModel map = new Model(status);
        GameDrawer view = new GameDrawer(map, status);
        status.setStatus("Packing frame...", 95);
        JFrame frame = new JFrame("Ozonlaget");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        status.setStatus("Initiallizing controls...", 99);
        new Controller(map, view);
        
        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Close loadscreen
        loadingFrame.dispose();

        // Display game
        frame.setVisible(true);
    }
}
