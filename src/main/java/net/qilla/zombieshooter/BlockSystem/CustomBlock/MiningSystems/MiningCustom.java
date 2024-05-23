package net.qilla.zombieshooter.BlockSystem.CustomBlock.MiningSystems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.BlockMapper;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.MineableData;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.BlockKey;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlock;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlockRegistry;
import net.qilla.zombieshooter.Utils.ItemManagement;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningCustom {

    private static final Map<MiningInstance, MiningCustom> mineMap = new HashMap<>();

    private BukkitTask damageTask = null;
    private BukkitTask mineTask = null;
    private final Player player;
    private Block block;
    private MineableData mineableData;
    private CustomBlock customBlock;
    private int breakProgress = 0;

    protected MiningCustom(Player player, MiningInstance miningInstance) {
        this.player = player;

        mineMap.put(miningInstance, this);
    }

    protected void startMining(Block block, MineableData mineableData) {
        this.block = block;
        this.mineableData = mineableData;
        this.customBlock = CustomBlockRegistry.getFromRegistryID(mineableData.blockID());
        this.breakProgress = 0;

        if(!hasCorrectTool()) return;
        if (customBlock.breakTime() == null) return;
        if (customBlock.breakTime() == 0) instantMine();
        else progressiveMine();
    }

    private void instantMine() {
        Bukkit.getScheduler().runTask(ZombieShooter.getInstance(), this::finishedMining);
    }

    private void progressiveMine() {
        damageTask = Bukkit.getScheduler().runTaskTimer(ZombieShooter.getInstance(), () -> {
            if (breakProgress >= 9) finishedMining();
            else crackBlock(breakProgress);
            breakProgress++;
        }, 0, Math.round(customBlock.breakTime() / 9.0));
    }

    private void crackBlock(int breakProgress) {
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

    private void finishedMining() {
        end();
        block.getWorld().playSound(block.getLocation(), customBlock.sound(), 1, 0.75f);
        block.getWorld().spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.25, 0.25, 0.25, 0, customBlock.material().createBlockData());
        itemOutput();
        if (customBlock.getNode() != null) {
            nodeLogic();
            return;
        }
        removeBlock();
    }

    private void resendMine() {
        final MiningInstance newMiningInstance = new MiningInstance(player.getUniqueId(), block.hashCode());
        //final MineableData newMineableData = new MineableData();
        new MiningCustom(player, newMiningInstance).startMining(block, mineableData);
    }

    private void removeBlock() {
        new BlockBreakEvent(block, player).callEvent();
        block.removeMetadata(BlockKey.blockAmount.getKey(), ZombieShooter.getInstance());
        block.removeMetadata(BlockKey.lockedBlock.getKey(), ZombieShooter.getInstance());
        if (mineableData.isPermanent()) {
            lockBlock();
            return;
        }
        block.setType(Material.AIR);
        BlockMapper.getInstance().removeBlock(block.getLocation());
    }

    private void nodeLogic() {
        if (!block.hasMetadata(BlockKey.blockAmount.getKey())) block.setMetadata(BlockKey.blockAmount.getKey(), new FixedMetadataValue(ZombieShooter.getInstance(), customBlock.getNode().withinNode() - 1));

        final int newAmount = block.getMetadata(BlockKey.blockAmount.getKey()).getFirst().asInt() - 1;
        block.setMetadata(BlockKey.blockAmount.getKey(), new FixedMetadataValue(ZombieShooter.getInstance(), newAmount));
        if(newAmount < 0) removeBlock();
        //else MiningCore.sentListener(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, new BlockPos(block.getX(), block.getY(), block.getZ()), Direction.fromYRot(player.getPitch())), player);
    }

    private void lockBlock() {
        block.setMetadata(BlockKey.lockedBlock.getKey(), new FixedMetadataValue(ZombieShooter.getInstance(), true));
        block.setType(customBlock.fillMaterial());
        Bukkit.getScheduler().runTaskLater(ZombieShooter.getInstance(), this::unlockBlock, customBlock.respawnSec() * 20L);
    }

    private void unlockBlock() {
        block.setType(customBlock.material());
        block.removeMetadata(BlockKey.lockedBlock.getKey(), ZombieShooter.getInstance());
        block.getWorld().playSound(block.getLocation(), customBlock.sound(), 1, 0.0f);
        block.getWorld().spawnParticle(Particle.BLOCK, block.getLocation().add(0.6, 0.6, 0.6), 50, 0.25, 0.25, 0.25, 0, customBlock.material().createBlockData());
    }

    private boolean hasCorrectTool() {
        if (customBlock.correctTool() == null) return true;
        return customBlock.correctTool().contains(player.getInventory().getItemInMainHand().getType());
    }

    protected void end() {
        crackBlock(-1);
        if (damageTask != null) damageTask.cancel();
        mineMap.remove(new MiningInstance(player.getUniqueId(), block.hashCode()));
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