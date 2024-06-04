package net.qilla.shootergame.statsystem.stat;

public final class StatMaxHealth extends StatBase {

    private static final long DEFAULT_VALUE = 25;
    private static final String ICON = "<red>♥</red>";
    private static final String NAME = "<gray>Max Health</gray>";
    private static final StatType TYPE = StatType.MAX_HEALTH;

    public StatMaxHealth(long value) {
        super(value, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public StatMaxHealth() {
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