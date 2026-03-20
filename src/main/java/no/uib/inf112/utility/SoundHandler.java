package no.uib.inf112.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.enums.GunType;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class SoundHandler {

    private HashMap<GameState, String> gameMusic;
    private HashMap<GunType, String> gunSounds;
    private HashMap<BuffType, String> buffMusic;
    private HashMap<BuffType, String> buffSounds;

    private HashMap<EnemyAction, HashMap<EnemyType, String>> enemySounds;

    private ArrayList<String> damageSounds;

    // Clips
    private ArrayList<Clip> clips;
    private int counter;
    private static final int NUM_CLIPS = 40;

    private Clip currentMusic;
    private Clip currentBuffMusic;

    public SoundHandler() {
        loadMusic();
        loadEnemySounds();
        loadGunSounds();
        loadBuffMusic();
        loadDamageSounds();
        loadBuffSounds();

        // Clips
        this.counter = 0;
        loadClips();
    }

    private void loadDamageSounds() {
        this.damageSounds = new ArrayList<>();
        this.damageSounds.add("/no/uib/inf112/sound/playersounds/damaged.wav");
        this.damageSounds.add("/no/uib/inf112/sound/playersounds/death.wav");
    }

    private void loadBuffMusic() {
        this.buffMusic = new HashMap<>();
        this.buffMusic.put(BuffType.RAINBOW, "/no/uib/inf112/sound/buffs/rainbowBuff.wav");
        this.buffMusic.put(BuffType.DAMAGE, "/no/uib/inf112/sound/buffs/damageBuff.wav");

    }

    private void loadBuffSounds(){
        this.buffSounds = new HashMap<>();
        this.buffSounds.put(BuffType.ARMOR, "/no/uib/inf112/sound/buffs/armorSound.wav");
        this.buffSounds.put(BuffType.HEALTH, "/no/uib/inf112/sound/buffs/hpSound.wav");
        this.buffSounds.put(BuffType.AMMO, "/no/uib/inf112/sound/buffs/ammoPickupSound.wav");
    }

    private void loadGunSounds() {
        this.gunSounds = new HashMap<>();
        this.gunSounds.put(GunType.DEAGLE, "/no/uib/inf112/sound/guns/deagle.wav");
        this.gunSounds.put(GunType.MP5, "/no/uib/inf112/sound/guns/mp5.wav");
        this.gunSounds.put(GunType.SHOTGUN, "/no/uib/inf112/sound/guns/shotgun.wav");
    }

    private void loadMusic() {
        this.gameMusic = new HashMap<>();
        this.gameMusic.put(GameState.MAIN_MENU, "/no/uib/inf112/sound/music/mainMenuSong.wav");
        this.gameMusic.put(GameState.ACTIVE_GAME, "/no/uib/inf112/sound/music/activeGameSong.wav");
    }

    private void loadClips() {
        this.clips = new ArrayList<>();
        for (int i = 0; i < NUM_CLIPS; i++) {
            try {
                this.clips.add(AudioSystem.getClip());
            } catch (LineUnavailableException e) {
                throw new IllegalStateException("Could not load clip");
            }
        }
    }

    private void increment() {
        this.counter = (this.counter + 1) % NUM_CLIPS;
    }

    private Clip playClip(AudioInputStream stream) {
        Clip clip = this.clips.get(this.counter);
        while (clip.isActive() || clip == this.currentMusic) {
            increment();
            clip = this.clips.get(this.counter);
        }
        clip.close();

        try {
            clip.open(stream);
        } catch (LineUnavailableException | IOException e) {
            throw new IllegalAccessError("Could not access stream");
        }
        clip.start();
        increment();
        return clip;
    }

    private void loadEnemySounds() {
        this.enemySounds = new HashMap<>();
    }

    /**
     * Plays music based on given gamestate
     * @param state
     */
    public void playMusic(GameState state) {
        if (this.currentMusic != null) {
            this.currentMusic.stop();
            this.currentMusic.close();
        }

        String path = this.gameMusic.get(state);
        if (path == null) {
            return;
        }

        AudioInputStream stream = SoundReader.loadSound(path);
        if (stream != null) {
            this.currentMusic = playClip(stream);
            this.currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Plays a gunshot sound based on gun type
     * @param type
     */
    public void playGunShot(GunType type) {
        String path = this.gunSounds.get(type);
        if (path == null) {
            return;
        }

        AudioInputStream stream = SoundReader.loadSound(path);
        if (stream != null) {
            playClip(stream);
        }
    }

    /**
     * Plays music/sound based on given buff type
     * @param type
     */
    public void playBuffMusic(BuffType type) {
        if (this.currentMusic != null){ this.currentMusic.stop();}
        if (this.currentBuffMusic != null) {
            this.currentBuffMusic.stop();
        }
        this.currentBuffMusic = playClip(SoundReader.loadSound(this.buffMusic.get(type)));
    }

    /**
     * Resumes music after pause
     */
    public void playBuffSound(BuffType type) {
        String path = this.buffSounds.get(type);
        if (path == null) {
            System.out.println("No buff found");
            return;
        }

        AudioInputStream stream = SoundReader.loadSound(path);
        if (stream != null) {
            playClip(stream);
        }
    }

    public void resumeMusic() {
        this.currentBuffMusic.stop();
        this.currentMusic.start();
    }

    /**
     * Plays damaged/death sound (1: damaged, 2: dead)
     * @param index
     */
    public void playPlayerDamageSound(int index){
        playClip(SoundReader.loadSound(this.damageSounds.get(index)));
    }
}
