package net.qilla.shootergame.Command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.BlockSystem.CustomBlock.CustomBlock;
import net.qilla.shootergame.Permission.PermissionCommand;
import net.qilla.shootergame.Utils.ItemManagement;
import net.qilla.shootergame.ShooterGame;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetBlock implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>This command can only be executed by a player.</red>"));
            return true;
        }

        if (!player.hasPermission(PermissionCommand.getBlock)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You do not have permission to execute this command.</red>"));
            return true;
        }

        CustomBlock customBlock;
        boolean isPermanent = false;
        int amount = 1;

        try {
            customBlock = CustomBlock.valueOf(args[0].toUpperCase());
            isPermanent = Boolean.parseBoolean(args[1]);
            amount = Integer.parseInt(args[2]);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You specified an invalid input. Make sure to specify the block, type, and optionally, an amount.</red>"));
            return true;
        }

        if (amount < 1 || amount > 2048) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You specified an invalid amount of items.</red>"));
            return true;
        }

        ItemStack item = getItemStack(customBlock, amount, isPermanent);
        ItemManagement.giveItem(player, item);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);

        return true;
    }

    private @NotNull ItemStack getItemStack(CustomBlock customBlock, int amount, boolean isPermanent) {
        ItemStack item = new ItemStack(customBlock.material());
        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize("<!italic><green>" + customBlock.name() + " </green>"));
            meta.lore(List.of(MiniMessage.miniMessage().deserialize("<!italic><gray>Permanent: " + isPermanent + "</gray>")));
            meta.getPersistentDataContainer().set(new NamespacedKey(ShooterGame.getInstance(), "permanent_block"), PersistentDataType.BOOLEAN, isPermanent);
            meta.getPersistentDataContainer().set(new NamespacedKey(ShooterGame.getInstance(), "block_id"), PersistentDataType.SHORT, customBlock.id());
        });
        item.setAmount(amount);
        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission(PermissionCommand.getBlock)) return List.of();
        if (args.length == 1) return Arrays.stream(CustomBlock.values()).map(CustomBlock::toString).collect(Collectors.toList());
        if(args.length == 2) return List.of("true", "false");
        if(args.length == 3) return IntStream.range(16, 129).filter(i -> i % 16 == 0).mapToObj(String::valueOf).collect(Collectors.toList());
        return List.of();
    }
}