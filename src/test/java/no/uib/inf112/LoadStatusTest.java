package no.uib.inf112;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.uib.inf112.view.LoadStatus;

class LoadStatusTest {
    

    private LoadStatus status;

    @BeforeEach
    void setUp(){
        this.status = new LoadStatus();
    }

    @Test
    void loadStatusTest(){

        assertEquals("Initiallizing...", status.getStatus());
        assertEquals("0%", status.percentComplete());

        status.setStatus("foo", 5);

        assertEquals("foo", status.getStatus());
        assertEquals("5%", status.percentComplete());

        status.setStatus(null, 5);
        
        assertEquals("foo", status.getStatus());
        assertEquals("5%", status.percentComplete());

        status.setStatus("bar", -1);
        
        assertEquals("foo", status.getStatus());
        assertEquals("5%", status.percentComplete());

        status.setStatus("bar", 10);
        
        assertEquals("bar", status.getStatus());
        assertEquals("10%", status.percentComplete());
    }
}
