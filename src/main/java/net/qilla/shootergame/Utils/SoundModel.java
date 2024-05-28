package net.qilla.shootergame.Utils;

import org.bukkit.Sound;

public class SoundModel {

    private final float volume;
    private final float pitch;
    private final Sound sound;

    public SoundModel(float volume, float pitch, Sound sound) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public Sound getSound() {
        return sound;
    }
}
