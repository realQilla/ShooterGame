package net.qilla.shootergame.statsystem.stat;

public final class StatRegeneration extends StatBase {

    private static final long DEFAULT_VALUE = 1;
    private static final String ICON = "<red>\uD83E\uDDEA</red>";
    private static final String NAME = "<gray>Regeneration</gray>";
    private static final StatType TYPE = StatType.DAMAGE;

    public StatRegeneration(long value) {
        super(value, DEFAULT_VALUE, ICON, NAME, TYPE);
    }

    public StatRegeneration() {
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