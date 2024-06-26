package net.qilla.shootergame.packetlistener;

import io.netty.channel.*;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.blocksystem.customblock.miningsystem.MiningForwarder;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PacketGeneric {

    private final ShooterGame plugin;

    public PacketGeneric(ShooterGame plugin) {
        this.plugin = plugin;
    }

    private final Set<UUID> playersListened = new HashSet<>();

    public void addListener(Player player) {
        ChannelDuplexHandler handler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                if (packet instanceof ServerboundPlayerActionPacket breakPacket) {
                    new MiningForwarder(plugin).sentListener(breakPacket, player);
                }
                super.channelRead(context, packet);
            }
        };

        ServerGamePacketListenerImpl playerCon = ((CraftPlayer) player).getHandle().connection;
        ChannelPipeline pipeline = playerCon.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), handler);
        playersListened.add(player.getUniqueId());
    }

    public void removeListener(Player player) {
        ServerGamePacketListenerImpl playerCon = ((CraftPlayer) player).getHandle().connection;
        Channel channel = playerCon.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
        playersListened.remove(player.getUniqueId());
    }
}