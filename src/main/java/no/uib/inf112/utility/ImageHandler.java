package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf112.enums.Direction;

public class ImageHandler {

    //PlayerSprite (put into hashmap layer?)
    private static final int PLAYER_SPRITE_COUNT = 8;
    private HashMap<Direction, ArrayList<BufferedImage>> playerSprites;

    public ImageHandler(){
        this.playerSprites = new HashMap<>();
        loadPlayerSprite();
    }


    //////////////////////// PLAYER METHODS //////////////////////////////////
    private void loadPlayerSprite() {

        //this should probably be elegantified later
        ArrayList<BufferedImage> playerNorth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerNorthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerNorthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);

        this.playerSprites.put(Direction.NORTH, playerNorth);

        for (int i = 1; i<=PLAYER_SPRITE_COUNT;i++){
            playerNorth.add(ImageReader.fetcImage(String.format("src/main/java/no/resources/player/player%s_north.png", i)));
            playerSouth.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south.png", i)));
            playerWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_west.png", i)));
            playerEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_east.png", i)));
            playerNorthWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_north_west.png", i)));
            playerNorthEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_north_east.png", i)));
            playerSouthWest.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south_west.png", i)));
            playerSouthEast.add(ImageReader.fetcImage(String.format("src\\main\\java\\no\\resources\\player\\player%s_south_east.png", i)));
        }

        this.playerSprites.put(Direction.NORTH, playerNorth);
        this.playerSprites.put(Direction.SOUTH, playerSouth);
        this.playerSprites.put(Direction.WEST, playerWest);
        this.playerSprites.put(Direction.EAST, playerEast);
        this.playerSprites.put(Direction.NORTH_WEST, playerNorthWest);
        this.playerSprites.put(Direction.NORTH_EAST, playerNorthEast);
        this.playerSprites.put(Direction.SOUTH_WEST, playerSouthWest);
        this.playerSprites.put(Direction.SOUTH_EAST, playerSouthEast);

    }

    public BufferedImage getPlayerSprite(Direction dir, int index) {
            return this.playerSprites.get(dir).get(index);
        }
    


    //////////////////////////////// END PLAYER METHODS //////////////////////////
}


