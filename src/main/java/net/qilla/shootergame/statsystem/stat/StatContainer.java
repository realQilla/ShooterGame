package net.qilla.shootergame.statsystem.stat;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;

/**
 * Container for anything that has player stats.
 */

public final class StatContainer {

    private final EnumMap<StatType, StatBase> container = new EnumMap<>(StatType.class);

    /**
     * Constructs an empty container.
     */

    public StatContainer() {
        System.out.println("StatContainer created");
    }

    /**
     * Constructs a container with all listed stats registered.
     * @param list A list of StatBase objects.
     */

    public StatContainer(List<StatBase> list) {
        list.forEach(stat -> this.container.put(stat.getType(), stat));
        System.out.println("StatContainer created");
    }

    /**
     * Grabs the active instance of a stat within container.
     * @param type The stat type to return; the enum id.
     * @return Returns the specified StatBase.
     */

    public StatBase getStat(StatType type) {
        return this.container.get(type);
    }

    /**
     * Grabs all registered stats within the container.
     * @return Returns an EnumMap.
     */

    public EnumMap<StatType, StatBase> getContainer() {
        return this.container;
    }

    /**
     * Adds a value to a specified stat in the container.
     * @param type  type The stat type to modify; the stat enum.
     * @param value  value The amount to add.
     */

    public void addValue(@NotNull final StatType type, final long value) {
        this.container.computeIfPresent(type, (k, v) -> v.setValue(v.getValue() + value));
    }

    /**
     * Subtracts a value from a specified stat in the container.
     * @param type The stat type to modify; the stat enum.
     * @param value The amount to subtract.
     */

    public void subtractValue(@NotNull final StatType type, final long value) {
        this.container.computeIfPresent(type, (k, v) -> v.setValue(v.getValue() - value));
    }

    /**
     * Sets a value for specified stat in the container.
     * @param type The stat type to modify; the stat enum.
     * @param value The amount to set the value to.
     */

    public void setValue(@NotNull final StatType type, final long value) {
        this.container.computeIfPresent(type, (k, v) -> v.setValue(value));
    }

    /**
     * Resets a value for a specified stat in the container.
     * @param type The stat type to modify; the enum id.
     */

    public StatContainer resetStat(StatType type) {
        this.container.computeIfAbsent(type, k -> container.get(type)).resetValue();
        return this;
    }

    /**
     * Resets all stats in the container
     */

    public StatContainer resetStats() {
        this.container.forEach((k, v) -> v.resetValue());
        return this;
    }

    /**
     * Registers a specified stat to the container.
     * @param type The stat type to add; the enum id.
     * @param base The stat object to add.
     */

    public StatContainer register(StatType type, StatBase base) {
        this.container.put(type, base);
        return this;
    }

    /**
     * Unregisters a stat from the container.
     * @param type The stat type to remove; the enum id.
     */

    public StatContainer unregister(final StatType type) {
        this.container.remove(type);
        return this;
    }

    /**
     * Returns whether a stat is registered in the container.
     * @param type The stat type to search for; the enum id.
     * @return Returns a boolean.
     */

    public boolean isRegistered(StatType type) {
        return this.container.containsKey(type);
    }

    /**
     * Registers all existing stats(with default values) to the container.
     */

    public StatContainer registerAll() {
        register(StatType.MAX_HEALTH, new StatMaxHealth());
        register(StatType.HEALTH, new StatHealth());
        register(StatType.DEFENSE, new StatDefense());
        register(StatType.DAMAGE, new StatDamage());
        register(StatType.REGENERATION, new StatRegeneration());
        return this;
    }
}
