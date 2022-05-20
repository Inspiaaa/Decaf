package game.sound;

// The SoundEffect class represents short sound effects that can be loaded fully into memory.
// This class is a descriptor for a sound effect, while each instance that actually plays is
// a SoundEffectInstance (which also lets you set up instance-specific fields like pan, and volume).
// Thanks to its design, you can play multiple of the same kind of sound effects at the same time.
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
