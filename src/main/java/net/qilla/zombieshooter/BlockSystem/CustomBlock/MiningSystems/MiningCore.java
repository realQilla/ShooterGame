package net.qilla.zombieshooter.BlockSystem.CustomBlock.MiningSystems;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.BlockMapper;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.MineableData;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.BlockKey;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MiningCore {

    public static void sentListener(ServerboundPlayerActionPacket packet, Player player) {
        Location blockLoc = new Location(player.getWorld(),
                packet.getPos().getX(),
                packet.getPos().getY(),
                packet.getPos().getZ());
        Block block = blockLoc.getBlock();

        if (block.hasMetadata(BlockKey.lockedBlock.getKey())) return;
        MineableData mineableData = BlockMapper.getInstance().getMineableData(blockLoc);
        if(mineableData == null) return;
        if(player.getGameMode() == GameMode.CREATIVE) return;
        MiningCustom.MiningInstance miningInstance = new MiningCustom.MiningInstance(player.getUniqueId(), block.hashCode());

        switch (packet.getAction()) {
            case ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK: {
                if (MiningCustom.lookup(miningInstance)) MiningCustom.getMiningInstance(player, miningInstance).startMining(block, mineableData);
                 else new MiningCustom(player, miningInstance).startMining(block, mineableData);
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