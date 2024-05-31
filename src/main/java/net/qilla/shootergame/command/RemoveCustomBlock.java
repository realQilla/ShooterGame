package net.qilla.shootergame.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.ArmorKey;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import net.qilla.shootergame.blocksystem.blockdb.BlockMapper;
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import net.qilla.shootergame.util.BlockManagement;
import net.qilla.shootergame.util.ItemManagement;
import net.qilla.shootergame.util.TriSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemoveCustomBlock {

    private final ShooterGame plugin;
    private final Commands commands;

    private final String command = "removecustomblock";
    private final List<String> commandAlias = List.of("rcb");
    private final String argX = "x";
    private final String argY = "y";
    private final String argZ = "z";

    public RemoveCustomBlock(ShooterGame plugin, Commands commands) {
        this.plugin = plugin;
        this.commands = commands;
    }

    public void register() {
        final LiteralArgumentBuilder<CommandSourceStack> commandNode = Commands
                .literal(command)
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("shooter.removecustomblock"))
                .executes(this::usage);

        final RequiredArgumentBuilder<CommandSourceStack, String> xNode = Commands
                .argument(argX, StringArgumentType.word())
                .suggests((context, builder) -> {
                    final String argument = builder.getRemaining();
                    final Player player = (Player) context.getSource().getSender();

                    final RayTraceResult rayTraceResult = player.rayTraceBlocks(12.0);
                    if(rayTraceResult != null) {
                        final String blockX = String.valueOf(rayTraceResult.getHitBlock().getX());
                        if(blockX.regionMatches(true, 0, argument, 0, argument.length()))
                            builder.suggest(blockX);
                    }
                    return builder.buildFuture();
                })
                .executes(this::usage);

        final RequiredArgumentBuilder<CommandSourceStack, String> yNode = Commands
                .argument(argY, StringArgumentType.word())
                .suggests((context, builder) -> {
                    final String argument = builder.getRemaining();
                    final Player player = (Player) context.getSource().getSender();

                    final RayTraceResult rayTraceResult = player.rayTraceBlocks(12.0);
                    if(rayTraceResult != null) {
                        final String blockY = String.valueOf(rayTraceResult.getHitBlock().getY());
                        if(blockY.regionMatches(true, 0, argument, 0, argument.length()))
                            builder.suggest(blockY);
                    }
                    return builder.buildFuture();
                })
                .executes(this::usage);

        final ArgumentCommandNode<CommandSourceStack, String> zNode = Commands
                .argument(argZ, StringArgumentType.word())
                .suggests((context, builder) -> {
                    final String argument = builder.getRemaining();
                    final Player player = (Player) context.getSource().getSender();

                    final RayTraceResult rayTraceResult = player.rayTraceBlocks(12.0);
                    if(rayTraceResult != null) {
                        final String blockZ = String.valueOf(rayTraceResult.getHitBlock().getZ());
                        if(blockZ.regionMatches(true, 0, argument, 0, argument.length()))
                            builder.suggest(blockZ);
                    }
                    return builder.buildFuture();
                })
                .executes(this::remove).build();

        xNode.then(yNode.then(zNode));
        commandNode.then(xNode);

        this.commands.register(commandNode.build(), commandAlias);
    }

    private int usage(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid arguments.</red>"));
        return Command.SINGLE_SUCCESS;
    }

    private int remove(CommandContext<CommandSourceStack> context) {
        final Player player = (Player) context.getSource().getSender();
        final String specifiedX = context.getArgument(argX, String.class);
        final String specifiedY = context.getArgument(argY, String.class);
        final String specifiedZ = context.getArgument(argZ, String.class);

        if(specifiedX.isEmpty() || specifiedY.isEmpty() || specifiedZ.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must specify all three coordinates.</red>"));
            return 0;
        }

        final Optional<Integer> possibleX;
        final Optional<Integer> possibleY;
        final Optional<Integer> possibleZ;

        try {
            possibleX = Optional.of(Integer.parseInt(specifiedX));
            possibleY = Optional.of(Integer.parseInt(specifiedY));
            possibleZ = Optional.of(Integer.parseInt(specifiedZ));
        } catch (NumberFormatException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid coordinates.</red>"));
            return 0;
        }
        final BlockMapper blockMapper = plugin.getBlockMapper();
        final Location blockLoc = new Location(player.getWorld(), possibleX.get(), possibleY.get(), possibleZ.get());

        if(blockMapper.getMineableData(blockLoc) == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>There is no custom block at that position.</red>"));
            return 0;
        } else {
            BlockManagement.removeBlock(blockLoc, new TriSound(Sound.BLOCK_ANVIL_LAND, 0.5f, 0.0f));
            blockMapper.removeBlock(blockLoc);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully remove the custom block!</green>"));
        }
        return Command.SINGLE_SUCCESS;
    }
}