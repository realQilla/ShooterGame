package net.qilla.shootergame.GunSystem.GunCreation.Mod;

import net.qilla.shootergame.Utils.SoundModel;
import org.bukkit.Particle;
import org.bukkit.Sound;

public record CosmeticMod(SoundModel changeFireMode, SoundModel reloadMagazine, SoundModel reloadMagazineEnd, SoundModel emptyMagazine, SoundModel emptyCapacity, SoundModel fireSound, SoundModel hitMarkerSound, Particle fireParticle) {


    /**
     * @param changeFireMode Changes the weapons fire mode
     * @param reloadMagazine Sound for each stage of the magazine reload
     * @param reloadMagazineEnd Sound for when the magazine finishes reloading
     * @param emptyMagazine Sound for when a magazine reload is attempted but is empty
     * @param emptyCapacity Sound for when all there are no more bullets to reload the magazine
     * @param fireSound Sound for when the gun is fired
     * @param fireParticle Particle trail for when the gun is fired
     */

    public CosmeticMod(SoundModel changeFireMode, SoundModel reloadMagazine, SoundModel reloadMagazineEnd, SoundModel emptyMagazine, SoundModel emptyCapacity, SoundModel fireSound, SoundModel hitMarkerSound, Particle fireParticle) {
        this.changeFireMode = changeFireMode;
        this.reloadMagazine = reloadMagazine;
        this.reloadMagazineEnd = reloadMagazineEnd;
        this.emptyMagazine = emptyMagazine;
        this.emptyCapacity = emptyCapacity;
        this.fireSound = fireSound;
        this.hitMarkerSound = hitMarkerSound;
        this.fireParticle = fireParticle;
    }

    /**
     * Default constructor for CosmeticMod
     *
     * @param fireSound Custom weapon fire sound
     * @param fireParticle Custom weapon particle
     */

    public CosmeticMod(SoundModel fireSound, Particle fireParticle) {
        this(new SoundModel(0.75f, 2.0f, Sound.BLOCK_LAVA_POP), //Change fire mode
                new SoundModel(0.75f, 2.0f, Sound.BLOCK_PISTON_EXTEND), //Reload magazine
                new SoundModel(1.0f, 1.0f, Sound.ENTITY_BEE_STING), //Reload magazine end
                new SoundModel(0.75f, 2.0f, Sound.ENTITY_BEE_STING), //Empty magazine
                new SoundModel(0.75f, 2.0f, Sound.ENTITY_SNOW_GOLEM_SHEAR), //Empty total capacity
                fireSound, // Fire sound
                new SoundModel(2.0f, 1.0f, Sound.BLOCK_NOTE_BLOCK_BIT), //Hit marker
                fireParticle // Fire particle
        );
    }
}