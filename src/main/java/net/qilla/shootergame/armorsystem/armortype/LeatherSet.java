package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.PieceType;
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public final class LeatherSet extends ArmorBase {

    private static final ArmorSet armorSet = ArmorSet.LEATHER_SET;

    private LeatherSet(Material pieceMaterial, StatModel pieceStat, PieceType pieceType, String pieceName, List<String> pieceLore) {
        super(pieceMaterial, pieceStat, pieceType, pieceName, pieceLore);
    }

    public static ArmorSet getSet() {
        return armorSet;
    }

    public static ArmorBase Helmet() {
        return new LeatherSet(Material.LEATHER_HELMET,
                new StatModel(0, 3, 0),
                PieceType.HELMET,
                "<!italic><yellow>Leather Helmet</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD HELMET</bold></yellow>"));
    }
    public static ArmorBase Chestplate() {
        return new LeatherSet(Material.LEATHER_CHESTPLATE,
                new StatModel(0, 7, 0),
                PieceType.CHEST_PLATE,
                "<!italic><yellow>Leather Chestplate</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD CHESTPLATE</bold></yellow>"));
    }

    public static ArmorBase Leggings() {
        return new LeatherSet(Material.LEATHER_LEGGINGS,
                new StatModel(0, 5, 0),
                PieceType.LEGGINGS,
                "<!italic><yellow>Leather Leggings</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD LEGGINGS</bold></yellow>"));
    }

    public static ArmorBase Boots() {
        return new LeatherSet(Material.LEATHER_BOOTS,
                new StatModel(0, 2, 0),
                PieceType.BOOTS,
                "<!italic><yellow>Leather Boots</yellow>",
                List.of("",
                        "<!italic><yellow><bold>STANDARD BOOTS</bold></yellow>"));
    }
}