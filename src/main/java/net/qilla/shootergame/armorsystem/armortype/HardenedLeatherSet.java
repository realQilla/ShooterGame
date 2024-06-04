package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.statsystem.stat.StatDefense;
import net.qilla.shootergame.statsystem.stat.StatMaxHealth;
import net.qilla.shootergame.statsystem.stat.StatBase;
import org.bukkit.Material;
import java.util.List;

public final class HardenedLeatherSet extends ArmorBase {

    private static final ArmorSet ARMOR_SET = ArmorSet.HARDENED_LEATHER_SET;

    private HardenedLeatherSet(final Material material, final StatBase[] pieceStatBases, final ArmorType type, final String name, final List<String> lore) {
        super(material, pieceStatBases, type, name, lore);
    }

    public static ArmorSet getSet() {
        return ARMOR_SET;
    }

    public static ArmorBase Helmet() {
        return new HardenedLeatherSet(Material.LEATHER_HELMET,
                new StatBase[]{
                        new StatMaxHealth(4),
                        new StatDefense(3)
                },
                ArmorType.HELMET,
                "<!italic><blue>Hardened Leather Helmet</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED HELMET</bold></blue>"));
    }

    public static ArmorBase Chestplate() {
        return new HardenedLeatherSet(Material.LEATHER_CHESTPLATE,
                new StatBase[]{
                        new StatMaxHealth(8),
                        new StatDefense(6)
                },
                ArmorType.CHEST_PLATE,
                "<!italic><blue>Hardened Leather Chestplate</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED CHESTPLATE</bold></blue>"));
    }

    public static ArmorBase Leggings() {
        return new HardenedLeatherSet(Material.LEATHER_LEGGINGS,
                new StatBase[]{
                        new StatMaxHealth(6),
                        new StatDefense(4)
                },
                ArmorType.LEGGINGS,
                "<!italic><blue>Hardened Leather Leggings</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED LEGGINGS</bold></blue>"));
    }

    public static ArmorBase Boots() {
        return new HardenedLeatherSet(Material.LEATHER_BOOTS,
                new StatBase[]{
                        new StatMaxHealth(2),
                        new StatDefense(2)
                },
                ArmorType.BOOTS,
                "<!italic><blue>Hardened Leather Boots</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED BOOTS</bold></blue>"));
    }
}