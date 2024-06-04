package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.statsystem.stat.StatBase;
import org.bukkit.Material;

import java.util.List;

public abstract class ArmorBase {

    private final Material material;
    private final StatBase[] stats;
    private final ArmorType type;
    private final String name;
    private final List<String> lore;

    ArmorBase(Material material, StatBase[] stats, ArmorType type, String name, List<String> lore) {
        this.material = material;
        this.stats = stats;
        this.type = type;
        this.name = name;
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public StatBase[] getStats() {
        return stats;
    }

    public ArmorType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}