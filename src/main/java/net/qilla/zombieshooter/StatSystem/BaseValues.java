package net.qilla.zombieshooter.StatSystem;

public enum BaseValues {
    MAX_HEARTS(20),

    BASE_DAMAGE(1),
    BASE_HEALTH(25),
    BASE_REGENERATION(1),
    BASE_DEFENSE(0);

    private final long value;

    private BaseValues(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
