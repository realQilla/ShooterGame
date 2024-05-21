package net.qilla.zombieshooter.StatSystem.StatUtil;

public class Formula {

    /**
     * Simple formula for calculating the player's health
     *
     * @param healthAfter The health after the player took damage
     * @param maxHealth The players overall max health
     * @return
     */

    public static long healthBar(long healthAfter, long maxHealth) {
        return (long) Math.ceil((double) healthAfter / maxHealth * 20);
    }

    public static long defenseCalc(long damage, long defense) {
        return Math.round(damage * (1 - ((double) defense / (defense + 50))));
    }
}
