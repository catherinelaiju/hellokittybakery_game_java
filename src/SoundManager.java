import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * SoundManager
 * 
 * Simply plays short sound effects.
 * 
 * HOW TO USE AUDIO IN JAVA:
 * 1. AudioInputStream: Reads the file.
 * 2. Clip: A small memory buffer that plays the sound.
 */
public class SoundManager {
    
    public static void playSound(String fileName) {
        try {
            // 1. Get the file (Assumes sounds are in src/sounds/)
            File soundFile = new File("src/sounds/" + fileName);
            
            if (soundFile.exists()) {
                // 2. Open Stream
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                
                // 3. Get a Clip
                Clip clip = AudioSystem.getClip();
                
                // 4. Open audio clip and load samples from the stream
                clip.open(audioIn);
                
                // 5. Play
                clip.start();
            } else {
                // Silent fail or debug message
                // System.out.println("Sound file not found: " + fileName);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
