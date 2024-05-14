package net.qilla.zombieshooter.CorpseSystem;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.util.HashSet;

public class NPCConnection extends EmptyConnection {
    private final ServerPlayer npc;

    public NPCConnection(PacketFlow flag, ServerPlayer npc) throws IOException {
        super(flag);
        this.npc = npc;
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        // Send a packet to update the NPC's position and rotation on the client side
        ClientboundPlayerPositionPacket packet = new ClientboundPlayerPositionPacket(x, y, z, yaw, pitch, new HashSet<>(), 0);
        send(packet);

        // Update the NPC's position and rotation on the server side
        npc.absMoveTo(x, y, z, yaw, pitch);
    }
}