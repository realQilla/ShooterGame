package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.PieceType;
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public final class HardenedLeatherSet extends ArmorBase {

    private static final ArmorSet armorSet = ArmorSet.HARDENED_LEATHER_SET;

    private HardenedLeatherSet(Material pieceMaterial, StatModel pieceStat, PieceType pieceType, String pieceName, List<String> pieceLore) {
        super(pieceMaterial, pieceStat, pieceType, pieceName, pieceLore);
    }

    public static ArmorSet getSet() {
        return armorSet;
    }

    public static ArmorBase Helmet() {
        return new HardenedLeatherSet(Material.LEATHER_HELMET,
                new StatModel(5, 5, 0),
                PieceType.HELMET,
                "<!italic><blue>Hardened Leather Helmet</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED HELMET</bold></blue>"));
    }
    public static ArmorBase Chestplate() {
        return new HardenedLeatherSet(Material.LEATHER_CHESTPLATE,
                new StatModel(5, 9, 0),
                PieceType.CHEST_PLATE,
                "<!italic><blue>Hardened Leather Chestplate</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED CHESTPLATE</bold></blue>"));
    }

    public static ArmorBase Leggings() {
        return new HardenedLeatherSet(Material.LEATHER_LEGGINGS,
                new StatModel(5, 7, 0),
                PieceType.LEGGINGS,
                "<!italic><blue>Hardened Leather Leggings</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED LEGGINGS</bold></blue>"));
    }

    public static ArmorBase Boots() {
        return new HardenedLeatherSet(Material.LEATHER_BOOTS,
                new StatModel(5, 4, 0),
                PieceType.BOOTS,
                "<!italic><blue>Hardened Leather Boots</blue>",
                List.of("",
                        "<!italic><blue><bold>UPGRADED BOOTS</bold></blue>"));
    }
}