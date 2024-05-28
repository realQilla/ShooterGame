package net.qilla.shootergame.StatSystem.HealthCalculation;

public class HealthDifference {

    private final long health;
    private final long amount;

    public HealthDifference(long currentHealth, long amount) {
        this.health = currentHealth;
        this.amount = amount;
    }

    public long getHealth() {
        return health;
    }

    public long getAmount() {
        return amount;
    }
}
