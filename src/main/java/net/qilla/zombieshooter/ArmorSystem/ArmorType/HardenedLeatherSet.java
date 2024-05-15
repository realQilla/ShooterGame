package net.qilla.zombieshooter.ArmorSystem.ArmorType;

import net.qilla.zombieshooter.ArmorSystem.ArmorID;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public class HardenedLeatherSet extends ArmorBase {

    private HardenedLeatherSet(Material material, StatModel statModel, ArmorID.ArmorPiece armorPiece, String armorName, List<String> armorLore) {
        super(material, statModel, armorPiece, armorName, armorLore);
    }

    public static ArmorBase Helmet() {
        return new HardenedLeatherSet(Material.LEATHER_HELMET,
                new StatModel(5, 5, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.HARDENED_LEATHER_SET, ArmorID.ArmorType.HELMET),
                "<!italic><blue>Hardened Leather Helmet</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED HELMET</bold></blue>"));
    }
    public static ArmorBase Chestplate() {
        return new HardenedLeatherSet(Material.LEATHER_CHESTPLATE,
                new StatModel(5, 9, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.HARDENED_LEATHER_SET, ArmorID.ArmorType.CHEST_PLATE),
                "<!italic><blue>Hardened Leather Chestplate</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED CHESTPLATE</bold></blue>"));
    }

    public static ArmorBase Leggings() {
        return new HardenedLeatherSet(Material.LEATHER_LEGGINGS,
                new StatModel(5, 7, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.HARDENED_LEATHER_SET, ArmorID.ArmorType.LEGGINGS),
                "<!italic><blue>Hardened Leather Leggings</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED LEGGINGS</bold></blue>"));
    }

    public static ArmorBase Boots() {
        return new HardenedLeatherSet(Material.LEATHER_BOOTS,
                new StatModel(5, 4, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.HARDENED_LEATHER_SET, ArmorID.ArmorType.BOOTS),
                "<!italic><blue>Hardened Leather Boots</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED BOOTS</bold></blue>"));
    }
}