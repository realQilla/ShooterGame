package net.qilla.shootergame.ArmorSystem.ArmorType;

import net.qilla.shootergame.ArmorSystem.ArmorID;
import net.qilla.shootergame.StatSystem.StatManagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public abstract class ArmorBase {

    private final Material armorMaterial;
    private final StatModel statModel;
    private final ArmorID.ArmorPiece armorPiece;
    private final String armorName;
    private final List<String> armorLore;

    ArmorBase(Material armorMaterial, StatModel statModel, ArmorID.ArmorPiece armorPiece, String armorName, List<String> armorLore) {
        this.armorMaterial = armorMaterial;
        this.statModel = statModel;
        this.armorPiece = armorPiece;
        this.armorName = armorName;
        this.armorLore = armorLore;
    }

    public Material getArmorMaterial() {
        return armorMaterial;
    }

    public StatModel getStatModel() {
        return statModel;
    }

    public ArmorID.ArmorPiece getArmorSet() {
        return armorPiece;
    }

    public String getArmorName() {
        return armorName;
    }

    public List<String> getArmorLore() {
        return armorLore;
    }
}