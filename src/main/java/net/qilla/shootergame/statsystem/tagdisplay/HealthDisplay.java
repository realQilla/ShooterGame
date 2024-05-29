package net.qilla.shootergame.statsystem.tagdisplay;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.statsystem.EntityKey;
import net.qilla.shootergame.statsystem.healthcalc.HealthDifference;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthDisplay {

    private final LivingEntity entity;
    private final long health;
    private final long amount;
    private final DisplayType type;

    public HealthDisplay(LivingEntity entity, HealthDifference healthDifference, DisplayType type) {
        this.entity = entity;
        this.health = healthDifference.getHealth();
        this.amount = healthDifference.getAmount();
        this.type = type;
    }

    public HealthDisplay(LivingEntity entity) {
        this.entity = entity;
        this.health = Math.round(entity.getHealth());
        this.amount = 0;
        this.type = DisplayType.NONE;
    }

    public void initialDisplaySetup() {
        updateHealthDisplay();
        entity.setCustomNameVisible(true);
        entity.setMetadata(String.valueOf(EntityKey.MODIFIED_ENTITY), new FixedMetadataValue(ShooterGame.getInstance(), true));
    }

    public void updateHealthDisplay() {

        if(type == DisplayType.HEAL) {
            final long currentHealth = health + amount;

            entity.customName(MiniMessage.miniMessage().deserialize(entity.getType().name() + " <green>♥ " + currentHealth + "</green>"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    entity.customName(MiniMessage.miniMessage().deserialize(entity.getType().name() + " <white>♥ " + currentHealth + "</white>"));
                }
            }.runTaskLater(ShooterGame.getInstance(), 10);
            return;
        }

        if(type == DisplayType.DAMAGE) {
            final long currentHealth = (health - amount);

            entity.customName(MiniMessage.miniMessage().deserialize(entity.getType().name() + " <red>♥ " + currentHealth + "</red>"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    entity.customName(MiniMessage.miniMessage().deserialize(entity.getType().name() + " <white>♥ " + currentHealth + "</white>"));
                }
            }.runTaskLater(ShooterGame.getInstance(), 10);
            return;
        }

        if (type == DisplayType.NONE) {
            entity.customName(MiniMessage.miniMessage().deserialize(entity.getType().name() + " <white>♥ " + health + "</white>"));
            return;
        }
    }

    public enum DisplayType {
        HEAL, DAMAGE, NONE
    }
}
