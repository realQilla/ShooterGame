package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.PieceType;
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public final class TitanSet extends ArmorBase {

    private static final ArmorSet armorSet = ArmorSet.TITAN_SET;

    private TitanSet(Material pieceMaterial, StatModel pieceStat, PieceType pieceType, String pieceName, List<String> pieceLore) {
        super(pieceMaterial, pieceStat, pieceType, pieceName, pieceLore);
    }

    public static ArmorSet getSet() {
        return armorSet;
    }

    public static ArmorBase Helmet() {
        return new TitanSet(Material.IRON_HELMET,
                new StatModel(100, 90, 0),
                PieceType.HELMET,
                "<!italic><gold>Titan Helmet</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED HELMET</bold></gold>"));
    }
    public static ArmorBase Chestplate() {
        return new TitanSet(Material.IRON_CHESTPLATE,
                new StatModel(200, 150, 0),
                PieceType.CHEST_PLATE,
                "<!italic><gold>Titan Chestplate</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED CHESTPLATE</bold></gold>"));
    }

    public static ArmorBase Leggings() {
        return new TitanSet(Material.IRON_LEGGINGS,
                new StatModel(150, 110, 0),
                PieceType.LEGGINGS,
                "<!italic><gold>Titan Leggings</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED LEGGINGS</bold></gold>"));
    }

    public static ArmorBase Boots() {
        return new TitanSet(Material.IRON_BOOTS,
                new StatModel(75, 80, 0),
                PieceType.BOOTS,
                "<!italic><gold>Titan Boots</gold>",
                List.of("",
                        "<!italic><gold><bold>ENCHANTED BOOTS</bold></gold>"));
    }
}