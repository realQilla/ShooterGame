package net.qilla.zombieshooter.StatSystem.StatManagement;

public class StatModel {

    private long maxHealth;
    private long defence;
    private long regeneration;

    public StatModel(long maxHealth, long defence, long regeneration) {
        this.maxHealth = maxHealth;
        this.defence = defence;
        this.regeneration = regeneration;
    }

    public long getMaxHealth() {
        return maxHealth;
    }

    public long getDefence() {
        return defence;
    }

    public void setMaxHealth() {
        this.maxHealth = defence;
    }

    public void setDefence(long playerDefence) {
        this.defence = playerDefence;
    }
}
