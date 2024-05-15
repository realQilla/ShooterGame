package net.qilla.zombieshooter.ArmorSystem;

public record ArmorID(ArmorPiece armorPiece) {

    public record ArmorPiece(ArmorSet armorSet, ArmorType armorType) {
    }

    public enum ArmorSet {
        LEATHER_SET,
        HARDENED_LEATHER_SET,
        TITAN;
    }

    public enum ArmorType {
        HELMET,
        CHEST_PLATE,
        LEGGINGS,
        BOOTS;
    }
}
