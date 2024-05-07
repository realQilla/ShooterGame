package net.qilla.zombieshooter.PlayerMechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MechanicListener implements Listener {

    @EventHandler
    public void entityDeath(EntityDeathEvent event) {
        new InstantPickup().entityDropItem(event);
    }
}
