package net.qilla.zombieshooter.Command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.ArmorSystem.ArmorID;
import net.qilla.zombieshooter.ArmorSystem.ArmorRegistry;
import net.qilla.zombieshooter.ArmorSystem.ArmorType.ArmorBase;
import net.qilla.zombieshooter.ArmorSystem.ArmorPDC;
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
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }

        ArmorID.ArmorSet armorSet;
        ArmorID.ArmorType armorType;

        switch (args.length) {
            case 1 -> {
                try {
                    armorSet = ArmorID.ArmorSet.valueOf(args[0].toUpperCase());
                } catch (IllegalArgumentException e) {
                    armorSet = null;
                }
                armorType = null;
            }
            case 2 -> {
                try {
                    armorSet = ArmorID.ArmorSet.valueOf(args[0].toUpperCase());
                    armorType = ArmorID.ArmorType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid arguments, specify a set and armor type</red>"));
                    return true;
                }
            }
            default -> {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid arguments, specify a set and armor type</red>"));
                return true;
            }
        }

        if (!ArmorRegistry.getInstance().getArmorRegistry().containsKey(new ArmorID(new ArmorID.ArmorPiece(armorSet, armorType))) && armorType != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Selected armor does not exist.</red>"));
            return true;
        }

        if(armorType == null) {
            for (ArmorID.ArmorType eachType : ArmorID.ArmorType.values()) {
                ArmorBase eachPiece = ArmorRegistry.getInstance().getArmorRegistry().get(new ArmorID(new ArmorID.ArmorPiece(armorSet, eachType)));
                if (eachPiece != null) {
                    ItemStack item = getItemStack(eachPiece);
                    new ItemManagement().giveItem(player, item);
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
                }
            }
            return true;
        }

        ArmorBase armorBase = ArmorRegistry.getInstance().getArmorRegistry().get(new ArmorID(new ArmorID.ArmorPiece(armorSet, armorType)));

        ItemStack item = getItemStack(armorBase);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
        new ItemManagement().giveItem(player, item);
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
            meta.getPersistentDataContainer().set(ArmorPDC.ITEM_STAT_DEFENCE.getKey(), PersistentDataType.LONG, armorBase.getStatModel().getDefence());
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
        if(statModel.getDefence() != 0) {
            lore.add("<!italic><dark_gray>\uD83D\uDEE1</dark_gray> <gray>Defence:</gray> <white>+" + statModel.getDefence() + "</white>");
        }
        if(statModel.getRegeneration() != 0) {
            lore.add("<!italic><light_purple>\uD83E\uDDEA</light_purple> <gray>Regeneration:</gray> <white>+" + statModel.getRegeneration() + "</white>");
        }
        lore.addAll(armorBase.getArmorLore());
        return lore;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<String, List<String>> armorSets = new HashMap<>();

        for (Map.Entry<ArmorID, ArmorBase> entry : ArmorRegistry.getInstance().getArmorRegistry().entrySet()) {
            String armorSet = entry.getKey().armorPiece().armorSet().toString().toLowerCase();
            String armorType = entry.getKey().armorPiece().armorType().toString().toLowerCase();

            armorSets.computeIfAbsent(armorSet, k -> new ArrayList<>()).add(armorType);
        }

        if (args.length == 1) {
            return new ArrayList<>(armorSets.keySet());
        } else if (args.length == 2 && armorSets.containsKey(args[0])) {
            return armorSets.get(args[0]);
        }
        return List.of();
    }
}
