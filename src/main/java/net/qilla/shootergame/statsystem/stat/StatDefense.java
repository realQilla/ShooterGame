package net.qilla.shootergame.statsystem.stat;

public final class StatDefense extends StatBase {

    private static final long DEFAULT_VALUE = 0;
    private static final String ICON = "<dark_gray>\uD83D\uDEE1</dark_gray>";
    private static final String NAME = "<gray>Defense</gray>";
    private static final StatType TYPE = StatType.DEFENSE;

    public StatDefense(long value) {
        super(value, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public StatDefense() {
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