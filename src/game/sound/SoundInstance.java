package game.sound;

import javax.sound.sampled.*;
import java.io.File;

public class SoundInstance {
    private Clip clip;
    private float masterVolume;

    public SoundInstance(String filepath) {
        this(filepath, 1);
    }

    public SoundInstance(String filepath, float masterVolume) {
        this.masterVolume = masterVolume;

        try {
            clip = AudioSystem.getClip();
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filepath));
            clip.open(stream);

            // Closes the clip once it has finished playing to prevent a memory leak.
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setVolume(1);
    }

    public void play() {
        if (clip == null) return;

        clip.start();
    }

    public void repeat(int count) {
        clip.loop(count);
    }

    public void repeatForever() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) return;

        clip.stop();
    }

    public boolean isPlaying() {
        if (clip == null)
            return false;
        return clip.isRunning();
    }

    // Returns the length of the clip in seconds
    public float getDuration() {
        if (clip == null)
            return 0;

        return (float)clip.getMicrosecondLength() / 1000;
    }

    // volume = 0 => Silent
    // volume = 1 => Normal volume
    // volume = 2 => Twice as loud
    public void setVolume(float volume) {
        // https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        // MASTER_GAIN internally uses decibels for the volume, but a linear scale for the volume
        // is more practical (and intuitive for programmers). That's why
        // the 20 * log10 formula is needed for conversion. (20 appears to be a magic constant in the
        // definition of decibel).

        // Maths:
        // log10(0) => -infinity => silent
        // log10(1) => 0 => no change to the volume
        // log10(2) => >0 => louder

        gainControl.setValue(20 * (float)Math.log10(volume * masterVolume));
    }

    // Sets how far left / right the sound is heard from.
    // pan = -1 => Left
    // pan =  0 => Normal
    // pan =  1 => Right
    public void setPan(float pan) {
        FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
        panControl.setValue(pan);
    }
}
