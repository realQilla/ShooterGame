package net.qilla.zombieshooter.Command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.ArmorSystem.ArmorID;
import net.qilla.zombieshooter.ArmorSystem.ArmorRegistry;
import net.qilla.zombieshooter.ArmorSystem.ArmorType.ArmorBase;
import net.qilla.zombieshooter.ArmorSystem.ArmorPDC;
import net.qilla.zombieshooter.Permission.PermissionCommand;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatModel;
import net.qilla.zombieshooter.Utils.ItemManagement;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GetArmor implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>This command can only be executed by a player.</red>"));
            return true;
        }

        if(!player.hasPermission(PermissionCommand.getBlock)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You do not have permission to execute this command.</red>"));
            return true;
        }

        ArmorID.ArmorSet armorSet;

        try {
            armorSet = ArmorID.ArmorSet.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>That armor set does not exist.</red>"));
            return true;
        }

        for (ArmorID.ArmorType eachType : ArmorID.ArmorType.values()) {
            ArmorBase eachPiece = ArmorRegistry.getArmorRegistry().get(new ArmorID(new ArmorID.ArmorPiece(armorSet, eachType)));
            if (eachPiece != null) {
                ItemStack item = getItemStack(eachPiece);
                ItemManagement.giveItem(player, item);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
            }
        }
        return true;
    }

    private static @NotNull ItemStack getItemStack(ArmorBase armorBase) {
        ItemStack item = new ItemStack(armorBase.getArmorMaterial());
        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(armorBase.getArmorName()));
            List<String> lore = setLore(armorBase);
            List<Component> loreComponents = new ArrayList<>();
            for(String loreLine: lore) {
                loreComponents.add(MiniMessage.miniMessage().deserialize(loreLine));
            }

            meta.lore(loreComponents);
            meta.getPersistentDataContainer().set(ArmorPDC.ARMOR_SET.getKey(), PersistentDataType.STRING, armorBase.getArmorSet().armorSet().toString());
            meta.getPersistentDataContainer().set(ArmorPDC.ITEM_STAT_MAX_HEALTH.getKey(), PersistentDataType.LONG, armorBase.getStatModel().getMaxHealth());
            meta.getPersistentDataContainer().set(ArmorPDC.ITEM_STAT_DEFENSE.getKey(), PersistentDataType.LONG, armorBase.getStatModel().getDefense());
            meta.getPersistentDataContainer().set(ArmorPDC.ITEM_STAT_REGENERATION.getKey(), PersistentDataType.LONG, armorBase.getStatModel().getRegeneration());
            meta.setAttributeModifiers(null);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
        return item;
    }

    private static @NotNull List<String> setLore(ArmorBase armorBase) {
        StatModel statModel = armorBase.getStatModel();

        List<String> lore = new ArrayList<>();
        if(statModel.getMaxHealth() != 0) {
            lore.add("<!italic><red>♥</red> <gray>Health:</gray> <white>+" + statModel.getMaxHealth() + "</white>");
        }
        if(statModel.getDefense() != 0) {
            lore.add("<!italic><dark_gray>\uD83D\uDEE1</dark_gray> <gray>Defense:</gray> <white>+" + statModel.getDefense() + "</white>");
        }
        if(statModel.getRegeneration() != 0) {
            lore.add("<!italic><light_purple>\uD83E\uDDEA</light_purple> <gray>Regeneration:</gray> <white>+" + statModel.getRegeneration() + "</white>");
        }
        lore.addAll(armorBase.getArmorLore());
        return lore;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission(PermissionCommand.getBlock)) return List.of();

        Map<String, List<String>> armorSets = new HashMap<>();
        for (Map.Entry<ArmorID, ArmorBase> entry : ArmorRegistry.getArmorRegistry().entrySet()) {
            String armorSet = entry.getKey().armorPiece().armorSet().toString().toLowerCase();

            armorSets.computeIfAbsent(armorSet, k -> new ArrayList<>()).add(armorSet);
        }

        if (args.length == 1) {
            return new ArrayList<>(armorSets.keySet());
        }
        return List.of();
    }
}
