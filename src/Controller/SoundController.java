package Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundController {
    private Map<String, MediaPlayer> sounds;
    private MediaPlayer backgroundMusic;

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private double soundVolume = 0.7;

    public SoundController() {
        sounds = new HashMap<String, MediaPlayer>();
        initializeSounds();
    }

    public static SoundController getInstance() {
        return new SoundController();
    }

    private void initializeSounds() {
        try {
            URL jumpSource = getClass().getResource("/Assets/sounds/hop.m4a");
            if (jumpSource != null) {
                sounds.put("jump", new MediaPlayer(new Media(jumpSource.toString())));
            } else {
                System.err.println("Jump sound file not found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading sound files: " + e.getMessage());
        }
    }

    public void playSound(String soundName) {
         if (!soundEnabled) return;

        MediaPlayer player = sounds.get(soundName);
        if (player != null) {
            player.stop();
            player.play();
        }
    }
}
