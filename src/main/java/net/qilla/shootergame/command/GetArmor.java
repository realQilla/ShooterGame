package net.qilla.shootergame.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
import net.qilla.shootergame.statsystem.statmanagement.StatModel;
import net.qilla.shootergame.util.ItemManagement;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetArmor {

    private final ShooterGame plugin;
    private final Commands commands;

    private final String command = "getarmor";
    private final List<String> commandAlias = List.of("ga");
    private final String argSet = "type";

    public GetArmor(ShooterGame plugin, Commands commands) {
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

        for(ArmorBase pieceBase : plugin.getArmorRegistry().getSet(armorSet)) {
            ItemStack item = getItemStack(pieceBase, armorSet);
            ItemManagement.giveItem(player, item);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.25f, 2.0f);
        }

        return Command.SINGLE_SUCCESS;
    }

    private @NotNull ItemStack getItemStack(ArmorBase pieceBase, ArmorSet armorSet) {
        ItemStack item = new ItemStack(pieceBase.getPieceMaterial());
        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(pieceBase.getPieceName()));
            List<String> lore = setLore(pieceBase);
            List<Component> loreComponents = new ArrayList<>();
            for(String loreLine : lore) {
                loreComponents.add(MiniMessage.miniMessage().deserialize(loreLine));
            }

            meta.lore(loreComponents);
            meta.getPersistentDataContainer().set(ArmorKey.ARMOR_SET.getKey(), PersistentDataType.STRING, armorSet.name());
            meta.getPersistentDataContainer().set(ArmorKey.ITEM_STAT_MAX_HEALTH.getKey(), PersistentDataType.LONG, pieceBase.getPieceStat().getMaxHealth());
            meta.getPersistentDataContainer().set(ArmorKey.ITEM_STAT_DEFENSE.getKey(), PersistentDataType.LONG, pieceBase.getPieceStat().getDefense());
            meta.getPersistentDataContainer().set(ArmorKey.ITEM_STAT_REGENERATION.getKey(), PersistentDataType.LONG, pieceBase.getPieceStat().getRegeneration());
            meta.setAttributeModifiers(null);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
        return item;
    }

    private @NotNull List<String> setLore(ArmorBase armorBase) {
        StatModel statModel = armorBase.getPieceStat();

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
        lore.addAll(armorBase.getPieceLore());
        return lore;
    }
}