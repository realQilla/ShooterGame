package net.qilla.shootergame.statsystem.stat;

public final class StatHealth extends StatBase {

    private static final long DEFAULT_VALUE = StatMaxHealth.defaultValue();
    private static final String ICON = "<red>♥</red>";
    private static final String NAME = "<gray>Health</gray>";
    private static final StatType TYPE = StatType.HEALTH;

    public StatHealth(long value) {
        super(value, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public StatHealth() {
        super(DEFAULT_VALUE, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public static long defaultValue() {
        return DEFAULT_VALUE;
    }

    public static String icon() {
        return ICON;
    }

    public static String name() {
        return NAME;
    }

    public static StatType type() {
        return TYPE;
    }
}