package net.qilla.shootergame.blocksystem.customblock.miningsystem;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.qilla.shootergame.blocksystem.blockdb.MineableData;
import net.qilla.shootergame.blocksystem.customblock.CustomBlock;
import net.qilla.shootergame.blocksystem.customblock.MiningReg;
import net.qilla.shootergame.util.BlockManagement;
import net.qilla.shootergame.util.ItemManagement;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.util.TriSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public final class MiningInstance {

    private final ShooterGame plugin = ShooterGame.getInstance();
    private final MiningReg miningReg = plugin.getMiningReg();

    private final MiningID miningID;
    private BukkitTask breakTask = null;
    private final Player player;
    private final Block block;
    private final MineableData mineableData;
    private final CustomBlock customBlock;

    private int breakProgress;

    MiningInstance(Player player, MiningID miningID, MineableData mineableData, Block block) {
        this.player = player;
        this.miningID = miningID;

        this.block = block;
        this.mineableData = mineableData;
        this.customBlock = plugin.getCustomBlockRegistry().getFromRegistryID(mineableData.blockID());
        this.breakProgress = 0;

        if(!hasCorrectTool()) return;
        if (customBlock.breakTime() == null) return;
        if (customBlock.breakTime() == 0) instantMine();
        else progressiveMine();
    }

    private void instantMine() {
        Bukkit.getScheduler().runTask(ShooterGame.getInstance(), this::finish);
    }

    private void progressiveMine() {
        breakTask = Bukkit.getScheduler().runTaskTimer(ShooterGame.getInstance(), () -> {
            if (breakProgress >= 9) {
                breakTask.cancel();
                finish();
            }
            else crackBlock(breakProgress);
            breakProgress++;
        }, 0, Math.round(customBlock.breakTime() / 9.0));
    }

    private void crackBlock(int breakProgress) {
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());
        ServerGamePacketListenerImpl connection = nmsPlayer.connection;
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

    private void finish() {
        crackBlock(-1);
        BlockManagement.popBlock(block.getLocation(), new TriSound(customBlock.sound(), 1, 0.75f));
        itemOutput();
        if (customBlock.getNode() != null) {
            nodeLogic();
            return;
        }
        removeBlock();
    }

    private void removeBlock() {
        new BlockBreakEvent(block, player).callEvent();
        if (mineableData.isPermanent()) {
            lockBlock();
            return;
        }
        block.setType(Material.AIR);
        plugin.getBlockMapper().removeBlock(block.getLocation());
    }

    private void nodeLogic() {
        if(!miningReg.isNodeMapped(block)) miningReg.setNode(block, customBlock.getNode().withinNode());

        final int nodeAmount = miningReg.subtractNode(block);
        if(nodeAmount <= 0) removeBlock();
    }

    private void lockBlock() {
        miningReg.lockBlock(block);
        block.setType(customBlock.fillMaterial());
        Bukkit.getScheduler().runTaskLater(ShooterGame.getInstance(), this::unlockBlock, customBlock.respawnSec() * 20L);
    }

    private void unlockBlock() {
        miningReg.unlockBlock(block);
        BlockManagement.setBlock(customBlock.material(), block.getLocation(), new TriSound(customBlock.sound(), 1, 0.0f));
    }

    private boolean hasCorrectTool() {
        if (customBlock.correctTool() == null) return true;
        return customBlock.correctTool().contains(player.getInventory().getItemInMainHand().getType());
    }

    void forceEnd() {
        crackBlock(-1);
        if (breakTask != null) breakTask.cancel();
        miningReg.removeMining(miningID);
    }
}