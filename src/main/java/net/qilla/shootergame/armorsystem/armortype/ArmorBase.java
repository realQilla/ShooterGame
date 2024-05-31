package net.qilla.shootergame.armorsystem.armortype;

import net.qilla.shootergame.armorsystem.armormodel.PieceType;
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import org.bukkit.Material;

import java.util.List;

public abstract class ArmorBase {

    private final Material pieceMaterial;
    private final StatModel pieceStat;
    private final PieceType pieceType;
    private final String pieceName;
    private final List<String> pieceLore;

    ArmorBase(Material pieceMaterial, StatModel pieceStat, PieceType pieceType, String pieceName, List<String> pieceLore) {
        this.pieceMaterial = pieceMaterial;
        this.pieceStat = pieceStat;
        this.pieceType = pieceType;
        this.pieceName = pieceName;
        this.pieceLore = pieceLore;
    }

    public Material getPieceMaterial() {
        return pieceMaterial;
    }

    public StatModel getPieceStat() {
        return pieceStat;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public String getPieceName() {
        return pieceName;
    }

    public List<String> getPieceLore() {
        return pieceLore;
    }
}