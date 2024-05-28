package net.qilla.shootergame.CorpseSystem;

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
}