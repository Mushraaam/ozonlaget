package no.uib.inf112.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import no.uib.inf112.model.Model;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

class HelicopterTest {
    MockedConstruction<SoundHandler> mockedSoundHandler;
    Model model;
    Helicopter heli;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model(mock(LoadStatus.class));
        heli = (Helicopter) model.getVehicles().getFirst();
    }

    @AfterEach
    void tearDown() {
        if (mockedSoundHandler != null) {
            mockedSoundHandler.close();
        }
    }

    @Test
    void helicopterMoveTest() {
        

        Rectangle2D.Double bounds = heli.getBounds();

        for (int expansion = 1; expansion <= 60; expansion++) {
            bounds = new Rectangle2D.Double(
                    bounds.getX() - 0.05 * expansion,
                    bounds.getY() - 0.05 * expansion,
                    bounds.width + 0.1 * expansion,
                    bounds.height + 0.1 * expansion);

            if (expansion == 60) {
                bounds = new Rectangle2D.Double(
                        bounds.getX() + 6,
                        bounds.getY(),
                        bounds.getWidth(),
                        bounds.getHeight());
            }

            heli.increment();

            assertEquals(bounds, heli.getBounds());
        }

        for (int i = 0; i < 100; i++) {
            bounds = new Rectangle2D.Double(
                    bounds.getX() + 6,
                    bounds.getY(),
                    bounds.getWidth(),
                    bounds.getHeight());

            heli.increment();

            assertEquals(bounds, heli.getBounds());
        }
    }

    @Test
    void fuelTest(){
        for (int i = 0; i < 7; i++){
            if (i < 6){
                assertFalse(heli.isFuelFull());
            }
            else{
                assertTrue(heli.isFuelFull());
            }
            heli.depositGas();
        }
    }

    @Test
    void heliIndexTest(){
        for (int i = 0; i < 100; i++){
            int expectedIndex = i % 7;
            assertEquals(expectedIndex, heli.getIndex());
            heli.increment();
        }
    }
}
