package game.sound;

public class SoundEffect {
    private String filepath;
    private float masterVolume;

    public SoundEffect(String filepath) {
        this(filepath, 1);
    }

    public SoundEffect(String filepath, float masterVolume) {
        this.filepath = filepath;
        this.masterVolume = masterVolume;
    }

    public SoundEffectInstance play() {
        SoundEffectInstance instance = new SoundEffectInstance(filepath, masterVolume);
        instance.play();
        return instance;
    }

    public SoundEffectInstance repeat(int count) {
        SoundEffectInstance instance = new SoundEffectInstance(filepath, masterVolume);
        instance.repeat(count);
        return instance;
    }

    public SoundEffectInstance repeatForever() {
        SoundEffectInstance instance = new SoundEffectInstance(filepath, masterVolume);
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
