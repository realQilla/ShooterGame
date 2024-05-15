package net.qilla.zombieshooter.ArmorSystem;

import net.qilla.zombieshooter.ArmorSystem.ArmorType.ArmorBase;
import net.qilla.zombieshooter.ArmorSystem.ArmorType.HardenedLeatherSet;
import net.qilla.zombieshooter.ArmorSystem.ArmorType.LeatherSet;
import net.qilla.zombieshooter.ArmorSystem.ArmorType.TitanSet;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {

    private final static ArmorRegistry instance = new ArmorRegistry();
    private static final Map<ArmorID, ArmorBase> armorRegistry = new HashMap<>();

    private ArmorRegistry() {
    }

    private void register(ArmorID armorID, ArmorBase armorBase) {
        armorRegistry.put(armorID, armorBase);
    }

    public ArmorBase getSet(ArmorID armorID) {
        return armorRegistry.get(armorID);
    }

    public Map<ArmorID, ArmorBase> getArmorRegistry() {
        return armorRegistry;
    }

    public void registerAll() {
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

    public static ArmorRegistry getInstance() {
        return instance;
    }
}
