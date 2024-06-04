package net.qilla.shootergame.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.blocksystem.blockdb.BlockMapper;
import net.qilla.shootergame.util.BlockManagement;
import net.qilla.shootergame.util.TriSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.List;

public class RCBCom {

    private final ShooterGame plugin;
    private final Commands commands;

    private final String command = "removecustomblock";
    private final List<String> commandAlias = List.of("rcb");
    private final String argX = "x";
    private final String argY = "y";
    private final String argZ = "z";

    public RCBCom(ShooterGame plugin, Commands commands) {
        this.plugin = plugin;
        this.commands = commands;
    }

    public void register() {
        final LiteralArgumentBuilder<CommandSourceStack> commandNode = Commands
                .literal(command)
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("shooter.removecustomblock"))
                .executes(this::usage);

        final RequiredArgumentBuilder<CommandSourceStack, Integer> xNode = Commands
                .argument(argX, IntegerArgumentType.integer())
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

        final RequiredArgumentBuilder<CommandSourceStack, Integer> yNode = Commands
                .argument(argY, IntegerArgumentType.integer())
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

        final ArgumentCommandNode<CommandSourceStack, Integer> zNode = Commands
                .argument(argZ, IntegerArgumentType.integer())
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
        final int specifiedX = context.getArgument(argX, Integer.class);
        final int specifiedY = context.getArgument(argY, Integer.class);
        final int specifiedZ = context.getArgument(argZ, Integer.class);

        final BlockMapper blockMapper = plugin.getBlockMapper();
        final Location blockLoc = new Location(player.getWorld(), specifiedX, specifiedY, specifiedZ);

        if(blockMapper.getMineableData(blockLoc) == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>There is no custom block at that position.</red>"));
            return 0;
        } else {
            BlockManagement.removeBlock(blockLoc, new TriSound(Sound.BLOCK_ANVIL_PLACE, 0.5f, 2.0f));
            blockMapper.removeBlock(blockLoc);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully remove the custom block!</green>"));
        }
        return Command.SINGLE_SUCCESS;
    }
}