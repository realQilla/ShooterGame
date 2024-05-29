package net.qilla.shootergame.corpsesystem;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;

public class NPCConnection extends EmptyConnection {
    private final ServerPlayer npc;

    public NPCConnection(PacketFlow flag, ServerPlayer npc) throws IOException {
        super(flag);
        this.npc = npc;
    }
}