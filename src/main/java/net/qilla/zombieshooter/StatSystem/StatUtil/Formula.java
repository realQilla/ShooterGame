package net.qilla.zombieshooter.StatSystem.StatUtil;

public class Formula {

    public static long healthBar(long healthAfter, long maxHealth) {
        return (long) Math.ceil((double) healthAfter / maxHealth * 20);
    }

    public static long defenseCalc(long damage, long defense) {
        return Math.round(damage * (1 - ((double) defense / (defense + 50))));
    }
}
