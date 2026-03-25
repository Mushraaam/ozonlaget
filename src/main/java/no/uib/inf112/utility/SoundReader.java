package no.uib.inf112.utility;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class SoundReader {

    private SoundReader() {
        /* This utility class should not be instantiated */
    }

    static AudioInputStream loadSound(String url) {

        URL path = SoundReader.class.getResource(url);
        try {
            if (path != null) {
                return AudioSystem.getAudioInputStream(path);
            } else {
                throw new IOException("Clip path not found.");
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            return null;
        }
    }
}
