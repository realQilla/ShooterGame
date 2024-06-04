package net.qilla.shootergame.statsystem.stat;

public abstract class StatBase {

    private long value;
    private final long defaultValue;
    private final String icon;
    private final String name;
    private final StatType type;

    public StatBase(long value, long defaultValue, String icon, String name, StatType type) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    /**
     * Sets a stat value
     * @param value A long value to take the value's place.
     * @return Returns the object instance post-modification.
     */

    public StatBase setValue(long value) {
        this.value = value;
        return this;
    }

    /**
     * Resets a stat value to it's set default.
     * @return Returns the object instance post-modification.
     */

    public StatBase resetValue() {
        this.value = defaultValue;
        return this;
    }

    /**
     * Returns the current stat value.
     * @return Returns a long.
     */

    public long getValue() {
        return value;
    }

    /**
     * Returns the default value for the stat.
     * @return Returns a long.
     */

    public long getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the icon for the stat.
     * @return Returns a string.
     */

    public final String getIcon() {
        return icon;
    }

    /**
     * Returns the name of the stat, color included.
     * @return Returns a string.
     */

    public final String getName() {
        return name;
    }

    /**
     * Returns the type that the stat is associated with.
     * @return Returns a StatType enum.
     */

    public final StatType getType() {
        return type;
    }
}