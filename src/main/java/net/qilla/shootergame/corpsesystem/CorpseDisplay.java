package net.qilla.shootergame.corpsesystem;

import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.qilla.shootergame.npccreator.NPCBuilder;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.type.Slab;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CorpseDisplay {

    public void body(Player player) throws NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Location location = getCorpseLocation(player.getLocation());
        ServerPlayer npc = createNPC(player, location);
        npc.setPose(Pose.SLEEPING);

        List<SynchedEntityData.DataValue<?>> dataList = npc.getEntityData().packAll();

        PlayerTeam playerTeam = new PlayerTeam(new Scoreboard(), npc.getName().toString());
        playerTeam.getPlayers().add(npc.getScoreboardName());
        playerTeam.setCollisionRule(Team.CollisionRule.NEVER);
        playerTeam.setNameTagVisibility(Team.Visibility.NEVER);

        Bukkit.getOnlinePlayers().forEach(playerNear -> {
            if (playerNear.getWorld() != playerNear.getWorld() && playerNear.getLocation().distance(playerNear.getLocation()) > 32) return;
            final var playerCon = ((CraftPlayer) playerNear).getHandle().connection;

            playerCon.sendPacket(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            playerCon.sendPacket(new ClientboundAddEntityPacket(npc, npc.getId()));
            playerCon.sendPacket(new ClientboundSetEntityDataPacket(npc.getId(), dataList));

            playerCon.sendPacket(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam));
            playerCon.sendPacket(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));

            new BukkitRunnable() {
                @Override
                public void run() {
                    playerCon.sendPacket(new ClientboundRemoveEntitiesPacket(npc.getId()));
                }
            }.runTaskLaterAsynchronously(ShooterGame.getInstance(), 320);
        });

        //Field dataPose = Entity.class.getDeclaredField("DATA_POSE");
        //Field sleepingPosId = LivingEntity.class.getDeclaredField("SLEEPING_POS_ID");
        //dataPose.setAccessible(true);
        //sleepingPosId.setAccessible(true);
        //dataList.add(new SynchedEntityData.DataItem<>((EntityDataAccessor) sleepingPosId.get(null), Optional.of(blockPos)).value());
        //dataList.add(new SynchedEntityData.DataItem<>((EntityDataAccessor) dataPose.get(null), Pose.SLEEPING).value());
    }

    private Location getCorpseLocation(Location location) {
        final double X = location.getBlockX();
        final double Y = location.getBlockY() + 0.10;
        final double Z = location.getBlockZ();
        final float yaw = location.getYaw();
        final float pitch = location.getPitch();
        Location blockLoc = new Location(location.getWorld(), X, Y, Z, yaw, pitch);
        while(!blockLoc.clone().subtract(0, 1, 0).getBlock().isCollidable() && blockLoc.clone().subtract(0, 1, 0).getY() > -64) {
            blockLoc.subtract(0, 1, 0);
        }
        if(blockLoc.getBlock().getBlockData() instanceof Slab) blockLoc.add(0, 0.5, 0);

        return blockLoc;
    }

    private ServerPlayer createNPC(Player player, Location location) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        NPCBuilder npcBuilder = new NPCBuilder(player.getWorld(), location, location.getYaw(), 0);
        npcBuilder.setName("Corpse");
        npcBuilder.setSkin(player);
        npcBuilder.build();
        return npcBuilder.getNPC();
    }
}
