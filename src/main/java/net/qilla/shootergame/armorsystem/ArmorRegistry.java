package net.qilla.shootergame.armorsystem;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import net.qilla.shootergame.armorsystem.armortype.HardenedLeatherSet;
import net.qilla.shootergame.armorsystem.armortype.LeatherSet;
import net.qilla.shootergame.armorsystem.armortype.TitanSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ArmorRegistry {

    private static ArmorRegistry instance;
    private final EnumMap<ArmorSet, EnumMap<ArmorType, ArmorBase>> armorRegistry = new EnumMap<>(ArmorSet.class);

    private ArmorRegistry() {
        registerAll();
    }

    public static ArmorRegistry getInstance() {
        if (instance == null) instance = new ArmorRegistry();
        return instance;
    }

    @NotNull
    public Map<ArmorSet, EnumMap<ArmorType, ArmorBase>> getRegistry() {
        return armorRegistry.clone();
    }

    @NotNull
    public EnumMap<ArmorType, ArmorBase> getSet(@NotNull ArmorSet armorSet) {
        if(armorRegistry.get(armorSet) == null) throw new NullPointerException("Non-registered armor set: " + armorSet + ".");
        return getRegistry().get(armorSet);
    }

    @NotNull
    public ArmorBase getPiece(@NotNull ArmorSet armorSet, @Nullable ArmorType armorType) {
        if(getSet(armorSet).get(armorType) == null) throw new NullPointerException("Non-registered armor piece: " + armorType + ".");
        return getSet(armorSet).get(armorType);
    }

    private void register(ArmorSet armorSet, ArmorBase armorBase) {
        armorRegistry.computeIfAbsent(armorSet, k -> new EnumMap<>(ArmorType.class)).put(armorBase.getType(), armorBase);
    }

    private void registerAll() {
        register(LeatherSet.getSet(),LeatherSet.Helmet());
        register(LeatherSet.getSet(),LeatherSet.Chestplate());
        register(LeatherSet.getSet(),LeatherSet.Leggings());
        register(LeatherSet.getSet(),LeatherSet.Boots());
        register(HardenedLeatherSet.getSet(),HardenedLeatherSet.Helmet());
        register(HardenedLeatherSet.getSet(),HardenedLeatherSet.Chestplate());
        register(HardenedLeatherSet.getSet(),HardenedLeatherSet.Leggings());
        register(HardenedLeatherSet.getSet(),HardenedLeatherSet.Boots());
        register(TitanSet.getSet(),TitanSet.Helmet());
        register(TitanSet.getSet(),TitanSet.Chestplate());
        register(TitanSet.getSet(),TitanSet.Leggings());
        register(TitanSet.getSet(),TitanSet.Boots());
    }
}
