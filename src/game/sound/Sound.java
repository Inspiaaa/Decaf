package game.sound;

public class Sound {
    private String filepath;
    private float masterVolume;

    public Sound(String filepath) {
        this(filepath, 1);
    }

    public Sound(String filepath, float masterVolume) {
        this.filepath = filepath;
        this.masterVolume = masterVolume;
    }

    public SoundInstance play() {
        SoundInstance instance = new SoundInstance(filepath, masterVolume);
        instance.play();
        return instance;
    }

    public SoundInstance repeat(int count) {
        SoundInstance instance = new SoundInstance(filepath, masterVolume);
        instance.repeat(count);
        return instance;
    }

    public SoundInstance repeatForever() {
        SoundInstance instance = new SoundInstance(filepath, masterVolume);
        instance.repeatForever();
        return instance;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
    }

    public float getMasterVolume() {
        return masterVolume;
    }
}
