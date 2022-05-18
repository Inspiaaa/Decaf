package game.sound;

import javax.sound.sampled.*;
import java.io.File;

public class Music implements Runnable {
    private final int BUFFER_SIZE = 64 * 1024;

    private String filepath;
    private SourceDataLine soundLine;
    private AudioInputStream audioInputStream;

    private volatile boolean isPlaying;

    public Music(String filepath) {
        this.filepath = filepath;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            soundLine = (SourceDataLine) AudioSystem.getLine(info);
            soundLine.open(audioFormat);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        soundLine.start();

        int numBytesRead = 0;
        byte[] sampledData = new byte[BUFFER_SIZE];

        try {
            while (numBytesRead != -1 && isPlaying) {
                numBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
                if (numBytesRead >= 0) {
                    soundLine.write(sampledData, 0, numBytesRead);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        soundLine.drain();
        soundLine.close();
    }

    public void play() {
        // TODO: soundLine.flush()
        isPlaying = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void pause() {
        // isPlaying = false;
        soundLine.stop();
    }

    public void resume() {
        soundLine.start();
    }

    public void stop() {
        isPlaying = false;
        soundLine.stop();
    }

    public boolean isPlaying() {
        return soundLine.isRunning();
    }

    public void setVolume(float volume) {
        // TODO
    }
}
