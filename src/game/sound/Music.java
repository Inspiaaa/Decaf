package game.sound;

import javax.sound.sampled.*;
import java.io.File;

// The Music class represents sound data that is so large that it should
// be streamed from the disk instead of loading all the data upfront.
public class Music implements Runnable {
    private final int BUFFER_SIZE = 64 * 1024;

    private String filepath;
    private SourceDataLine soundLine;
    private AudioInputStream audioInputStream;

    private volatile boolean isStreamingData;
    private Thread dataStreamingThread;

    public Music(String filepath) {
        this.filepath = filepath;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            soundLine = (SourceDataLine) AudioSystem.getLine(info);
            soundLine.open(audioFormat);

            audioInputStream.mark(Integer.MAX_VALUE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int numBytesRead = 0;
        byte[] sampledData = new byte[BUFFER_SIZE];

        try {
            while (numBytesRead != -1 && isStreamingData) {
                numBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
                if (numBytesRead >= 0) {
                    soundLine.write(sampledData, 0, numBytesRead);
                }
                System.out.println(numBytesRead);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startStreamingData() {
        stopStreamingData();

        isStreamingData = true;
        dataStreamingThread = new Thread(this);
        dataStreamingThread.start();
    }

    private void stopStreamingData() {
        isStreamingData = false;

        if (dataStreamingThread != null) {
            try {
                dataStreamingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        try {
            audioInputStream.reset();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // When the music was paused the last time and then restarted, the buffer may still have some content.
        // This flushes the buffer so that it will play from the beginning again.
        soundLine.flush();
        soundLine.start();

        startStreamingData();
    }

    public void pause() {
        System.out.println("Pause");
        stopStreamingData();
        soundLine.stop();
    }

    public void resume() {
        System.out.println("Resume");
        soundLine.start();
        startStreamingData();
    }

    public boolean isPlaying() {
        return soundLine.isRunning();
    }

    public void setVolume(float volume) {
        // TODO
    }
}
