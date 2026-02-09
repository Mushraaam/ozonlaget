package no.uib.inf112.view;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.DrawStates.GameScreen;

public class GameDrawer extends JPanel{
    
    private IMap map;
    private IDrawer gameScreen;

    public GameDrawer(IMap map){
        this.map = map;
        this.gameScreen = new GameScreen(this.map);
        this.setPreferredSize(new Dimension(1200, 800));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        if (true){ //Fremtidig sjekk om gamemode == playing
            this.gameScreen.draw(g2);
        }
    }



}
