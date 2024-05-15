package net.qilla.zombieshooter.StatSystem.StatManagement;

public class StatModel {

    private long maxHealth;
    private long defense;
    private long regeneration;

    public StatModel(long maxHealth, long defense, long regeneration) {
        this.maxHealth = maxHealth;
        this.defense = defense;
        this.regeneration = regeneration;
    }

    public long getMaxHealth() {
        return maxHealth;
    }

    public long getDefense() {
        return defense;
    }

    public long getRegeneration() {
        return regeneration;
    }

    public void setMaxHealth() {
        this.maxHealth = defense;
    }

    public void setDefense(long playerDefense) {
        this.defense = playerDefense;
    }

    public void setRegeneration(long playerRegeneration) {
        this.regeneration = playerRegeneration;
    }
}