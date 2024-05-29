package net.qilla.shootergame.gunsystem.guncreation.gunmod;

public record AmmunitionMod(int gunCapacity, int gunMagazine, int reloadStages, int ticksPerStage) {

    /**
     * Default constructor for the AmmunitionMod
     */

    public AmmunitionMod() {
        this(128, 16, 5, 4);
    }

    /**
     * @param gunCapacity Bullet capacity of the gun excluding the magazine
     * @param gunMagazine Bullet capacity of the magazine
     * @param reloadStages Number of stages in the gun reload
     * @param ticksPerStage Number of ticks per stage of reload
     */

    public AmmunitionMod(int gunCapacity, int gunMagazine, int reloadStages, int ticksPerStage) {
        this.gunCapacity = gunCapacity;
        this.gunMagazine = gunMagazine;
        this.reloadStages = reloadStages;
        this.ticksPerStage = ticksPerStage;
    }

}
