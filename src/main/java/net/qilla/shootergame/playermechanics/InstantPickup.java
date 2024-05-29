package net.qilla.shootergame.playermechanics;

import net.qilla.shootergame.util.ItemManagement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class InstantPickup implements Listener {

    @EventHandler
    public void entityDropItem(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();

        if(player == null) return;

        ItemManagement.giveItem(player, event.getDrops().stream().toList());
        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}
