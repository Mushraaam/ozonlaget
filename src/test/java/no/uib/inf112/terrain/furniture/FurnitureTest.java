package no.uib.inf112.terrain.furniture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.Test;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;

public class FurnitureTest {
    Rectangle2D.Double bounds = new Rectangle2D.Double(10, 10, 
        Config.getInt("tableWidth"), Config.getInt("tableHeight"));
@Test
void testAllFurniture() {

    BeigeBigBed bigBeigeBed = new BeigeBigBed(bounds);
    assertFalse(bigBeigeBed.isWall());
    assertEquals(bounds, bigBeigeBed.getBounds());
    assertEquals(StaticObjectType.BEIGE_BIG_BED, bigBeigeBed.getType());

    BeigeCouch beigeCouch = new BeigeCouch(bounds);
    assertFalse(beigeCouch.isWall());
    assertEquals(bounds, beigeCouch.getBounds());
    assertEquals(StaticObjectType.BEIGE_COUCH_DOWN, beigeCouch.getType());

    BeigeSmallBed beigeSmallBed = new BeigeSmallBed(bounds);
    assertFalse(beigeSmallBed.isWall());
    assertEquals(bounds, beigeSmallBed.getBounds());
    assertEquals(StaticObjectType.BEIGE_SMALL_BED, beigeSmallBed.getType());

    BeigeSmallCouch beigeSmallCouch = new BeigeSmallCouch(bounds);
    assertFalse(beigeSmallCouch.isWall());
    assertEquals(bounds, beigeSmallCouch.getBounds());
    assertEquals(StaticObjectType.BEIGE_COUCH_SMALL, beigeSmallCouch.getType());

    BeigeWoodChairDown beigeWoodChairDown = new BeigeWoodChairDown(bounds);
    assertFalse(beigeWoodChairDown.isWall());
    assertEquals(bounds, beigeWoodChairDown.getBounds());
    assertEquals(StaticObjectType.BEIGE_CHAIR_WOOD_DOWN, beigeWoodChairDown.getType());

    BeigeWoodChairUp beigeWoodChairUp = new BeigeWoodChairUp(bounds);
    assertFalse(beigeWoodChairUp.isWall());
    assertEquals(bounds, beigeWoodChairUp.getBounds());
    assertEquals(StaticObjectType.BEIGE_CHAIR_WOOD_UP, beigeWoodChairUp.getType());

    DarkLongDrawer darkLongDrawer = new DarkLongDrawer(bounds);
    assertFalse(darkLongDrawer.isWall());
    assertEquals(bounds, darkLongDrawer.getBounds());
    assertEquals(StaticObjectType.DARK_DRAWER_LONG, darkLongDrawer.getType());

    DarkSmallDrawer darkSmallDrawer = new DarkSmallDrawer(bounds);
    assertFalse(darkSmallDrawer.isWall());
    assertEquals(bounds, darkSmallDrawer.getBounds());
    assertEquals(StaticObjectType.DARK_DRAWER_SMALL, darkSmallDrawer.getType());

    DarkSmallDrawerUp darkSmallDrawerUp = new DarkSmallDrawerUp(bounds);
    assertFalse(darkSmallDrawerUp.isWall());
    assertEquals(bounds, darkSmallDrawerUp.getBounds());
    assertEquals(StaticObjectType.DARK_DRAWER_SMALL_UP, darkSmallDrawerUp.getType());

    DarkWoodenTable darkWoodenTable = new DarkWoodenTable(bounds);
    assertFalse(darkWoodenTable.isWall());
    assertEquals(bounds, darkWoodenTable.getBounds());
    assertEquals(StaticObjectType.DARK_TABLE_ROUNDED, darkWoodenTable.getType());
    Rectangle2D.Double darktableexceptionbounds = new Rectangle2D.Double(0, 0, 10, 10);
    assertThrows(IllegalArgumentException.class, () -> new DarkWoodenTable(darktableexceptionbounds));

    DarkWoodenTableSmall darkWoodenTableSmall = new DarkWoodenTableSmall(bounds);
    assertFalse(darkWoodenTableSmall.isWall());
    assertEquals(bounds, darkWoodenTableSmall.getBounds());
    assertEquals(StaticObjectType.DARK_TABLE_SMALL, darkWoodenTableSmall.getType());

    DarkWoodenTableSquare darkWoodenTableSquare = new DarkWoodenTableSquare(bounds);
    assertFalse(darkWoodenTableSquare.isWall());
    assertEquals(bounds, darkWoodenTableSquare.getBounds());
    assertEquals(StaticObjectType.DARK_TABLE_SQUARE, darkWoodenTableSquare.getType());

    GreyTv greyTv = new GreyTv(bounds);
    assertFalse(greyTv.isWall());
    assertEquals(bounds, greyTv.getBounds());
    assertEquals(StaticObjectType.GREY_TV, greyTv.getType());

    GreyTvLeft greyTvLeft = new GreyTvLeft(bounds);
    assertFalse(greyTvLeft.isWall());
    assertEquals(bounds, greyTvLeft.getBounds());
    assertEquals(StaticObjectType.GREY_TV_LEFT, greyTvLeft.getType());

    PlantOne plantOne = new PlantOne(bounds);
    assertFalse(plantOne.isWall());
    assertEquals(bounds, plantOne.getBounds());
    assertEquals(StaticObjectType.PLANT_ONE, plantOne.getType());

    PlantTwo plantTwo = new PlantTwo(bounds);
    assertFalse(plantTwo.isWall());
    assertEquals(bounds, plantTwo.getBounds());
    assertEquals(StaticObjectType.PLANT_TWO, plantTwo.getType());

    RedChairLeft redChairLeft = new RedChairLeft(bounds);
    assertFalse(redChairLeft.isWall());
    assertEquals(bounds, redChairLeft.getBounds());
    assertEquals(StaticObjectType.RED_CHAIR_LEFT, redChairLeft.getType());

    RedChairRight redChairRight = new RedChairRight(bounds);
    assertFalse(redChairRight.isWall());
    assertEquals(bounds, redChairRight.getBounds());
    assertEquals(StaticObjectType.RED_CHAIR_RIGHT, redChairRight.getType());
}
}
