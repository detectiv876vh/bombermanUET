package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.net.URL;

public class Sound {

    Clip clip; // Biến clip sẽ được sử dụng để chứa và điều khiển âm thanh (phát, lặp lại, dừng).
    URL soundURL[] = new URL[30]; // Tổng số sound thêm vào.
    FloatControl fc;    // Điều chỉnh âm lượng.
    int volumeScale = 3;
    float volume;   // min: -80f -> max: 6f.

    public Sound() {

        soundURL[0] = getClass().getResource("/sound/backgroundmusic.wav");
        soundURL[1] = getClass().getResource("/sound/pickup.wav");
        soundURL[2] = getClass().getResource("/sound/opendoor.wav");
        soundURL[3] = getClass().getResource("/sound/teleport.wav");
        soundURL[4] = getClass().getResource("/sound/buttonchange.wav");
        soundURL[5] = getClass().getResource("/sound/explosion.wav");
        soundURL[6] = getClass().getResource("/sound/placebomb.wav");
        soundURL[7] = getClass().getResource("/sound/gameover.wav");

    }

    public void setFile(int i) {

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]); // lấy dữ liệu âm thanh từ tệp tại soundURL[i]
            clip = AudioSystem.getClip(); // trả về một đối tượng Clip mới.
            clip.open(ais); // nạp dữ liệu âm thanh vào clip.
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);  // Truy xuất bộ điều khiển âm thanh của clip
                                                                                // và điểu chỉnh nó theo đơn vị dB
            if (i == 6) { // Nếu là placebomb.wav
                fc.setValue(6f); // Đặt âm lượng lớn nhất (6dB)
            } else {
                checkVolume(); // Các file khác dùng theo volumeScale bình thường
            }

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

    public void checkVolume() {

        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
