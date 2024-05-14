package net.qilla.zombieshooter.StatSystem.StatManagement;

import net.qilla.zombieshooter.GunSystem.ArmorSystem.ItemPDC;
import net.qilla.zombieshooter.StatSystem.ActionBar.StatDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatManager extends StatCore {

    private static final Map<UUID, StatManager> statManager = new HashMap<>();

    private final long baseMaxHealth = 100;
    private final long baseDefence = 0;
    private final long baseRegeneration = 1;

    private final Player player;
    private final UUID playerUUID;

    public StatManager(Player player) {
        super(player.getUniqueId());
        this.player = player;
        this.playerUUID = player.getUniqueId();
        statManager.put(playerUUID, this);

        super.setStats(new StatModel(calcTotalMaxHealth(), calcTotalDefence(), calcTotalRegeneration()));
        super.setHealth(baseMaxHealth);
        new StatDisplay(player);
    }

    public void modifyHealth(long amount, Type type) {
        if(type == Type.REMOVE) super.setHealth(super.getHealth() - amount);
        if(type == Type.ADD) super.setHealth(super.getHealth() + amount);
        StatDisplay.getStatDisplay(playerUUID).updateDisplay();
    }

    public void resetHealth() {
        super.setHealth(baseMaxHealth);
        StatDisplay.getStatDisplay(playerUUID).updateDisplay();
    }

    public void clear() {
        super.clearStats();
    }

    public StatModel getStats() {
        return super.getStats();
    }

    public long getHealth() {
        return super.getHealth();
    }

    private long calcTotalMaxHealth() {
        return baseMaxHealth + getArmorPDC(ItemPDC.ITEM_STAT_MAX_HEALTH) + getHeldPDC(ItemPDC.ITEM_STAT_MAX_HEALTH);
    }

    private long calcTotalDefence() {
        return baseDefence + getArmorPDC(ItemPDC.ITEM_STAT_DEFENCE) + getHeldPDC(ItemPDC.ITEM_STAT_DEFENCE);
    }

    private long calcTotalRegeneration() {
        return baseRegeneration + getArmorPDC(ItemPDC.ITEM_STAT_REGENERATION) + getHeldPDC(ItemPDC.ITEM_STAT_REGENERATION);
    }

    private long getArmorPDC(ItemPDC itemPDC) {
        long totalValue = 0;
        for(ItemStack item : player.getInventory().getArmorContents()) {
            if(item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(itemPDC.getKey(), PersistentDataType.LONG)) {
                totalValue += item.getItemMeta().getPersistentDataContainer().get(itemPDC.getKey(), PersistentDataType.LONG);
            }
        }
        return totalValue;
    }

    private long getHeldPDC(ItemPDC itemPDC) {
        long totalValue = 0;
        if(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(itemPDC.getKey(), PersistentDataType.LONG)) {
            totalValue += player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(itemPDC.getKey(), PersistentDataType.LONG);
        }
        return totalValue;
    }

    public static StatManager getStatManager(UUID playerUUID) {
        return statManager.get(playerUUID);
    }

    public enum Type {
        REMOVE, ADD
    }
}
