package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30]; // Tổng số sound thêm vào.

    public Sound() {

        soundURL[0] = getClass().getResource("/sound/backgroundmusic.wav");
        soundURL[1] = getClass().getResource("/sound/pickup.mp3");
        soundURL[2] = getClass().getResource("/sound/opendoor.mp3");
        soundURL[3] = getClass().getResource("/sound/teleport.wav");
    }

    public void setFile(int i) {

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
        }

    }

    public void play() {

        clip.start();
    }

    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {

        clip.stop();
    }
}
