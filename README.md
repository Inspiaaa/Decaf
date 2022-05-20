# Java Game Engine

A zero dependency, lightweight Java game engine for making 2D games inspired by Unity and Libgdx.

## Component-based architecture

Similarly to Unity, most objects (entities) in your game are represented by GameObjects. They themselves don't generally implement behaviour (such as rendering or collision handling), but they instead act as a container for components. Each feature / functionality an object should have is implemented in a separate component.

## Music and Sound

There are two types of sounds:

- **Sound effects** are short clips of sound that can be fully loaded into memory. E.g. gun shots, explosions, ... Multiple of them can also be played in parallel and they are represented by the `SoundEffect` class.

- **Music** is for longer clips of sound that are too large to be loaded into memory at once. E.g. music (40MB), long voice lines, ... The music data is then streamed from the disk into memory as it is needed. This type of sound is represented by the `Music` class.

### Using sound effects

At the beginning of the game, when you load all your assets, you can create a new `SoundEffect` instance, passing in a file path:

```java
SoundEffect gunShot = new SoundEffect("./assets/GunShot.wav");
```

If you want, you can customise the master volume:

```java
// 0 => Silent
// 1 => Normal volume
// 2 => Twice as loud
gunShot.setMasterVolume(1);
```

Later, when you want to play the sound effect, you can do the following:

```java
gunShot.play();
// Or
gunShot.repeat(5);
// Or
gunShot.repeatForever();
```

If you want to stop the playing instance, or customise the volume / pan (how far left / right the sound is perceived), you can use the returned `SoundEffectInstance`:

```java
SoundEffectInstance sfx = gunShot.play();
sfx.setVolume(0.5f);

// -1 => left
//  0 => middle
//  1 => right
sfx.setPan(-0.5f);
sfx.stop();
```

After you've stopped a sound effect from being played, it can not be restarted or resumed, as the internal resources are automatically cleaned up.

## Using music

Similarly to sound effects, you should only create a `Music` instance for each sound at the start of the game and then this instance should be reused.

```java
Music music = new Music("./assets/AmbientMusic.wav");
music.setVolume(0.5f);
```

```java
// Starts / Restarts the song
music.play();

music.pause();

// Continues playing
music.resume();
```


