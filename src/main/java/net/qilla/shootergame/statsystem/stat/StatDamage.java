package net.qilla.shootergame.statsystem.stat;

public final class StatDamage extends StatBase {

    private static final long DEFAULT_VALUE = 0;
    private static final String ICON = "<dark_red>\uD83D\uDDE1</dark_red>";
    private static final String NAME = "<gray>Damage</gray>";
    private static final StatType TYPE = StatType.DAMAGE;

    public StatDamage(long value) {
        super(value, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public StatDamage() {
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