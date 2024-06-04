package net.qilla.shootergame.armorsystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FinalizeArmor {

    private final ArmorBase armorBase;
    private final ArmorSet armorSet;

    public FinalizeArmor(ArmorBase armorBase, ArmorSet armorSet) {
        this.armorBase = armorBase;
        this.armorSet = armorSet;
    }

    public ItemStack get() {
        final ItemStack armorPiece = new ItemStack(this.armorBase.getMaterial());
        armorPiece.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(this.armorBase.getName()));
            meta.lore(lore());

            meta.getPersistentDataContainer().set(ItemKey.SET_ARMOR.getKey(), PersistentDataType.STRING, armorSet.toString());
            meta.getPersistentDataContainer().set(ItemKey.TYPE_ARMOR.getKey(), PersistentDataType.STRING, armorBase.getType().toString());
            meta.setAttributeModifiers(null);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
        return armorPiece;
    }

    private List<Component> lore() {
        return Arrays.stream(this.armorBase.getStats())
                .map(stat -> MiniMessage.miniMessage().deserialize("<!italic>" + stat.getIcon() + " <gray>" + stat.getName() + ":</gray> <white>+" + stat.getValue() + "</white>"))
                .collect(Collectors.toList());
    }
}
