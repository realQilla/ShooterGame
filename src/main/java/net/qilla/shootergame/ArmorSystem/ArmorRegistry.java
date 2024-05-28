package net.qilla.shootergame.ArmorSystem;

import net.qilla.shootergame.ArmorSystem.ArmorType.ArmorBase;
import net.qilla.shootergame.ArmorSystem.ArmorType.HardenedLeatherSet;
import net.qilla.shootergame.ArmorSystem.ArmorType.LeatherSet;
import net.qilla.shootergame.ArmorSystem.ArmorType.TitanSet;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {

    private static final Map<ArmorID, ArmorBase> armorRegistry = new HashMap<>();

    private ArmorRegistry() {
    }

    private static void register(ArmorID armorID, ArmorBase armorBase) {
        armorRegistry.put(armorID, armorBase);
    }

    public static ArmorBase getSet(ArmorID armorID) {
        return armorRegistry.get(armorID);
    }

    public static Map<ArmorID, ArmorBase> getArmorRegistry() {
        return armorRegistry;
    }

    public static void registerAll() {
        register(new ArmorID(LeatherSet.Helmet().getArmorSet()), LeatherSet.Helmet());
        register(new ArmorID(LeatherSet.Chestplate().getArmorSet()), LeatherSet.Chestplate());
        register(new ArmorID(LeatherSet.Leggings().getArmorSet()), LeatherSet.Leggings());
        register(new ArmorID(LeatherSet.Boots().getArmorSet()), LeatherSet.Boots());
        register(new ArmorID(HardenedLeatherSet.Helmet().getArmorSet()), HardenedLeatherSet.Helmet());
        register(new ArmorID(HardenedLeatherSet.Chestplate().getArmorSet()), HardenedLeatherSet.Chestplate());
        register(new ArmorID(HardenedLeatherSet.Leggings().getArmorSet()), HardenedLeatherSet.Leggings());
        register(new ArmorID(HardenedLeatherSet.Boots().getArmorSet()), HardenedLeatherSet.Boots());
        register(new ArmorID(TitanSet.Helmet().getArmorSet()), TitanSet.Helmet());
        register(new ArmorID(TitanSet.Chestplate().getArmorSet()), TitanSet.Chestplate());
        register(new ArmorID(TitanSet.Leggings().getArmorSet()), TitanSet.Leggings());
        register(new ArmorID(TitanSet.Boots().getArmorSet()), TitanSet.Boots());
    }
}
