package inspiaaa.decaf.sound;

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
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startStreamingData() {
        stopStreamingData();

        if (dataStreamingThread != null) {
            try {
                dataStreamingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isStreamingData = true;
        soundLine.start();
        dataStreamingThread = new Thread(this);
        dataStreamingThread.start();
    }

    private void stopStreamingData() {
        isStreamingData = false;
        soundLine.stop();
    }

    // Restarts the sound.
    public void play() {
        try {
            // Start the source data from the beginning again.
            if (audioInputStream != null) {
                audioInputStream.close();
            }
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // When the music was paused the last time and then restarted, the buffer may still have some content.
        // This flushes the buffer so that it will play from the beginning again.
        soundLine.flush();

        startStreamingData();
    }

    public void pause() {
        stopStreamingData();
    }

    public void resume() {
        startStreamingData();
    }

    public boolean isPlaying() {
        return soundLine.isRunning();
    }

    public void setVolume(float volume) {
        // See SoundEffectInstance for an explanation.
        FloatControl gainControl = (FloatControl) soundLine.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20 * (float)Math.log10(volume));
    }

    // TODO: Add pan
}
