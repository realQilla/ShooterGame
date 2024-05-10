package net.qilla.zombieshooter.DamageIndicator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class IndicatorListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof LivingEntity) {
            new DamageDisplay().indicator(event);
        }
    }
}
