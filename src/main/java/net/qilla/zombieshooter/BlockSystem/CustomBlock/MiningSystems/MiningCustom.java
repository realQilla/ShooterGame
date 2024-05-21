package net.qilla.zombieshooter.BlockSystem.CustomBlock.MiningSystems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlock;
import net.qilla.zombieshooter.Utils.ItemManagement;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningCustom {

    private static final Map<MiningInstance, MiningCustom> mineMap = new HashMap<>();

    private BukkitTask mineTask = null;
    private final Player player;
    private Block block;
    private CustomBlock customBlock;
    private int breakProgress = 0;

    protected MiningCustom(Player player, MiningInstance miningInstance) {
        this.player = player;

        mineMap.put(miningInstance, this);
    }

    protected void start(Block block, CustomBlock customBlock) {
        this.block = block;
        this.customBlock = customBlock;
        this.breakProgress = 0;

        if (customBlock.breakTime() == null) return;
        if (customBlock.breakTime() == 0) instantMine();
        else progressiveMine();
    }

    protected void end() {
        damageBlock(-1);
        if (mineTask != null) mineTask.cancel();
        mineMap.remove(new MiningInstance(player.getUniqueId(), block.hashCode()));
    }

    private void instantMine() {
        Bukkit.getScheduler().runTask(ZombieShooter.getInstance(), this::brokenBlock);
    }

    private void progressiveMine() {

        mineTask = Bukkit.getScheduler().runTaskTimer(ZombieShooter.getInstance(), () -> {
            if (breakProgress >= 9) brokenBlock();
            else damageBlock(breakProgress);
            breakProgress++;
        }, 0, customBlock.breakTime() / 9);
    }

    private void damageBlock(int breakProgress) {
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());
        var connection = nmsPlayer.connection;
        connection.send(new ClientboundBlockDestructionPacket(block.hashCode(), blockPos, breakProgress));
    }

    private void itemOutput() {
        if (customBlock.itemDrops() == null) return;
        for (CustomBlock.ItemDrops itemDrop : customBlock.itemDrops()) {
            if (Math.random() > itemDrop.chance()) continue;
            int amount = (int) (Math.random() * (itemDrop.amountMax() - itemDrop.amountMin()) + itemDrop.amountMin());
            ItemStack item = new ItemStack(itemDrop.item(), amount);
            ItemManagement.giveItem(player, item);
        }
    }

    private void brokenBlock() {
        end();
        lockBlock();
        new BlockBreakEvent(block, player).callEvent();
        block.setType(customBlock.getNode().fillMaterial());
        block.getWorld().playSound(block.getLocation(), customBlock.sound(), 1, 0.75f);
        block.getWorld().spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.25, 0.25, 0.25, 0, customBlock.material().createBlockData());
        itemOutput();
    }

    private void lockBlock() {
        Bukkit.getScheduler().runTaskLater(ZombieShooter.getInstance(), this::unlockBlock, customBlock.getNode().respawnTime());
        block.setMetadata("locked_block", new FixedMetadataValue(ZombieShooter.getInstance(), true));
    }

    private void unlockBlock() {
        block.setType(customBlock.material());
        block.removeMetadata("locked_block", ZombieShooter.getInstance());
    }

    @NotNull
    public static MiningCustom getMiningInstance(@NotNull Player player, @NotNull MiningInstance miningInstance) {
        if (mineMap.containsKey(miningInstance)) return mineMap.get(miningInstance);
        return new MiningCustom(player, miningInstance);
    }

    public static boolean lookup(MiningInstance miningInstance) {
        return mineMap.containsKey(miningInstance);
    }

    public record MiningInstance(UUID playerUUID, int blockHashcode) {
    }
}