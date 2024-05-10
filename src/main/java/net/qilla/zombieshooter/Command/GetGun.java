package net.qilla.zombieshooter.Command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunData;
import net.qilla.zombieshooter.Utils.ItemManagement;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;
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

public class GetGun implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player))  {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }

        Player player = (Player) sender;

        if(args.length != 2) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid arguments</red>"));
            return true;
        }

        String gunName = args[0];
        String gunTier = args[1];

        if(!GunRegistry.getInstance().getGunRegistry().containsKey(new GunID(gunName, gunTier))) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Selected gun does not exist.</red>"));
            return true;
        }


        GunBase gunType = GunRegistry.getInstance().getGunRegistry().get(new GunID(gunName, gunTier));

        ItemStack item = new ItemStack(gunType.getGunMaterial());

        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(gunType.getGunName()));
            List<String> lore = gunType.getGunLore();
            List<Component> loreComponents = new ArrayList<>();
            for(String loreLine: lore) {
                loreComponents.add(MiniMessage.miniMessage().deserialize(loreLine));
            }

            meta.lore(loreComponents);
            meta.getPersistentDataContainer().set(GunData.GUN_TYPE.getKey(), PersistentDataType.STRING, gunType.getTypeID().gunType());
            meta.getPersistentDataContainer().set(GunData.GUN_TIER.getKey(), PersistentDataType.STRING, gunType.getTypeID().gunTier());
            meta.getPersistentDataContainer().set(GunData.GUN_UUID.getKey(), PersistentDataType.STRING, String.valueOf(UUID.randomUUID()));
            meta.getPersistentDataContainer().set(GunData.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER, gunType.getAmmunitionMod().gunCapacity());
            meta.getPersistentDataContainer().set(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, gunType.getAmmunitionMod().gunMagazine());
            meta.getPersistentDataContainer().set(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(GunData.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN, false);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
        new ItemManagement().giveItem(player, item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<GunID, GunBase> gunRegistry = GunRegistry.getInstance().getGunRegistry();
        Map<String, List<String>> gunTypesAndTiers = new HashMap<>();

        for (Map.Entry<GunID, GunBase> entry : gunRegistry.entrySet()) {
            String gunType = entry.getKey().gunType();
            String gunTier = entry.getKey().gunTier();

            gunTypesAndTiers.computeIfAbsent(gunType, k -> new ArrayList<>()).add(gunTier);
        }

        if (args.length == 1) {
            return new ArrayList<>(gunTypesAndTiers.keySet());
        } else if (args.length == 2 && gunTypesAndTiers.containsKey(args[0])) {
            return gunTypesAndTiers.get(args[0]);
        }

        return List.of();
    }
}
