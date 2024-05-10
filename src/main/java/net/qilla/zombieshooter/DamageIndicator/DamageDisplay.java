package net.qilla.zombieshooter.DamageIndicator;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.Utils.Randomizer;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageDisplay {

    public void indicator(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        Entity attacker = event.getDamageSource().getDirectEntity();
        Location location = entity.getLocation();
        location.setY(entity.getY() + 1.5f);
        long damage = Math.round(event.getDamage());
        Display indicator;
        if(attacker == null ) {
            location.setX(new Randomizer().between(location.getX() - 0.5f, location.getX() + 0.5f));
            location.setZ(new Randomizer().between(location.getZ() - 0.5f, location.getZ() + 0.5f));
            indicator = location.getWorld().spawn(location, TextDisplay.class, display -> {
                display.text(MiniMessage.miniMessage().deserialize("<white>" + damage + "</white>"));
                display.setAlignment(TextDisplay.TextAlignment.CENTER);
                display.setBillboard(Display.Billboard.CENTER);
            });
        } else {
            location.setX(new Randomizer().between(location.getX() - 0.5f, location.getX() + 0.5f));
            location.setZ(new Randomizer().between(location.getZ() - 0.5f, location.getZ() + 0.5f));
            indicator = location.getWorld().spawn(location, TextDisplay.class, display -> {
                display.text(MiniMessage.miniMessage().deserialize("<white>" + damage + "</white>"));
                display.setAlignment(TextDisplay.TextAlignment.CENTER);
                display.setBillboard(Display.Billboard.CENTER);
            });
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                indicator.remove();
            }
        }.runTaskLater(ZombieShooter.getInstance(), 80);
    }
}
