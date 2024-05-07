package net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod;

public record AmmunitionMod(int ammoCapacity, int gunMagazine, int reloadStages, int ticksPerStage) {

    /**
     * Default constructor for the AmmunitionMod
     */

    public AmmunitionMod() {
        this(128, 16, 5, 4);
    }

    /**
     * @param ammoCapacity Bullet capacity of the gun excluding the magazine
     * @param gunMagazine Bullet capacity of the magazine
     * @param reloadStages Number of stages in the gun reload
     * @param ticksPerStage Number of ticks per stage of reload
     */

    public AmmunitionMod(int ammoCapacity, int gunMagazine, int reloadStages, int ticksPerStage) {
        this.ammoCapacity = ammoCapacity;
        this.gunMagazine = gunMagazine;
        this.reloadStages = reloadStages;
        this.ticksPerStage = ticksPerStage;
    }

}
