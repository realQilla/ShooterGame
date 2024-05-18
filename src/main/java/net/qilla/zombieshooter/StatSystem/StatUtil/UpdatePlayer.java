package net.qilla.zombieshooter.StatSystem.StatUtil;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UpdatePlayer {

    public static void healthBarDisplay(Player player, long health, long maxHealth) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        Packet<ClientGamePacketListener> packet = new ClientboundSetHealthPacket(Formula.healthBar(health, maxHealth),
                player.getFoodLevel(),
                player.getSaturation());

        nmsPlayer.connection.sendPacket(packet);
    }
}
