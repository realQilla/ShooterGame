package net.qilla.shootergame.statsystem.statutil;

public class Formula {

    /**
     * Formula for calculating the players health to a value of 20.
     * @param health The players health after the event.
     * @param maxHealth The players max health.
     * @return Returns the resulting long.
     */

    public static long healthBar(long health, long maxHealth) {
        return (long) Math.ceil((double) health / maxHealth * 20);
    }

    /**
     * Formula for calculating incoming damage taking into account defense.
     * @param damage The incoming damage.
     * @param defense The defense of the entity at the damage event.
     * @return Returns the resulting long.
     */

    public static long defenseCalc(long damage, long defense) {
        return Math.round(damage * (1 - ((double) defense / (defense + 50))));
    }
}
