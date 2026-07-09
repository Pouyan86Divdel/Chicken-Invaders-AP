package sound;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private static Clip backgroundClip;
    private static boolean bgmEnabled = true;
    private static boolean sfxEnabled = true;

    public static void playBGM(String filepath) {
        if (!bgmEnabled) return;
        try {
            stopBGM();
            File soundFile = new File(filepath);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioIn);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
            }
        } catch (Exception e) {
            System.out.println("Error playing BGM: " + e.getMessage());
        }
    }

    public static void stopBGM() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    public static void playSFX(String filepath) {
        if (!sfxEnabled) return;
        try {
            File soundFile = new File(filepath);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error playing SFX: " + e.getMessage());
        }
    }

    public static boolean isBgmEnabled() { return bgmEnabled; }
    public static void setBgmEnabled(boolean enabled) {
        bgmEnabled = enabled;
        if (!enabled) stopBGM();
    }

    public static boolean isSfxEnabled() { return sfxEnabled; }
    public static void setSfxEnabled(boolean enabled) { sfxEnabled = enabled; }
}