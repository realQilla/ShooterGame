package net.qilla.zombieshooter.ArmorSystem.ArmorType;

import net.qilla.zombieshooter.ArmorSystem.ArmorID;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public class LeatherSet extends ArmorBase {

    private LeatherSet(Material material, StatModel statModel, ArmorID.ArmorPiece armorPiece, String armorName, List<String> armorLore) {
        super(material, statModel, armorPiece, armorName, armorLore);
    }

    public static ArmorBase Helmet() {
        return new LeatherSet(Material.LEATHER_HELMET,
                new StatModel(0, 3, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.LEATHER_SET, ArmorID.ArmorType.HELMET),
                "<!italic><yellow>Leather Helmet</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD HELMET</bold></yellow>"));
    }
    public static ArmorBase Chestplate() {
        return new LeatherSet(Material.LEATHER_CHESTPLATE,
                new StatModel(0, 7, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.LEATHER_SET, ArmorID.ArmorType.CHEST_PLATE),
                "<!italic><yellow>Leather Chestplate</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD CHESTPLATE</bold></yellow>"));
    }

    public static ArmorBase Leggings() {
        return new LeatherSet(Material.LEATHER_LEGGINGS,
                new StatModel(0, 5, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.LEATHER_SET, ArmorID.ArmorType.LEGGINGS),
                "<!italic><yellow>Leather Leggings</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD LEGGINGS</bold></yellow>"));
    }

    public static ArmorBase Boots() {
        return new LeatherSet(Material.LEATHER_BOOTS,
                new StatModel(0, 2, 0),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.LEATHER_SET, ArmorID.ArmorType.BOOTS),
                "<!italic><yellow>Leather Boots</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD BOOTS</bold></yellow>"));
    }
}