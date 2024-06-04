package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.statsystem.stat.StatDefense;
import net.qilla.shootergame.statsystem.stat.StatMaxHealth;
import net.qilla.shootergame.statsystem.stat.StatBase;
import org.bukkit.Material;

import java.util.List;

public final class TitanSet extends ArmorBase {

    private static final ArmorSet armorSet = ArmorSet.TITAN_SET;

    private TitanSet(final Material material, final StatBase[] pieceStatBases, final ArmorType type, final String name, final List<String> lore) {
        super(material, pieceStatBases, type, name, lore);
    }

    public static ArmorSet getSet() {
        return armorSet;
    }

    public static ArmorBase Helmet() {
        return new TitanSet(Material.IRON_HELMET,
                new StatBase[]{
                        new StatMaxHealth(250),
                        new StatDefense(125)
                },
                ArmorType.HELMET,
                "<!italic><gold>Titan Helmet</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED HELMET</bold></gold>"));
    }

    public static ArmorBase Chestplate() {
        return new TitanSet(Material.IRON_CHESTPLATE,
                new StatBase[]{
                        new StatMaxHealth(500),
                        new StatDefense(325)
                },
                ArmorType.CHEST_PLATE,
                "<!italic><gold>Titan Chestplate</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED CHESTPLATE</bold></gold>"));
    }

    public static ArmorBase Leggings() {
        return new TitanSet(Material.IRON_LEGGINGS,
                new StatBase[]{
                        new StatMaxHealth(425),
                        new StatDefense(275)
                },
                ArmorType.LEGGINGS,
                "<!italic><gold>Titan Leggings</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED LEGGINGS</bold></gold>"));
    }

    public static ArmorBase Boots() {
        return new TitanSet(Material.IRON_BOOTS,
                new StatBase[]{
                        new StatMaxHealth(200),
                        new StatDefense(100)
                },
                ArmorType.BOOTS,
                "<!italic><gold>Titan Boots</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED BOOTS</bold></gold>"));
    }
}