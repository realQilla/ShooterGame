package net.qilla.shootergame.statsystem.statutil;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UpdatePlayer {

    /**
     * Sets the health of the player(client side)
     *
     * @param player The player whose health to modify.
     * @param health The total health after being changed.
     * @param maxHealth The players overall max health.
     */

    public static void healthBarDisplay(Player player, long health, long maxHealth) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        Packet<ClientGamePacketListener> packet = new ClientboundSetHealthPacket(Formula.healthBar(health, maxHealth),
                player.getFoodLevel(),
                player.getSaturation());

        nmsPlayer.connection.sendPacket(packet);
    }
}
