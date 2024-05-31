package net.qilla.shootergame.armorsystem;

import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import net.qilla.shootergame.armorsystem.armortype.HardenedLeatherSet;
import net.qilla.shootergame.armorsystem.armortype.LeatherSet;
import net.qilla.shootergame.armorsystem.armortype.TitanSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CustomArmorReg {

    private final Map<ArmorSet, List<ArmorBase>> armorRegistry = new HashMap<>();

    public CustomArmorReg() {
        registerAll();
    }

    public Map<ArmorSet, List<ArmorBase>> getRegistry() {
        return armorRegistry;
    }

    public List<ArmorBase> getSet(ArmorSet armorSet) {
        return armorRegistry.get(armorSet);
    }

    private void register(ArmorSet armorSet, ArmorBase armorBase) {
        armorRegistry.computeIfAbsent(armorSet, k -> new ArrayList<>()).add(armorBase);
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
