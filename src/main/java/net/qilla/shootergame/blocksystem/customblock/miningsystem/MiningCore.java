package net.qilla.shootergame.blocksystem.customblock.miningsystem;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.blocksystem.blockdb.BlockMapper;
import net.qilla.shootergame.blocksystem.blockdb.MineableData;
import net.qilla.shootergame.blocksystem.customblock.BlockKey;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class MiningCore {

    private final ShooterGame plugin;

    public MiningCore(ShooterGame plugin) {
        this.plugin = plugin;
    }

    public void sentListener(ServerboundPlayerActionPacket packet, Player player) {
        Location blockLoc = new Location(player.getWorld(),
                packet.getPos().getX(),
                packet.getPos().getY(),
                packet.getPos().getZ());
        Block block = blockLoc.getBlock();
        if (block.hasMetadata(BlockKey.lockedBlock.getKey())) {
            return;
        }
        MineableData mineableData = plugin.getBlockMapper().getMineableData(blockLoc);
        if(mineableData == null) return;
        if(player.getGameMode() == GameMode.CREATIVE) return;
        MiningCustom.MiningInstance miningInstance = new MiningCustom.MiningInstance(player.getUniqueId(), block.hashCode());
        switch (packet.getAction()) {
            case ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK: {
                //if (MiningCustom.lookup(miningInstance)) MiningCustom.getMiningInstance(player, miningInstance).startMining(block, mineableData);
                 new MiningCustom(player, miningInstance).startMining(block, mineableData);
                break;
            }

            case ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK,
                 ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                 ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM: {
                if (!MiningCustom.lookup(miningInstance)) return;
                MiningCustom.getMiningInstance(player, miningInstance).end();
                break;
            }
        }
    }
}