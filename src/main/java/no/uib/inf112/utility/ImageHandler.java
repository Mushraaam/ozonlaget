package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageHandler {

    //PlayerSprite (put into hashmap layer?)
    private static final int PLAYER_SPRITE_COUNT = 8;
    private ArrayList<BufferedImage> playerNorth;
    private ArrayList<BufferedImage> playerSouth;
    private ArrayList<BufferedImage> playerWest;
    private ArrayList<BufferedImage> playerEast;
    private ArrayList<BufferedImage> playerNorthWest;
    private ArrayList<BufferedImage> playerNorthEast;
    private ArrayList<BufferedImage> playerSouthWest;
    private ArrayList<BufferedImage> playerSouthEast;

    public ImageHandler(){
        loadPlayerSprite();
    }

    private void loadPlayerSprite() {
        this.playerNorth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerSouth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerNorthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerNorthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerSouthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        this.playerSouthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);

        for (int i = 1; i<=PLAYER_SPRITE_COUNT;i++){
            this.playerNorth.add(ImageReader.fetcImage(String.format("src/main/java/no/resources/player/player%s_north.png", i)));
            this.playerSouth.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south.png", i)));
            this.playerWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_west.png", i)));
            this.playerEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_east.png", i)));
            this.playerNorthWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_north_west.png", i)));
            this.playerNorthEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_north_east.png", i)));
            this.playerSouthWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south_west.png", i)));
            this.playerSouthEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south_east.png", i)));
        }
    }
}
