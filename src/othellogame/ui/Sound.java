package othellogame.ui;

import javafx.scene.media.*;
import javafx.util.*;

import java.io.*;

/**
 * Manages audio playback for different events in the Othello game application.
 * <p>
 * It uses JavaFX {@link MediaPlayer} to handle sound files.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public class Sound {
    /**
     * The {@code MediaPlayer} for the menu sound.
     */
    private MediaPlayer menuSound;

    /**
     * The {@code MediaPlayer} for the game sound.
     */
    private MediaPlayer gameSound;

    /**
     * The {@code MediaPlayer} for the exit sound.
     */
    private MediaPlayer exitSound;

    /**
     * A boolean indicating whether menu sound can be played.
     */
    public static boolean canPlayMenuSound = true;

    /**
     * A boolean indicating whether game sound can be played.
     */
    public static boolean canPlayGameSound = true;

    /**
     * Constructs a {@code Sound} object and initializes {@code MediaPlayer} instances with corresponding sound files.
     */
    public Sound() {
        this.menuSound = new MediaPlayer(new Media(new File("src/sound/Menu_Sound.mp3").toURI().toString()));
        this.gameSound = new MediaPlayer(new Media(new File("src/sound/Game_Sound.mp3").toURI().toString()));
        this.exitSound = new MediaPlayer(new Media(new File("src/sound/Exit_Sound.mp3").toURI().toString()));
    }

    /**
     * Plays the specified sound based on the given sound name.
     * @param soundName the name of the sound to play ("menu", "game", or "exit").
     */
    public void playSound(String soundName) {
        switch (soundName) {
            case "menu":
                menuSound.setVolume(10);
                playMenuSound(menuSound);
                menuSound.setOnEndOfMedia(() -> playMenuSound(menuSound));
                break;
            case "game":
                gameSound.setVolume(10);
                playGameSound(gameSound);
                gameSound.setOnEndOfMedia(() -> playGameSound(gameSound));
                break;
            case "exit":
                exitSound.setVolume(10);
                exitSound.seek(Duration.ZERO);
                exitSound.play();
                exitSound.setOnEndOfMedia(() -> {
                    exitSound.seek(Duration.ZERO);
                    exitSound.play();
                });
                break;
        }
    }

    /**
     * Stops the specified sound based on the given sound name.
     * @param soundName the name of the sound to stop ("menu", "game", or "exit").
     */
    public void stopSound(String soundName) {
        switch (soundName) {
            case "menu" -> menuSound.stop();
            case "game" -> gameSound.stop();
            case "exit" -> exitSound.stop();
        }
    }

    /**
     * Plays the menu sound if it's allowed to play.
     * @param sound the {@code MediaPlayer} for the menu sound.
     */
    private void playMenuSound(MediaPlayer sound) {
        if (canPlayMenuSound) {
            sound.seek(Duration.ZERO);
            sound.play();
        }
    }

    /**
     * Plays the game sound if it's allowed to play.
     * @param sound the {@code MediaPlayer} for the game sound.
     */
    private void playGameSound(MediaPlayer sound) {
        if (canPlayGameSound) {
            sound.seek(Duration.ZERO);
            sound.play();
        }
    }
}