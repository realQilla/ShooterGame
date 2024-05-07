package net.qilla.zombieshooter.Command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import net.qilla.zombieshooter.Utils.ItemManagement;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
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

import java.util.ArrayList;
import java.util.List;

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

        String inputName = args[0].toUpperCase();
        String inputTier = args[1];

        String gunName;

        switch (inputName) {
            case "STANDARDPISTOL": {
                gunName = "standard_pistol";
                break;
            }
            case "ASSAULTRIFLE": {
                gunName = "assault_rifle";
                break;
            }
            default: {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid gun name</red>"));
                return true;
            }
        }

        String gunTier;

        switch(inputTier) {
            case "1": {
                gunTier = "tier_1";
                break;
            }
            case "2": {
                gunTier = "tier_2";
                break;
            }
            case "3": {
                gunTier = "tier_3";
                break;
            }
            default: {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid gun tier</red>"));
                return true;
            }
        }

        if(!GunRegistry.getGunRegistry().containsKey(new UniqueID(gunName, gunTier))) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid gun name</red>"));
            return true;
        }


        GunBase gunType = GunRegistry.getGunRegistry().get(new UniqueID(gunName, gunTier));

        ItemStack item = new ItemStack(gunType.getGunMaterial());

        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(gunType.getGunName()));
            List<String> lore = gunType.getGunLore();
            List<Component> loreComponents = new ArrayList<>();
            for(String loreLine: lore) {
                loreComponents.add(MiniMessage.miniMessage().deserialize(loreLine));
            }

            meta.lore(loreComponents);
            meta.getPersistentDataContainer().set(GunData.TYPE_GUN.getKey(), PersistentDataType.STRING, gunType.getUniqueID().gunType());
            meta.getPersistentDataContainer().set(GunData.TIER_GUN.getKey(), PersistentDataType.STRING, gunType.getUniqueID().gunTier());
            meta.getPersistentDataContainer().set(GunData.GUN_AMMUNITION_CAPACITY.getKey(), PersistentDataType.INTEGER, gunType.getAmmunitionMod().ammoCapacity());
            meta.getPersistentDataContainer().set(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, gunType.getAmmunitionMod().gunMagazine());
            meta.getPersistentDataContainer().set(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN, false);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
        player.playSound(player, "minecraft:entity.experience_orb.pickup", 1.0f, 1.0f);
        new ItemManagement().giveItem(player, item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("standardpistol", "assaultrifle");
    }
}
