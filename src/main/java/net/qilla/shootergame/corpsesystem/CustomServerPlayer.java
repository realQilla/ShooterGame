package net.qilla.shootergame.corpsesystem;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.network.protocol.PacketFlow;

import java.io.IOException;

public class CustomServerPlayer extends ServerPlayer {
    public final NPCConnection connection;

    public CustomServerPlayer(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation clientInformation) throws IOException {
        super(server, level, profile, clientInformation);
        this.connection = new NPCConnection(PacketFlow.CLIENTBOUND, this);
    }
}