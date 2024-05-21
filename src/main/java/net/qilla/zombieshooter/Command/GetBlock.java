package net.qilla.zombieshooter.Command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlock;
import net.qilla.zombieshooter.Permission.PermissionCommand;
import net.qilla.zombieshooter.Utils.ItemManagement;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GetBlock implements CommandExecutor, TabExecutor {

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

        CustomBlock customBlock;
        int amount = 1;

        try {
            customBlock = CustomBlock.valueOf(args[0].toUpperCase());
            if (args.length == 2) {
                amount = Integer.parseInt(args[1]);
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You specified an invalid input.</red>"));
            return true;
        }

        ItemStack item = getItemStack(customBlock, amount);
        ItemManagement.giveItem(player, item);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);

        return true;
    }

    private static @NotNull ItemStack getItemStack(CustomBlock customBlock, int amount) {
        ItemStack item = new ItemStack(customBlock.material());
        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize("<!italic><green>Custom Block " + customBlock.name() + " </green>"));
            meta.getPersistentDataContainer().set(new NamespacedKey(ZombieShooter.getInstance(), "block_id"), PersistentDataType.SHORT, customBlock.id());
        });
        item.setAmount(amount);
        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission(PermissionCommand.getBlock)) return List.of();

        if (args.length == 1) {
            List<String> materialList = new ArrayList<>();
            for (CustomBlock customBlock : CustomBlock.values()) {
                materialList.add(customBlock.toString());
            }
            return materialList;
        }
        if(args.length == 2) {
            List<String> numList = new ArrayList<>();
            for (int i = 0 ; i < 65; i += 16) {
                numList.add(String.valueOf(i));
            }
            return numList;
        }
        return List.of();
    }
}
