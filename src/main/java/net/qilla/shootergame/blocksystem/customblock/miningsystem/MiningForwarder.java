package net.qilla.shootergame.blocksystem.customblock.miningsystem;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.blocksystem.blockdb.MineableData;
import net.qilla.shootergame.blocksystem.customblock.MiningReg;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public final class MiningForwarder implements Listener {

    private final ShooterGame plugin;
    private final MiningReg miningReg;

    public MiningForwarder(ShooterGame plugin) {
        this.plugin = plugin;
        this.miningReg = plugin.getMiningReg();
    }

    public void sentListener(ServerboundPlayerActionPacket packet, Player player) {
        final Location blockLoc = new Location(player.getWorld(),
                packet.getPos().getX(),
                packet.getPos().getY(),
                packet.getPos().getZ());
        final Block block = blockLoc.getBlock();
        final MineableData mineableData = plugin.getBlockMapper().getMineableData(blockLoc);

        if(mineableData == null) return;
        if (plugin.getMiningReg().isBlockLocked(block)) return;
        if(player.getGameMode() == GameMode.CREATIVE) return;

        final MiningID miningID = new MiningID(player.getUniqueId(), block.hashCode());

        switch (packet.getAction()) {
            case ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK,
                 ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                 ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM: {
                if (miningReg.isMining(miningID)) miningReg.getMiningInstance(miningID).forceEnd();
                break;
            }
            case ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK: {
                miningReg.addMining(miningID, new MiningInstance(player, miningID, mineableData, block));
                break;
            }
        }
    }
}