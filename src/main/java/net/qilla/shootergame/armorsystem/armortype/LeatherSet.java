package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.statsystem.stat.StatDefense;
import net.qilla.shootergame.statsystem.stat.StatMaxHealth;
import net.qilla.shootergame.statsystem.stat.StatBase;
import org.bukkit.Material;

import java.util.List;

public final class LeatherSet extends ArmorBase {

    private static final ArmorSet armorSet = ArmorSet.LEATHER_SET;

    private LeatherSet(final Material material, final StatBase[] pieceStatBases, final ArmorType type, final String name, final List<String> lore) {
        super(material, pieceStatBases, type, name, lore);
    }


    public static ArmorSet getSet() {
        return armorSet;
    }

    public static ArmorBase Helmet() {
        return new LeatherSet(Material.LEATHER_HELMET,
                new StatBase[]{
                        new StatMaxHealth(2),
                        new StatDefense(2)
                },
                ArmorType.HELMET,
                "<!italic><yellow>Leather Helmet</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD HELMET</bold></yellow>"));
    }

    public static ArmorBase Chestplate() {
        return new LeatherSet(Material.LEATHER_CHESTPLATE,
                new StatBase[]{
                        new StatMaxHealth(5),
                        new StatDefense(4)
                },
                ArmorType.CHEST_PLATE,
                "<!italic><yellow>Leather Chestplate</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD CHESTPLATE</bold></yellow>"));
    }

    public static ArmorBase Leggings() {
        return new LeatherSet(Material.LEATHER_LEGGINGS,
                new StatBase[]{
                        new StatMaxHealth(4),
                        new StatDefense(3)
                },
                ArmorType.LEGGINGS,
                "<!italic><yellow>Leather Leggings</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD LEGGINGS</bold></yellow>"));
    }

    public static ArmorBase Boots() {
        return new LeatherSet(Material.LEATHER_BOOTS,
                new StatBase[]{
                        new StatMaxHealth(2),
                        new StatDefense(1)
                },
                ArmorType.BOOTS,
                "<!italic><yellow>Leather Boots</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD BOOTS</bold></yellow>"));
    }
}