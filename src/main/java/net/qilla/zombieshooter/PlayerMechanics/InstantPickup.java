package net.qilla.zombieshooter.PlayerMechanics;

import net.qilla.zombieshooter.Utils.ItemManagement;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class InstantPickup {

    public void entityDropItem(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();

        if(player == null) return;

        ItemManagement.giveItem(player, event.getDrops().stream().toList());
        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}
