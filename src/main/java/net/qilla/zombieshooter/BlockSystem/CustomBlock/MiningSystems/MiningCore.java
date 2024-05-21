package net.qilla.zombieshooter.BlockSystem.CustomBlock.MiningSystems;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlock;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlockRegistry;
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

        if (block.hasMetadata("locked_block")) return;
        CustomBlock customBlock = CustomBlockRegistry.getFromRegistryLoc(blockLoc);
        if(customBlock == null) return;
        MiningCustom.MiningInstance miningInstance = new MiningCustom.MiningInstance(player.getUniqueId(), block.hashCode());

        switch (packet.getAction()) {
            case ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK: {
                if (MiningCustom.lookup(miningInstance)) MiningCustom.getMiningInstance(player, miningInstance).start(block, customBlock);
                 else new MiningCustom(player, miningInstance).start(block, customBlock);
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