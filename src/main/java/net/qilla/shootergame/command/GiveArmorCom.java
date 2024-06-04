package net.qilla.shootergame.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.armorsystem.FinalizeArmor;
import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import net.qilla.shootergame.util.ItemManagement;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.Optional;

public class GiveArmorCom {

    private final ShooterGame plugin;
    private final Commands commands;

    private final String command = "givearmor";
    private final List<String> commandAlias = List.of("armor", "ga");
    private final String argSet = "type";

    public GiveArmorCom(ShooterGame plugin, Commands commands) {
        this.plugin = plugin;
        this.commands = commands;
    }

    public void register() {
        final LiteralArgumentBuilder<CommandSourceStack> commandNode = Commands
                .literal(command)
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("shooter.getarmor"))
                .executes(this::usage);

        final ArgumentCommandNode<CommandSourceStack, String> typeNode = Commands
                .argument(argSet, StringArgumentType.word())
                .suggests((context, builder) -> {
                    final String argument = builder.getRemaining();

                    for(ArmorSet armorSet : plugin.getArmorRegistry().getRegistry().keySet()) {
                        final String setName = armorSet.name();
                        if(setName.regionMatches(true, 0, argument, 0, argument.length()))
                            builder.suggest(setName);
                    }
                    return builder.buildFuture();
                })
                .executes(this::get).build();

        commandNode.then(typeNode);

        this.commands.register(commandNode.build(), commandAlias);
    }

    private int usage(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid arguments.</red>"));
        return Command.SINGLE_SUCCESS;
    }

    private int get(CommandContext<CommandSourceStack> context) {
        final Player player = (Player) context.getSource().getSender();
        final String specifiedSet = context.getArgument(argSet, String.class).toUpperCase();

        if(specifiedSet.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must specify an armor set.</red>"));
            return 0;
        }

        final Optional<ArmorSet> possibleSet;
        try {
            possibleSet = Optional.of(ArmorSet.valueOf(specifiedSet));
        } catch(IllegalArgumentException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>That armor set does not exist.</red>"));
            return 0;
        }

        ArmorSet armorSet = possibleSet.get();

        for(ArmorBase armorBase: plugin.getArmorRegistry().getSet(armorSet).values()) {
            final ItemStack item = new FinalizeArmor(armorBase, armorSet).get();
            ItemManagement.giveItem(player, item);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
        }

        return Command.SINGLE_SUCCESS;
    }
}