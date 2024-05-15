package net.qilla.zombieshooter.ArmorSystem.ArmorType;

import net.qilla.zombieshooter.ArmorSystem.ArmorID;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public class TitanSet extends ArmorBase {

    private TitanSet(Material material, StatModel statModel, ArmorID.ArmorPiece armorPiece, String armorName, List<String> armorLore) {
        super(material, statModel, armorPiece, armorName, armorLore);
    }

    public static ArmorBase Helmet() {
        return new TitanSet(Material.IRON_HELMET,
                new StatModel(100, 90, 3),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.TITAN, ArmorID.ArmorType.HELMET),
                "<!italic><gold>Titan Helmet</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED HELMET</bold></gold>"));
    }
    public static ArmorBase Chestplate() {
        return new TitanSet(Material.IRON_CHESTPLATE,
                new StatModel(200, 150, 8),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.TITAN, ArmorID.ArmorType.CHEST_PLATE),
                "<!italic><gold>Titan Chestplate</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED CHESTPLATE</bold></gold>"));
    }

    public static ArmorBase Leggings() {
        return new TitanSet(Material.IRON_LEGGINGS,
                new StatModel(150, 110, 4),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.TITAN, ArmorID.ArmorType.LEGGINGS),
                "<!italic><gold>Titan Leggings</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED LEGGINGS</bold></gold>"));
    }

    public static ArmorBase Boots() {
        return new TitanSet(Material.IRON_BOOTS,
                new StatModel(75, 80, 2),
                new ArmorID.ArmorPiece(ArmorID.ArmorSet.TITAN, ArmorID.ArmorType.BOOTS),
                "<!italic><gold>Titan Boots</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED BOOTS</bold></gold>"));
    }
}