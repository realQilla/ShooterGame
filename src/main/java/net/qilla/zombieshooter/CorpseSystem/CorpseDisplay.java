package net.qilla.zombieshooter.CorpseSystem;

import com.destroystokyo.paper.ClientOption;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.RelativeMovement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CorpseDisplay {

    public void body(EntityDeathEvent event) throws IOException {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        final CraftEntity craftEntity = (CraftEntity) event.getEntity();
        final CraftWorld craftWorld = (CraftWorld) entity.getWorld();
        final CraftServer craftServer = craftWorld.getHandle().getCraftServer();

        final ServerLevel nmsWorld = craftWorld.getHandle();
        final MinecraftServer nmsServer = craftServer.getServer();

        final GameProfile profile = new GameProfile(UUID.randomUUID(), "BILLY");
        final CustomServerPlayer npc = new CustomServerPlayer(nmsServer, nmsWorld, profile, ClientInformation.createDefault());
        npc.connection.teleport(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        for(Player player : Bukkit.getOnlinePlayers()) {
            if (entity.getWorld() != player.getWorld()) return;
            if (entity.getLocation().distance(player.getLocation()) > 32) return;


            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
            nmsPlayer.connection.sendPacket(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            nmsPlayer.connection.sendPacket(new ClientboundRotateHeadPacket(npc, (byte) ((int)(location.getYaw() * 256.0F / 360.0F))));
            nmsPlayer.connection.sendPacket(new ClientboundAddEntityPacket(npc));
            List<SynchedEntityData.DataValue<?>> dataList = npc.getEntityData().packAll();
            nmsPlayer.connection.sendPacket(new ClientboundSetEntityDataPacket(npc.getId(), dataList));
            //nmsPlayer.connection.sendPacket(new ClientboundAddEntityPacket(npc));
        }
    }
}
